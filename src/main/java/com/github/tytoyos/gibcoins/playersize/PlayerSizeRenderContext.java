package com.github.tytoyos.gibcoins.playersize;

public final class PlayerSizeRenderContext {
	private PlayerSizeRenderContext() {
	}

	public static boolean shouldScale(Object renderState) {
		return renderState instanceof PlayerSizeRenderStateExt ext && ext.gibcoins$shouldPlayerSizeScale();
	}

	public static void setShouldScale(Object renderState, boolean shouldScale) {
		if (renderState instanceof PlayerSizeRenderStateExt ext) {
			ext.gibcoins$setPlayerSizeScale(shouldScale);
		}
	}
}
