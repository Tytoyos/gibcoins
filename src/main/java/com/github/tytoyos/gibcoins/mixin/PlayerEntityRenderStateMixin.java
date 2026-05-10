package com.github.tytoyos.gibcoins.mixin;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements PlayerHiderRenderStateExt {
	@Unique
	private boolean gibcoins$playerHiderHidden;

	@Unique
	private boolean gibcoins$playerHiderGhost;

	@Override
	public boolean gibcoins$isPlayerHiderHidden() {
		return this.gibcoins$playerHiderHidden;
	}

	@Override
	public void gibcoins$setPlayerHiderHidden(boolean hidden) {
		this.gibcoins$playerHiderHidden = hidden;
	}

	@Override
	public boolean gibcoins$isPlayerHiderGhost() {
		return this.gibcoins$playerHiderGhost;
	}

	@Override
	public void gibcoins$setPlayerHiderGhost(boolean ghost) {
		this.gibcoins$playerHiderGhost = ghost;
	}
}
