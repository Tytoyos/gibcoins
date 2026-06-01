package impl.qol

import clickgui.GibCoinsConfig
import com.github.tytoyos.gibcoins.mixin.KeyBindingAccessor
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.Items
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket
import net.minecraft.particle.ParticleTypes
import kotlin.math.sqrt

object LeapFrog {
    private const val TICKS_BEFORE_BITE = 12.0
    private const val FISH_WAKE_DISTANCE_PER_TICK = 0.1
    private const val TRIGGER_DISTANCE = TICKS_BEFORE_BITE * FISH_WAKE_DISTANCE_PER_TICK
    private const val DISTANCE_EPSILON = 0.08
    private const val MIN_WAKE_DISTANCE = 0.2
    private const val MAX_WAKE_DISTANCE = 8.5
    private const val SIMULATED_JUMP_HOLD_TICKS = 2

    private var enabled = false
    private var currentBobberId = -1
    private var jumpedBobberId = -1
    private var simulatedJumpReleaseTicks = 0

    fun register() {
        ClientTickEvents.START_CLIENT_TICK.register { client ->
            tick(client)
        }
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            releaseSimulatedJump(client)
        }
    }

    @JvmStatic
    fun handleParticle(packet: ParticleS2CPacket) {
        if (
            !enabled ||
            packet.count != 0 ||
            packet.parameters.type != ParticleTypes.FISHING
        ) {
            return
        }

        val client = MinecraftClient.getInstance()
        if (client.currentScreen != null || !client.isWindowFocused) {
            return
        }

        val player = client.player ?: return
        val bobber = player.fishHook ?: return
        if (!isHoldingFishingRod(player)) {
            resetBobberState()
            return
        }

        updateBobberState(bobber.id)
        if (jumpedBobberId == bobber.id) {
            return
        }

        val distanceToWake = horizontalDistance(packet.x, packet.z, bobber.x, bobber.z)
        if (distanceToWake < MIN_WAKE_DISTANCE || distanceToWake > MAX_WAKE_DISTANCE) {
            return
        }

        if (distanceToWake <= TRIGGER_DISTANCE + DISTANCE_EPSILON) {
            if (pressJumpOnce(client, player)) {
                jumpedBobberId = bobber.id
            }
        }
    }

    @JvmStatic
    fun toggleEnabled(): Boolean {
        enabled = !enabled
        resetBobberState()
        GibCoinsConfig.save()
        return enabled
    }

    @JvmStatic
    fun setEnabled(value: Boolean) {
        enabled = value
        resetBobberState()
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    private fun tick(client: MinecraftClient) {
        val player = client.player
        val bobber = player?.fishHook
        if (!enabled || player == null || bobber == null || !isHoldingFishingRod(player)) {
            resetBobberState()
            return
        }

        updateBobberState(bobber.id)
    }

    private fun updateBobberState(bobberId: Int) {
        if (currentBobberId == bobberId) {
            return
        }

        currentBobberId = bobberId
        jumpedBobberId = -1
    }

    private fun resetBobberState() {
        currentBobberId = -1
        jumpedBobberId = -1
    }

    private fun pressJumpOnce(client: MinecraftClient, player: ClientPlayerEntity): Boolean {
        if (
            !player.isOnGround ||
            player.hasVehicle() ||
            player.abilities.flying ||
            player.isGliding
        ) {
            return false
        }

        val jumpKey = client.options.jumpKey
        if (!jumpKey.isPressed) {
            jumpKey.setPressed(true)
            simulatedJumpReleaseTicks = SIMULATED_JUMP_HOLD_TICKS
        }

        val jumpKeyAccessor = jumpKey as KeyBindingAccessor
        jumpKeyAccessor.setTimesPressed(jumpKeyAccessor.getTimesPressed() + 1)
        return true
    }

    private fun releaseSimulatedJump(client: MinecraftClient) {
        if (simulatedJumpReleaseTicks <= 0) {
            return
        }

        simulatedJumpReleaseTicks--
        if (simulatedJumpReleaseTicks > 0) {
            return
        }

        client.options.jumpKey.setPressed(false)
    }

    private fun isHoldingFishingRod(player: ClientPlayerEntity): Boolean {
        return player.mainHandStack.isOf(Items.FISHING_ROD) ||
            player.offHandStack.isOf(Items.FISHING_ROD)
    }

    private fun horizontalDistance(x1: Double, z1: Double, x2: Double, z2: Double): Double {
        val dx = x1 - x2
        val dz = z1 - z2
        return sqrt(dx * dx + dz * dz)
    }
}
