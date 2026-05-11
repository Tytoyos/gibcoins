package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

	@Redirect(
		method = "renderItem(Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V"
		)
	)
	private void gibcoins$ghostHeldItems(
		ItemRenderState itemRenderState,
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		int overlay,
		int color,
		ArmedEntityRenderState renderState,
		ItemRenderState unusedItemState,
		ItemStack stack,
		Arm arm,
		MatrixStack unusedMatrices,
		OrderedRenderCommandQueue unusedQueue,
		int unusedLight
	) {
		boolean ghost = NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState);
		NearbyPlayerHiderRenderContext.setGhostItemRender(ghost);
		try {
			itemRenderState.render(
				matrices,
				queue,
				light,
				overlay,
				color
			);
		} finally {
			NearbyPlayerHiderRenderContext.setGhostItemRender(false);
		}
	}
}
