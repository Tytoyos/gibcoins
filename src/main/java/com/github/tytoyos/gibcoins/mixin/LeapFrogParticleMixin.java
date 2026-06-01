package com.github.tytoyos.gibcoins.mixin;

import impl.qol.LeapFrog;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class LeapFrogParticleMixin {
	@Inject(method = "onParticle", at = @At("TAIL"))
	private void gibcoins$handleFishingParticle(ParticleS2CPacket packet, CallbackInfo ci) {
		LeapFrog.handleParticle(packet);
	}
}
