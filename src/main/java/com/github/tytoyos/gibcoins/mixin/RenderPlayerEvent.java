package com.github.tytoyos.gibcoins.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class RenderPlayerEvent {

    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    private void onRender(LivingEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {



        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity localPlayer = mc.player;

        if (localPlayer == null) return;

        if (!(entity instanceof AbstractClientPlayerEntity) || entity == localPlayer) {
            return;
        }

        double distanceSq = entity.squaredDistanceTo(localPlayer);

        if (distanceSq < 2.25) {
            info.cancel();
        }
    }
}