package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.model.special.PlayerHeadModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerHeadModelRenderer.class)
public class PlayerHeadModelRendererMixin {
	@Redirect(
		method = "render(Lnet/minecraft/client/texture/PlayerSkinCache$Entry;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;render(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
		)
	)
	private void gibcoins$renderGhostHeldPlayerHead(
		Direction direction,
		float yaw,
		float animationProgress,
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		SkullBlockEntityModel model,
		RenderLayer renderLayer,
		int color,
		ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay
	) {
		boolean ghost = NearbyPlayerHiderRenderContext.isGhostItemRenderActive();
		NearbyPlayerHiderRenderContext.setGhostSkullRender(ghost);
		try {
			SkullBlockEntityRenderer.render(direction, yaw, animationProgress, matrices, queue, light, model, renderLayer, color, crumblingOverlay);
		} finally {
			NearbyPlayerHiderRenderContext.setGhostSkullRender(false);
		}
	}
}
