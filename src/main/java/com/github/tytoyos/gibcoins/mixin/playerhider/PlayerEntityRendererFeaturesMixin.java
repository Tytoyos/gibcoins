package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererFeaturesMixin {
	@Inject(
		method = "shouldRenderFeatures(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void gibcoins$hideAllPlayerFeatures(PlayerEntityRenderState renderState, CallbackInfoReturnable<Boolean> cir) {
		if (NearbyPlayerHiderRenderContext.isPlayerHiderHidden(renderState)) {
			cir.setReturnValue(false);
		}
	}
}
