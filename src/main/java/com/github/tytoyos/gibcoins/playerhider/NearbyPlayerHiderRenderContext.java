package com.github.tytoyos.gibcoins.playerhider;

import impl.qol.NearbyPlayerHider;
import net.minecraft.util.math.ColorHelper;

public final class NearbyPlayerHiderRenderContext {
	public static final int GHOST_ITEM_TINT_SENTINEL = 0x67686274;
	private static final ThreadLocal<Boolean> GHOST_ARMOR_RENDER = ThreadLocal.withInitial(() -> false);
	private static final ThreadLocal<Boolean> GHOST_ITEM_RENDER = ThreadLocal.withInitial(() -> false);
	private static final ThreadLocal<Boolean> GHOST_ITEM_QUADS_RENDER = ThreadLocal.withInitial(() -> false);
	private static final ThreadLocal<Boolean> GHOST_SKULL_RENDER = ThreadLocal.withInitial(() -> false);

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

	public static void setGhostItemRender(boolean active) {
		GHOST_ITEM_RENDER.set(active);
	}

	public static boolean isGhostItemRenderActive() {
		return GHOST_ITEM_RENDER.get();
	}

	public static void setGhostItemQuadsRender(boolean active) {
		GHOST_ITEM_QUADS_RENDER.set(active);
	}

	public static boolean isGhostItemQuadsRenderActive() {
		return GHOST_ITEM_QUADS_RENDER.get();
	}

	public static void setGhostSkullRender(boolean active) {
		GHOST_SKULL_RENDER.set(active);
	}

	public static boolean isGhostSkullRenderActive() {
		return GHOST_SKULL_RENDER.get();
	}

	public static int getGhostAlpha() {
		return NearbyPlayerHider.getGhostOpacityAlpha();
	}

	public static int applyGhostOpacity(int color) {
		return ColorHelper.mix(ColorHelper.getWhite(getGhostAlpha() / 255.0F), color == 0 ? -1 : color);
	}
}
