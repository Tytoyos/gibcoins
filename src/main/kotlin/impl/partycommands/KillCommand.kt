package impl.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class KillCommand : PartyCommand {
    override val name = "kill"
    override fun execute(sender: String, args: List<String>): String {
        val target = args[0]
        val kill = listOf("${target.replaceFirstChar { c -> c.uppercase() }} was stabbed.", "${target.replaceFirstChar { c -> c.uppercase() }} had their head smashed by a hammer.",
                          "${target.replaceFirstChar { c -> c.uppercase() }} was dissolved in acid.", "WOAH ${sender.uppercase()} FORGOT WHAT THEY CAME FOR AND ENDED UP SUCKING ${target.uppercase()}'S TOES!!",
                          "${sender.replaceFirstChar { c -> c.uppercase() }} sat on $target's face for too long...", "Nuked $target.", "${target.replaceFirstChar { c -> c.uppercase() }} was choked by $sender's thunderclappingly enormous thighs!")

        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
            MinecraftClient.getInstance().execute {
                MinecraftClient.getInstance().networkHandler?.sendChatMessage(kill.random())
            }
        }
        return ""
    }
}