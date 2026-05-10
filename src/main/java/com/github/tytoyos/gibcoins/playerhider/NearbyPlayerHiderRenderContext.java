package com.github.tytoyos.gibcoins.playerhider;

public final class NearbyPlayerHiderRenderContext {
	public static final int GHOST_COLOR = 0x26FFFFFF;
	private static final ThreadLocal<Boolean> GHOST_ARMOR_RENDER = ThreadLocal.withInitial(() -> false);

	private NearbyPlayerHiderRenderContext() {
	}

	public static boolean isPlayerHiderHidden(Object renderState) {
		return renderState instanceof PlayerHiderRenderStateExt ext && ext.gibcoins$isPlayerHiderHidden();
	}

	public static boolean isPlayerHiderGhost(Object renderState) {
		return renderState instanceof PlayerHiderRenderStateExt ext && ext.gibcoins$isPlayerHiderGhost();
	}

	public static void setGhostArmorRender(boolean active) {
		GHOST_ARMOR_RENDER.set(active);
	}

	public static boolean isGhostArmorRenderActive() {
		return GHOST_ARMOR_RENDER.get();
	}
}
