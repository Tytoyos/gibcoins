package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	@ModifyVariable(
		method = "getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;",
		at = @At("HEAD"),
		argsOnly = true,
		ordinal = 1
	)
	private boolean gibcoins$forceGhostTranslucent(
		boolean translucent,
		LivingEntityRenderState renderState,
		boolean showBody,
		boolean unusedTranslucent,
		boolean showOutline
	) {
		return NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState) || translucent;
	}

	@Inject(
		method = "getMixColor(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)I",
		at = @At("RETURN"),
		cancellable = true
	)
	private void gibcoins$ghostMixColor(LivingEntityRenderState renderState, CallbackInfoReturnable<Integer> cir) {
		if (NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)) {
			cir.setReturnValue(NearbyPlayerHiderRenderContext.applyGhostOpacity(cir.getReturnValueI()));
		}
	}
}
