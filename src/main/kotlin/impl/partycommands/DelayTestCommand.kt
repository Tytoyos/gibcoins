package impl.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class DelayTestCommand : PartyCommand {
    override val name = "delaytest"
    override fun execute(sender: String, args: List<String>): String {

        //val response = args[0]

        CompletableFuture.delayedExecutor(1000, TimeUnit.MILLISECONDS).execute {
            MinecraftClient.getInstance().execute {
                MinecraftClient.getInstance().networkHandler?.sendChatMessage("Kaiine smells")
            }
        }
        return ""
    }
}