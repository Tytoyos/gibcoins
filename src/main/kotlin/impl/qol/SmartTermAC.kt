package impl.qol

import clickgui.GibCoinsConfig
import com.github.tytoyos.gibcoins.mixin.KeyBindingAccessor
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import utils.ItemUtils

object SmartTermAC {
    private const val TERMINATOR_ID = "TERMINATOR"
    private const val CLICKS_PER_SECOND = 7.0
    private const val REND_LEVEL = 1
    private val REND_ENCHANT_IDS = setOf("rend", "ultimate_rend")

    private var enabled = false
    private var nextLeftClick = 0L

    fun register() {
        ClientTickEvents.START_CLIENT_TICK.register { client ->
            tick(client)
        }
    }

    @JvmStatic
    fun toggleEnabled(): Boolean {
        enabled = !enabled
        resetClickState()
        GibCoinsConfig.save()
        return enabled
    }

    @JvmStatic
    fun setEnabled(value: Boolean) {
        enabled = value
        resetClickState()
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    private fun tick(client: MinecraftClient) {
        if (!shouldClick(client)) {
            resetClickState()
            return
        }

        val now = System.currentTimeMillis()
        if (now < nextLeftClick) {
            return
        }

        nextLeftClick = getNextClick(now)
        queueAttackClick(client)
    }

    private fun shouldClick(client: MinecraftClient): Boolean {
        if (!enabled || client.currentScreen != null || !client.isWindowFocused) {
            return false
        }

        val player = client.player ?: return false
        if (player.isUsingItem || !client.options.useKey.isPressed) {
            return false
        }

        val heldStack = player.mainHandStack
        return with(ItemUtils) {
            heldStack.skyblockId == TERMINATOR_ID &&
                !heldStack.hasSkyblockEnchantAtLeast(REND_ENCHANT_IDS, REND_LEVEL)
        }
    }

    private fun queueAttackClick(client: MinecraftClient) {
        val attackKey = client.options.attackKey as KeyBindingAccessor
        attackKey.setTimesPressed(attackKey.getTimesPressed() + 1)
    }

    private fun getNextClick(now: Long): Long {
        val delay = (1000.0 / CLICKS_PER_SECOND).toLong()
        val randomOffset = (java.util.Random().nextGaussian() * 60.0 - 30.0).toLong()
        return now + delay + randomOffset
    }

    private fun resetClickState() {
        nextLeftClick = 0L
    }
}
