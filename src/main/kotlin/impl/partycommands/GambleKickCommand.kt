package impl.partycommands

import net.minecraft.client.MinecraftClient


class GambleKickCommand : PartyCommand {
    override val name = "gamblekick"

    override fun execute(sender: String, args: List<String>): String {
        if (args.isEmpty()) return "Usage: !gamblekick <player>"

        val target = args[0]
        val kickRoll = (1..4).random()
        val playerName = MinecraftClient.getInstance().session.username
        val selfKick = (1..10).random()

        if (kickRoll == 2) {
            return "!kick $target"
        }

        if (sender.equals(playerName, true) && selfKick == 5) {
            return "/p leave"
        }

        return "$target was lucky."
    }
}