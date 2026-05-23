package com.github.tytoyos.gibcoins.mixin.ignoregrass;

import impl.qol.IgnoreGrass;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Inject(method = "getCrosshairTarget", at = @At("RETURN"), cancellable = true)
	private void gibcoins$ignoreGrassCrosshairBlock(float tickDelta, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
		HitResult hitResult = cir.getReturnValue();
		if (!(hitResult instanceof BlockHitResult blockHitResult)) {
			return;
		}

		ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
		BlockState hitState = self.getEntityWorld().getBlockState(blockHitResult.getBlockPos());
		if (!IgnoreGrass.shouldIgnore(hitState)) {
			return;
		}

		cir.setReturnValue(gibcoins$findEntityOrBlock(self, cameraEntity, tickDelta, blockHitResult));
	}

	private static HitResult gibcoins$findEntityOrBlock(
		ClientPlayerEntity player,
		Entity cameraEntity,
		float tickDelta,
		BlockHitResult fallback
	) {
		ItemStack heldStack = player.getActiveOrMainHandStack();
		AttackRangeComponent attackRange = heldStack.get(DataComponentTypes.ATTACK_RANGE);
		if (attackRange != null) {
			HitResult hitResult = attackRange.getHitResult(cameraEntity, tickDelta, EntityPredicates.CAN_HIT::test);
			if (hitResult.getType() == HitResult.Type.ENTITY) {
				return hitResult;
			}
		}

		double entityRange = player.getEntityInteractionRange();
		Vec3d start = cameraEntity.getCameraPosVec(tickDelta);
		Vec3d rotation = cameraEntity.getRotationVec(tickDelta);
		Vec3d entityEnd = start.add(rotation.multiply(entityRange));

		Box searchBox = cameraEntity.getBoundingBox()
			.stretch(rotation.multiply(entityRange))
			.expand(1.0);

		EntityHitResult entityHit = ProjectileUtil.raycast(
			cameraEntity,
			start,
			entityEnd,
			searchBox,
			EntityPredicates.CAN_HIT::test,
			entityRange * entityRange
		);

		if (entityHit != null) {
			return entityHit;
		}

		return fallback;
	}
}
