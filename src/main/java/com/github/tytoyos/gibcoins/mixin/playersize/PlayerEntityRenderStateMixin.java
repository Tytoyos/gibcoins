package com.github.tytoyos.gibcoins.mixin.playersize;

import com.github.tytoyos.gibcoins.playersize.PlayerSizeRenderStateExt;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements PlayerSizeRenderStateExt {
	@Unique
	private boolean gibcoins$shouldPlayerSizeScale;

	@Override
	public boolean gibcoins$shouldPlayerSizeScale() {
		return this.gibcoins$shouldPlayerSizeScale;
	}

	@Override
	public void gibcoins$setPlayerSizeScale(boolean shouldScale) {
		this.gibcoins$shouldPlayerSizeScale = shouldScale;
	}
}
