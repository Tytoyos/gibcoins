package impl.partycommands

import net.minecraft.client.MinecraftClient


class GambleKickCommand : PartyCommand {
    override val name = "gamblekick"

    override fun execute(sender: String, args: List<String>): String {
        if (args.isEmpty()) return "Usage: !gamblekick <player>"

        val target = args[0]
        val kickRoll = (1..4).random()
        val playerName = MinecraftClient.getInstance().session.username
        val selfKick = (1..7).random()

        if (kickRoll == 2) {
            Thread {
                Thread.sleep(1000)
                MinecraftClient.getInstance().networkHandler?.sendChatCommand("pc !kick $target")
            }
            return ""
        }

        if (sender.equals(playerName, true) && selfKick == 5) {
            Thread {
                Thread.sleep(1000)
                MinecraftClient.getInstance().networkHandler?.sendChatCommand("p leave")
            }
            return ""
        }

        Thread {
            Thread.sleep(1000)
            MinecraftClient.getInstance().networkHandler?.sendChatCommand("pc $target was lucky!")
        }

        return ""
    }
}