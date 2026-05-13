package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.PlayerHiderRenderStateExt;
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
		PlayerHiderRenderStateExt hiderState = (PlayerHiderRenderStateExt) renderState;
		hiderState.gibcoins$setPlayerHiderHidden(false);
		hiderState.gibcoins$setPlayerHiderGhost(false);

		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity localPlayer = client.player;

		if (!NearbyPlayerHider.shouldHide(localPlayer, renderedPlayer, renderState.x, renderState.y, renderState.z)) {
			return;
		}

		boolean ghost = NearbyPlayerHider.shouldRenderAsGhost(
			localPlayer,
			renderedPlayer,
			renderState.x,
			renderState.y,
			renderState.z
		);
		hiderState.gibcoins$setPlayerHiderHidden(true);
		hiderState.gibcoins$setPlayerHiderGhost(ghost);
		renderState.invisible = true;
		renderState.invisibleToPlayer = !ghost;
		renderState.playerName = null;
		renderState.displayName = null;
	}
}
