package com.github.tytoyos.gibcoins.mixin;

import impl.qol.NearbyPlayerHider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
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
	private void gibcoins$clickThroughHiddenPlayers(float tickDelta, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
		HitResult hitResult = cir.getReturnValue();
		if (!(hitResult instanceof EntityHitResult entityHitResult)) {
			return;
		}

		ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
		if (!NearbyPlayerHider.shouldClickThrough(self, entityHitResult.getEntity())) {
			return;
		}

		cir.setReturnValue(gibcoins$findAlternateHit(self, cameraEntity, tickDelta));
	}

	private static HitResult gibcoins$findAlternateHit(ClientPlayerEntity player, Entity cameraEntity, float tickDelta) {
		ItemStack heldStack = player.getActiveOrMainHandStack();
		AttackRangeComponent attackRange = heldStack.get(DataComponentTypes.ATTACK_RANGE);
		if (attackRange != null) {
			HitResult hitResult = attackRange.getHitResult(
				cameraEntity,
				tickDelta,
				entity -> EntityPredicates.CAN_HIT.test(entity) && !NearbyPlayerHider.shouldClickThrough(player, entity)
			);
			if (hitResult.getType() != HitResult.Type.MISS) {
				return hitResult;
			}
		}

		double blockRange = player.getBlockInteractionRange();
		double entityRange = player.getEntityInteractionRange();
		Vec3d start = cameraEntity.getCameraPosVec(tickDelta);
		Vec3d rotation = cameraEntity.getRotationVec(tickDelta);
		Vec3d entityEnd = start.add(rotation.multiply(entityRange));

		HitResult blockHitResult = cameraEntity.raycast(blockRange, tickDelta, false);
		double maxEntityDistanceSquared = entityRange * entityRange;
		if (blockHitResult.getType() != HitResult.Type.MISS) {
			maxEntityDistanceSquared = start.squaredDistanceTo(blockHitResult.getPos());
		}

		Box searchBox = cameraEntity.getBoundingBox()
			.stretch(rotation.multiply(entityRange))
			.expand(1.0);
		EntityHitResult alternateEntityHit = ProjectileUtil.raycast(
			cameraEntity,
			start,
			entityEnd,
			searchBox,
			entity -> EntityPredicates.CAN_HIT.test(entity) && !NearbyPlayerHider.shouldClickThrough(player, entity),
			maxEntityDistanceSquared
		);

		if (alternateEntityHit != null) {
			return alternateEntityHit;
		}

		return blockHitResult;
	}
}
