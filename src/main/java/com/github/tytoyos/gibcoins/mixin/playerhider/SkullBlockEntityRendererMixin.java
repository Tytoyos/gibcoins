package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkullBlockEntityRenderer.class)
public class SkullBlockEntityRendererMixin {
	@Redirect(
		method = "render(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
		)
	)
	private static <S> void gibcoins$submitGhostSkullModel(
		OrderedRenderCommandQueue queue,
		Model<? super S> model,
		S state,
		MatrixStack matrices,
		RenderLayer renderLayer,
		int light,
		int overlay,
		int outlineColor,
		ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay
	) {
		if (!NearbyPlayerHiderRenderContext.isGhostSkullRenderActive()) {
			queue.submitModel(model, state, matrices, renderLayer, light, overlay, outlineColor, crumblingOverlay);
			return;
		}

		queue.submitModel(
			model,
			state,
			matrices,
			renderLayer,
			light,
			overlay,
			NearbyPlayerHiderRenderContext.applyGhostOpacity(-1),
			null,
			0,
			crumblingOverlay
		);
	}
}
