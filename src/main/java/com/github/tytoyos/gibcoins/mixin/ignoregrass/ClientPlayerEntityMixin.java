package com.github.tytoyos.gibcoins.mixin.ignoregrass;

import impl.qol.DianaQol;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
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
		HitResult hitResult = cir.getReturnValue();
		if (hitResult instanceof EntityHitResult entityHitResult && gibcoins$isDyingLivingEntity(entityHitResult.getEntity())) {
			ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
			cir.setReturnValue(gibcoins$findBlockPastPoint(self, cameraEntity, tickDelta, hitResult.getPos()));
			return;
		}

		if (!(hitResult instanceof BlockHitResult blockHitResult)) {
			return;
		}

		ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
		BlockState hitState = self.getEntityWorld().getBlockState(blockHitResult.getBlockPos());
		if (!DianaQol.shouldIgnore(hitState)) {
			return;
		}

		cir.setReturnValue(gibcoins$findBlockBehindIgnored(self, cameraEntity, tickDelta, blockHitResult));
	}

	private static boolean gibcoins$isDyingLivingEntity(Entity entity) {
		if (!(entity instanceof LivingEntity living)) {
			return false;
		}

		return !living.isAlive() || living.getHealth() <= 0.0F || living.deathTime > 0;
	}

	private static BlockHitResult gibcoins$asMiss(Vec3d missPos, Vec3d lookVec) {
		return BlockHitResult.createMissed(
			missPos,
			Direction.getFacing(lookVec.x, lookVec.y, lookVec.z),
			BlockPos.ofFloored(missPos)
		);
	}

	private static HitResult gibcoins$findBlockBehindIgnored(
		ClientPlayerEntity player,
		Entity cameraEntity,
		float tickDelta,
		BlockHitResult firstHit
	) {
		double blockRange = player.getBlockInteractionRange();
		Vec3d lookVec = cameraEntity.getRotationVec(tickDelta);
		Vec3d end = cameraEntity.getCameraPosVec(tickDelta).add(lookVec.multiply(blockRange));
		BlockHitResult currentHit = firstHit;

		for (int i = 0; i < 16; i++) {
			BlockState state = player.getEntityWorld().getBlockState(currentHit.getBlockPos());
			if (!DianaQol.shouldIgnore(state)) {
				return currentHit;
			}

			Vec3d nextStart = currentHit.getPos().add(lookVec.multiply(0.01));
			RaycastContext context = new RaycastContext(
				nextStart,
				end,
				RaycastContext.ShapeType.OUTLINE,
				RaycastContext.FluidHandling.NONE,
				cameraEntity
			);
			BlockHitResult nextHit = player.getEntityWorld().raycast(context);
			if (nextHit.getType() == HitResult.Type.MISS) {
				return nextHit;
			}
			currentHit = nextHit;
		}

		return gibcoins$asMiss(currentHit.getPos(), lookVec);
	}

	private static HitResult gibcoins$findBlockPastPoint(
		ClientPlayerEntity player,
		Entity cameraEntity,
		float tickDelta,
		Vec3d point
	) {
		double blockRange = player.getBlockInteractionRange();
		Vec3d lookVec = cameraEntity.getRotationVec(tickDelta);
		Vec3d end = cameraEntity.getCameraPosVec(tickDelta).add(lookVec.multiply(blockRange));
		Vec3d start = point.add(lookVec.multiply(0.01));
		RaycastContext context = new RaycastContext(
			start,
			end,
			RaycastContext.ShapeType.OUTLINE,
			RaycastContext.FluidHandling.NONE,
			cameraEntity
		);
		BlockHitResult hit = player.getEntityWorld().raycast(context);
		if (hit.getType() == HitResult.Type.MISS) {
			return hit;
		}

		return gibcoins$findBlockBehindIgnored(player, cameraEntity, tickDelta, hit);
	}
}
