package impl.partycommands

import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


class GambleKickCommand : PartyCommand {
    override val name = "gamblekick"

    override fun execute(sender: String, args: List<String>): String {
        if (args.isEmpty()) {
            return "Usage: !gamblekick <player>"
        } else {
        val target = args[0]
        val kickRoll = (1..3).random()
        val playerName = MinecraftClient.getInstance().session.username
        val selfKick = (1..7).random()

        if (kickRoll == 2) {

            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage("!kick $target")
                }
            }
            return ""
        }

        if (sender.equals(playerName, true) && selfKick == 5) {

            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage("/p leave")
                }
            }
            return ""
        }

        if (target.lowercase() == "jakelegend") {

            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage("!kick JakeLegend")
                }
            }
            return ""
        }
            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute {
                MinecraftClient.getInstance().execute {
                    MinecraftClient.getInstance().networkHandler?.sendChatMessage("$target was lucky!")
                }
            }
        return ""
        }
    }
}