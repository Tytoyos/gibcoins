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
    fun roll() {
        val name = listOf("§c[§6ዞ§c] Benjamin Netanyahu","§c[§6ዞ§c] Donald Trump", "§c[§6ዞ§c] Jeffrey Epstein", "§c[§6ዞ§c] Hypixel", "§b[MVP§a+§b]Cata50GodLovesM7")
        val playerName = MinecraftClient.getInstance().session.username
        val message = listOf("nice weather.", "feet :drool:", "$playerName seems kinda gay...", "yea you're getting banned, $playerName.","$playerName could use some estrogen.")




        if (Random.nextDouble() < 0.5) {
            modMessage("${name.random()}§f: ${message.random()}","")
        }
    }
}
