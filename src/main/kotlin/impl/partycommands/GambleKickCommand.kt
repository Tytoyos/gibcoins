package impl.partycommands

import net.minecraft.client.MinecraftClient
import utils.PartyUtils

/*  25% chance to kick a target.
    register partial names (tyt -> tytoyos)
 */


class GambleKickCommand : PartyCommand {
    override val name = "gamblekick"

    override fun execute(sender: String, args: List<String>): String {
        if (args.isEmpty()) return "Usage: !gamblekick <player>"

        val target = PartyUtils.findMember(args[0])
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