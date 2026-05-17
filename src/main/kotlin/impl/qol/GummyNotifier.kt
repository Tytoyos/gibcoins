package impl.qol

import clickgui.GibCoinsConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object GummyNotifier {
    private const val EFFECT_NAME = "smoldering polarization"
    private const val MISSING_CONFIRM_TICKS = 5

    private var enabled = false
    private var wasEffectVisible = false
    private var missingTicks = 0

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (!enabled || client.player == null || client.networkHandler == null) {
                wasEffectVisible = false
                missingTicks = 0
                return@register
            }

            val effectVisible = isEffectVisible(client)
            if (effectVisible) {
                wasEffectVisible = true
                missingTicks = 0
                return@register
            }

            if (!wasEffectVisible) {
                return@register
            }

            missingTicks++
            if (missingTicks >= MISSING_CONFIRM_TICKS) {
                showTitle("Gummy Expired")
                wasEffectVisible = false
                missingTicks = 0
            }
        }
    }

    fun toggleEnabled(): Boolean {
        enabled = !enabled
        GibCoinsConfig.save()
        return enabled
    }

    fun setEnabled(value: Boolean) {
        enabled = value
        GibCoinsConfig.save()
    }

    fun isEnabled(): Boolean = enabled

    private fun isEffectVisible(client: MinecraftClient): Boolean {
        val playerList = client.networkHandler?.playerList ?: return false
        return playerList.any { entry ->
            val rowText = entry.displayName?.string ?: return@any false
            normalize(rowText).contains(EFFECT_NAME)
        }
    }

    private fun normalize(value: String): String {
        return value
            .replace(Regex("§."), "")
            .replace('\u00A0', ' ')
            .trim()
            .lowercase()
    }

    private fun showTitle(
        title: String,
        fadeInTicks: Int = 10,
        stayTicks: Int = 60,
        fadeOutTicks: Int = 20
    ) {
        val client = MinecraftClient.getInstance()
        client.inGameHud.setTitleTicks(fadeInTicks, stayTicks, fadeOutTicks)
        client.inGameHud.setTitle(Text.literal(title).formatted(Formatting.GOLD, Formatting.BOLD))
    }
}
