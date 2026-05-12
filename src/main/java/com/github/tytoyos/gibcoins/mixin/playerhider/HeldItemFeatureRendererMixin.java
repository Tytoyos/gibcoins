package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
		if (NearbyPlayerHiderRenderContext.isPlayerHiderHidden(renderState)) {
			ci.cancel();
		}
	}
}
