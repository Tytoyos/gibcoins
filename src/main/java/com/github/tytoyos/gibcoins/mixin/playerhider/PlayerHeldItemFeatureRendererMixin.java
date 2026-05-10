package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerHeldItemFeatureRenderer.class)
public class PlayerHeldItemFeatureRendererMixin {
	@Redirect(
		method = "renderSpyglass(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V"
		)
	)
	private void gibcoins$ghostSpyglass(
		ItemRenderState itemRenderState,
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		int overlay,
		int color,
		PlayerEntityRenderState renderState,
		Arm arm,
		MatrixStack unusedMatrices,
		OrderedRenderCommandQueue unusedQueue,
		int unusedLight
	) {
		itemRenderState.render(
			matrices,
			queue,
			light,
			overlay,
			NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)
				? NearbyPlayerHiderRenderContext.GHOST_COLOR
				: color
		);
	}
}
