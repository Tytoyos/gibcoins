package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.texture.PlayerSkinCache;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.block.SkullBlock;
import net.minecraft.component.type.ProfileComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin {
	@Shadow
	@Final
	private PlayerSkinCache skinCache;

	@Inject(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void gibcoins$hideHeadFeature(
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		LivingEntityRenderState renderState,
		float limbAngle,
		float limbDistance,
		CallbackInfo ci
	) {
		if (NearbyPlayerHiderRenderContext.isPlayerHiderHidden(renderState) && !NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)) {
			ci.cancel();
		}
	}

	@Redirect(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;render(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
		)
	)
	private void gibcoins$renderGhostWornSkull(
		Direction direction,
		float yaw,
		float animationProgress,
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		SkullBlockEntityModel model,
		RenderLayer renderLayer,
		int color,
		ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay,
		MatrixStack originalMatrices,
		OrderedRenderCommandQueue originalQueue,
		int originalLight,
		LivingEntityRenderState renderState,
		float limbAngle,
		float limbDistance
	) {
		boolean ghost = NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState);
		NearbyPlayerHiderRenderContext.setGhostSkullRender(ghost);
		try {
			RenderLayer ghostRenderLayer = ghost
				? this.gibcoins$getGhostSkullRenderLayer(renderState, renderLayer)
				: renderLayer;
			SkullBlockEntityRenderer.render(
				direction,
				yaw,
				animationProgress,
				matrices,
				queue,
				light,
				model,
				ghostRenderLayer,
				color,
				crumblingOverlay
			);
		} finally {
			NearbyPlayerHiderRenderContext.setGhostSkullRender(false);
		}
	}

	private RenderLayer gibcoins$getGhostSkullRenderLayer(LivingEntityRenderState renderState, RenderLayer fallback) {
		SkullBlock.SkullType skullType = renderState.wearingSkullType;
		if (skullType == SkullBlock.Type.PLAYER) {
			ProfileComponent profile = renderState.wearingSkullProfile;
			if (profile != null) {
				return this.skinCache.get(profile).getRenderLayer();
			}
		}

		if (skullType == SkullBlock.Type.PLAYER) {
			return PlayerSkinCache.DEFAULT_RENDER_LAYER;
		}

		return fallback;
	}

	@Redirect(
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V"
		)
	)
	private void gibcoins$renderGhostHeadItem(
		ItemRenderState itemRenderState,
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		int light,
		int overlay,
		int color,
		MatrixStack originalMatrices,
		OrderedRenderCommandQueue originalQueue,
		int originalLight,
		LivingEntityRenderState renderState,
		float limbAngle,
		float limbDistance
	) {
		boolean ghost = NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState);
		NearbyPlayerHiderRenderContext.setGhostItemRender(ghost);
		try {
			itemRenderState.render(matrices, queue, light, overlay, color);
		} finally {
			NearbyPlayerHiderRenderContext.setGhostItemRender(false);
		}
	}
}
