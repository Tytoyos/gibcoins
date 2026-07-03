package impl.`fun`.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class DomCommand : PartyCommand {
    override val name = "dom"
    override fun execute(sender: String, args: List<String>): String {

        val target = args[0].replaceFirstChar { c -> c.uppercase() }
        val user = sender.replaceFirstChar { c -> c.uppercase() }

        val dom = listOf("$user is dominating $target", "$target has been reminded who is in charge.", "Making $target sit down and behave.", "$target now responds with 'yes, boss.'",
            "$target has become suspiciously eager to follow instructions.", "$target folded almost immediately.", "$target now waits for permission.", "$target has become incredibly cooperative.",
            "Putting $target on a very short leash.", "Putting a tight choker on $target", "$target now seeks approval far more than they'd like to admit.", "Making $target stand in the corner.",
            "$target now looks at $user before making decisions.", "$target is about to become very obedient.", "Making $target submit to $user's power.", "That armor wont save $target now!",
            "$target will NOT be able to resist this one.")

        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
            MinecraftClient.getInstance().execute {
                MinecraftClient.getInstance().networkHandler?.sendChatMessage(dom.random())
            }
        }
        return ""
    }
}