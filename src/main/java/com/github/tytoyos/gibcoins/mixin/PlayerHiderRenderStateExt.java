package com.github.tytoyos.gibcoins.mixin;

public interface PlayerHiderRenderStateExt {
	boolean gibcoins$isPlayerHiderHidden();

	void gibcoins$setPlayerHiderHidden(boolean hidden);

	boolean gibcoins$isPlayerHiderGhost();

	void gibcoins$setPlayerHiderGhost(boolean ghost);
}
