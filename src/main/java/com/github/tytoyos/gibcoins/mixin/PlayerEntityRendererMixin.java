package com.github.tytoyos.gibcoins.mixin;

import impl.qol.NearbyPlayerHider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
	private void gibcoins$hideNearbyPlayers(
		PlayerLikeEntity renderedPlayer,
		PlayerEntityRenderState renderState,
		float tickDelta,
		CallbackInfo ci
	) {
		if (!NearbyPlayerHider.isEnabled()) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity localPlayer = client.player;

		if (localPlayer == null || renderedPlayer == localPlayer) {
			return;
		}

		if (localPlayer.squaredDistanceTo(renderState.x, renderState.y, renderState.z) <= NearbyPlayerHider.getHideDistanceSquared()) {
			renderState.invisible = true;
			renderState.invisibleToPlayer = true;
			renderState.displayName = null;
		}
	}
}
