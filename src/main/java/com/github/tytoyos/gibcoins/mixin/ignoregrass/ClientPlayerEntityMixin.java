package com.github.tytoyos.gibcoins.mixin.ignoregrass;

import impl.qol.DianaQol;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Inject(method = "getCrosshairTarget", at = @At("RETURN"), cancellable = true)
	private void gibcoins$ignoreGrassCrosshairBlock(float tickDelta, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
		if (!DianaQol.isEnabled()) {
			return;
		}

		HitResult hitResult = cir.getReturnValue();
		if (!gibcoins$shouldRetarget((ClientPlayerEntity)(Object)this, hitResult)) {
			return;
		}

		ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
		cir.setReturnValue(gibcoins$findAlternateHit(self, cameraEntity, tickDelta));
	}

	private static boolean gibcoins$shouldRetarget(ClientPlayerEntity player, HitResult hitResult) {
		if (hitResult instanceof EntityHitResult entityHitResult) {
			return gibcoins$isDyingLivingEntity(entityHitResult.getEntity());
		}
		if (hitResult instanceof BlockHitResult blockHitResult) {
			BlockState state = player.getEntityWorld().getBlockState(blockHitResult.getBlockPos());
			return DianaQol.shouldIgnore(state);
		}
		return false;
	}

	private static HitResult gibcoins$findAlternateHit(ClientPlayerEntity player, Entity cameraEntity, float tickDelta) {
		double blockRange = player.getBlockInteractionRange();
		double entityRange = player.getEntityInteractionRange();
		Vec3d start = cameraEntity.getCameraPosVec(tickDelta);
		Vec3d lookVec = cameraEntity.getRotationVec(tickDelta);
		Vec3d blockEnd = start.add(lookVec.multiply(blockRange));
		Vec3d entityEnd = start.add(lookVec.multiply(entityRange));

		BlockHitResult blockHitResult = gibcoins$raycastIgnoringDianaBlocks(player, cameraEntity, start, blockEnd, lookVec);

		double maxEntityDistanceSquared = entityRange * entityRange;
		if (blockHitResult.getType() != HitResult.Type.MISS) {
			maxEntityDistanceSquared = start.squaredDistanceTo(blockHitResult.getPos());
		}

		Box searchBox = cameraEntity.getBoundingBox()
			.stretch(lookVec.multiply(entityRange))
			.expand(1.0);
		EntityHitResult entityHitResult = ProjectileUtil.raycast(
			cameraEntity,
			start,
			entityEnd,
			searchBox,
			entity -> EntityPredicates.CAN_HIT.test(entity) && !gibcoins$isDyingLivingEntity(entity),
			maxEntityDistanceSquared
		);

		if (entityHitResult != null) {
			return entityHitResult;
		}

		return blockHitResult;
	}

	private static BlockHitResult gibcoins$raycastIgnoringDianaBlocks(
		ClientPlayerEntity player,
		Entity cameraEntity,
		Vec3d start,
		Vec3d end,
		Vec3d lookVec
	) {
		Vec3d currentStart = start;

		for (int i = 0; i < 32; i++) {
			RaycastContext context = new RaycastContext(
				currentStart,
				end,
				RaycastContext.ShapeType.OUTLINE,
				RaycastContext.FluidHandling.NONE,
				cameraEntity
			);
			BlockHitResult hit = player.getEntityWorld().raycast(context);
			if (hit.getType() == HitResult.Type.MISS) {
				return hit;
			}

			BlockState state = player.getEntityWorld().getBlockState(hit.getBlockPos());
			if (!DianaQol.shouldIgnore(state)) {
				return hit;
			}

			currentStart = gibcoins$stepPastIgnoredHit(hit, lookVec);
			if (currentStart.squaredDistanceTo(end) <= 0.0001) {
				break;
			}
		}

		return gibcoins$asMiss(end, lookVec);
	}

	private static BlockHitResult gibcoins$asMiss(Vec3d missPos, Vec3d lookVec) {
		return BlockHitResult.createMissed(
			missPos,
			Direction.getFacing(lookVec.x, lookVec.y, lookVec.z),
			BlockPos.ofFloored(missPos)
		);
	}

	private static Vec3d gibcoins$stepPastIgnoredHit(BlockHitResult hit, Vec3d lookVec) {
		Vec3d next = hit.getPos().add(lookVec.multiply(0.05));
		BlockPos hitPos = hit.getBlockPos();

		for (int i = 0; i < 8 && BlockPos.ofFloored(next).equals(hitPos); i++) {
			next = next.add(lookVec.multiply(0.05));
		}

		return next;
	}

	private static boolean gibcoins$isDyingLivingEntity(Entity entity) {
		if (!(entity instanceof LivingEntity living)) {
			return false;
		}
		return !living.isAlive() || living.getHealth() <= 0.0F || living.deathTime > 0;
	}
}
