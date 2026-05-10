package com.github.tytoyos.gibcoins.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {
	@Inject(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void gibcoins$hideHeldItems(
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		ArmedEntityRenderState renderState,
		float limbAngle,
		float limbDistance,
		CallbackInfo ci
	) {
		if (NearbyPlayerHiderRenderContext.isPlayerHiderHidden(renderState) && !NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)) {
			ci.cancel();
		}
	}

	@ModifyArg(
		method = "renderItem(Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V"
		),
		index = 4
	)
	private int gibcoins$ghostHeldItems(int color, ArmedEntityRenderState renderState) {
		return NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)
			? NearbyPlayerHiderRenderContext.GHOST_COLOR
			: color;
	}
}
