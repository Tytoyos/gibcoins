package com.github.tytoyos.gibcoins.mixin;

import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerHeldItemFeatureRenderer.class)
public class PlayerHeldItemFeatureRendererMixin {
	@ModifyArg(
		method = "renderSpyglass(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V"
		),
		index = 4
	)
	private int gibcoins$ghostSpyglass(int color, PlayerEntityRenderState renderState) {
		return NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)
			? NearbyPlayerHiderRenderContext.GHOST_COLOR
			: color;
	}
}
