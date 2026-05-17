package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@ModifyArg(
		method = "renderBakedItemQuads",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFFII)V"
		),
		index = 5
	)
	private static float gibcoins$ghostHeldItemAlpha(float alpha) {
		return NearbyPlayerHiderRenderContext.isGhostItemQuadsRenderActive()
			? (NearbyPlayerHiderRenderContext.getGhostAlpha() / 255.0F)
			: alpha;
	}
}
