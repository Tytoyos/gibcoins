package impl.qol

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import utils.modMessage
import kotlin.random.Random

object SystemNotifier {
    private var tickCounter = 0
    private var rollCD = 2400 //20 Minutes between each roll, aka 24000 Ticks.

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
        val name = listOf("Benjamin Netanyahu","Donald Trump", "Jeffrey Epstein","")
        val message = listOf("nice weather.", "feet :drool:")
        if (Random.nextDouble() < 1.0) {
            modMessage("§c[§6ዞ§c] ${name.random()}: ${message.random()}","")
        }
    }
}
