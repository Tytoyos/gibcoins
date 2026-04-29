package impl.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


class PartyFunFact : PartyCommand {
    override val name = "funfact"

    override fun execute(sender: String, args: List<String>): String {
        val fact = listOf("Kaiine is everyone's pet ❤","The rift sucks.","About 35,82% of currently active players would lick Diana's toes.")

        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
            MinecraftClient.getInstance().execute {
                MinecraftClient.getInstance().networkHandler?.sendChatMessage(fact.random())
            }
        }
        return ""
    }
}