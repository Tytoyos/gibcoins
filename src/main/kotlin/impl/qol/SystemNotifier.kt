package impl.qol

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import utils.modMessage
import kotlin.random.Random

object SystemNotifier {
    private var tickCounter = 0
    private var rollCD = 24000 //20 Minutes between each roll, aka 24000 Ticks.

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (client.player == null) return@register

            tickCounter++

            if (tickCounter >= rollCD) {
                tickCounter = 0
                roll()
            }
        }
    }
    private fun roll() {
        val name = listOf("§c[§6ዞ§c] Benjamin Netanyahu","§c[§6ዞ§c] Donald Trump", "§c[§6ዞ§c] Jeffrey Epstein", "§c[§6ዞ§c] Hypixel")
        val playerName = MinecraftClient.getInstance().session.username
        val message = listOf("nice weather.", "feet :drool:", "$playerName seems kinda gay...", "yea you're getting banned, $playerName.")


        if (Random.nextDouble() < 0.25) {
            modMessage("§f${name.random()}: ${message.random()}","")
        }
    }
}
