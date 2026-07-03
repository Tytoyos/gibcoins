package impl.qol

import clickgui.GibCoinsConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos

object PowderMining {
    private const val OPEN_COOLDOWN_MS = 250L

    private var enabled = false
    private var nextOpen = 0L
    private var lastOpenedPos: BlockPos? = null

    fun register() {
        ClientTickEvents.START_CLIENT_TICK.register { client ->
            tick(client)
        }

        AttackBlockCallback.EVENT.register { player, world, hand, pos, _ ->
            if (
                !world.isClient ||
                hand != Hand.MAIN_HAND ||
                !enabled ||
                !isOpenableChest(world.getBlockState(pos))
            ) {
                return@register ActionResult.PASS
            }

            val client = MinecraftClient.getInstance()
            if (client.player != player || !canOpen(client)) {
                return@register ActionResult.PASS
            }

            openTargetChest(client)
            ActionResult.FAIL
        }
    }

    @JvmStatic
    fun toggleEnabled(): Boolean {
        enabled = !enabled
        resetOpenState()
        GibCoinsConfig.save()
        return enabled
    }

    @JvmStatic
    fun setEnabled(value: Boolean) {
        enabled = value
        resetOpenState()
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    private fun tick(client: MinecraftClient) {
        if (!canOpen(client) || !client.options.attackKey.isPressed) {
            resetOpenState()
            return
        }

        if (!openTargetChest(client)) {
            resetOpenState()
        }
    }

    private fun canOpen(client: MinecraftClient): Boolean {
        val player = client.player ?: return false
        return enabled &&
            client.currentScreen == null &&
            client.isWindowFocused &&
            !player.isSpectator &&
            client.interactionManager != null
    }

    private fun openTargetChest(client: MinecraftClient): Boolean {
        val hitResult = client.crosshairTarget as? BlockHitResult ?: return false
        if (hitResult.type != HitResult.Type.BLOCK) {
            return false
        }

        val world = client.world ?: return false
        if (!isOpenableChest(world.getBlockState(hitResult.blockPos))) {
            return false
        }

        return openChest(client, hitResult)
    }

    private fun openChest(client: MinecraftClient, hitResult: BlockHitResult): Boolean {
        val player = client.player ?: return false
        val pos = hitResult.blockPos.toImmutable()
        val now = System.currentTimeMillis()
        if (lastOpenedPos == pos && now < nextOpen) {
            return true
        }

        lastOpenedPos = pos
        nextOpen = now + OPEN_COOLDOWN_MS

        val result = client.interactionManager?.interactBlock(player, Hand.MAIN_HAND, hitResult)
            ?: return false

        if (result is ActionResult.Success && result.swingSource() == ActionResult.SwingSource.CLIENT) {
            player.swingHand(Hand.MAIN_HAND)
        }

        return true
    }

    private fun resetOpenState() {
        nextOpen = 0L
        lastOpenedPos = null
    }

    private fun isOpenableChest(state: BlockState): Boolean {
        return state.isOf(Blocks.CHEST) ||
            state.isOf(Blocks.TRAPPED_CHEST) ||
            state.isOf(Blocks.ENDER_CHEST)
    }
}
