package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Arrays;

@Mixin(targets = "net.minecraft.client.render.item.ItemRenderState$LayerRenderState")
public class ItemRenderStateLayerRenderStateMixin {
	@ModifyArg(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V"
		),
		index = 7
	)
	private RenderLayer gibcoins$ghostHeldItemLayer(RenderLayer layer) {
		if (!NearbyPlayerHiderRenderContext.isGhostItemRenderActive()) {
			return layer;
		}

		return layer == TexturedRenderLayers.getEntityCutout()
			? TexturedRenderLayers.getBlockTranslucentCull()
			: layer;
	}

	@ModifyArg(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V"
		),
		index = 8
	)
	private ItemRenderState.Glint gibcoins$hideGhostHeldItemGlint(ItemRenderState.Glint glint) {
		return NearbyPlayerHiderRenderContext.isGhostItemRenderActive() ? ItemRenderState.Glint.NONE : glint;
	}

	@ModifyArg(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V"
		),
		index = 5
	)
	private int[] gibcoins$markGhostHeldItemTints(int[] tints) {
		if (!NearbyPlayerHiderRenderContext.isGhostItemRenderActive()) {
			return tints;
		}

		int[] markedTints = Arrays.copyOf(tints, tints.length + 1);
		markedTints[tints.length] = NearbyPlayerHiderRenderContext.GHOST_ITEM_TINT_SENTINEL;
		return markedTints;
	}

	@ModifyArg(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer;render(Ljava/lang/Object;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V"
		),
		index = 6
	)
	private boolean gibcoins$hideGhostSpecialHeldItemGlint(boolean glint) {
		return !NearbyPlayerHiderRenderContext.isGhostItemRenderActive() && glint;
	}

}
