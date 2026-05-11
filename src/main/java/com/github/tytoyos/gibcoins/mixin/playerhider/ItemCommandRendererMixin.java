package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.ItemCommandRenderer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;
import java.util.List;

@Mixin(ItemCommandRenderer.class)
public class ItemCommandRendererMixin {
	@Redirect(
		method = "render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/OutlineVertexConsumerProvider;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V"
		)
	)
	private void gibcoins$renderGhostHeldItemQuads(
		ItemDisplayContext displayContext,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		int overlay,
		int[] tints,
		List<BakedQuad> quads,
		RenderLayer renderLayer,
		ItemRenderState.Glint glint,
		BatchingRenderCommandQueue queue,
		VertexConsumerProvider.Immediate immediate,
		OutlineVertexConsumerProvider outlineVertexConsumers
	) {
		boolean ghost = tints.length > 0 && tints[tints.length - 1] == NearbyPlayerHiderRenderContext.GHOST_ITEM_TINT_SENTINEL;
		int[] renderTints = ghost ? Arrays.copyOf(tints, tints.length - 1) : tints;

		NearbyPlayerHiderRenderContext.setGhostItemQuadsRender(ghost);
		try {
			ItemRenderer.renderItem(displayContext, matrices, vertexConsumers, light, overlay, renderTints, quads, renderLayer, glint);
		} finally {
			NearbyPlayerHiderRenderContext.setGhostItemQuadsRender(false);
		}
	}
}
