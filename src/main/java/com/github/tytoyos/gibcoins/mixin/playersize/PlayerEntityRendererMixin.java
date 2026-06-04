package com.github.tytoyos.gibcoins.mixin.playersize;

import com.github.tytoyos.gibcoins.playersize.PlayerSizeRenderContext;
import impl.qol.PlayerSize;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(
		method = "scale(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V",
		at = @At("TAIL")
	)
	private void gibcoins$scalePlayerModel(PlayerEntityRenderState renderState, MatrixStack matrices, CallbackInfo ci) {
		if (PlayerSizeRenderContext.shouldScale(renderState)) {
			PlayerSize.applyScale(renderState, matrices);
		}
	}

	@Inject(
		method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void gibcoins$pullNameLabelWithYScale(
		PlayerLikeEntity renderedPlayer,
		PlayerEntityRenderState renderState,
		float tickDelta,
		CallbackInfo ci
	) {
		boolean shouldScale = PlayerSize.shouldScale(renderedPlayer);
		PlayerSizeRenderContext.setShouldScale(renderState, shouldScale);
		PlayerSize.pullNameLabelWithYScale(renderState, shouldScale);
	}
}
