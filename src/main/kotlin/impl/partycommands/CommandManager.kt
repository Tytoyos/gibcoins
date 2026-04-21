package impl.partycommands

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object CommandManager {

    private val commands = mutableMapOf<String, PartyCommand>()
    private const val PREFIX = "!"
    private val partyMembers = mutableSetOf<String>()

    fun processIncomingChat(plainText: String) {
        val cleanText = plainText.replace("§[0-9a-fk-orx]".toRegex(), "")
/*        val joinParty = Regex("(?:\\[.*?] )?(\\w+) joined the party\\.").find(cleanText)
        joinParty?.let { partyMembers.add(it.groupValues[1]) }
        val leaveParty = Regex("(?:\\[.*?] )?(\\w+) (?:has left|has been removed|was removed) from the party\\.").find(cleanText)
        leaveParty?.let {
            val name = it.groupValues[1]
            partyMembers.remove(name)
        }

        val disbandMessages = listOf(
            "has disbanded the party!",
            "You left the party.",
            "You are not currently in a party.",
            "The party was disbanded"
        )

        if (disbandMessages.any { cleanText.contains(it) }) {
            partyMembers.clear()
        }

        if (cleanText.startsWith("Party Leader:") || cleanText.startsWith("Party Members:")) {
            val names = cleanText.substringAfter(":").split(" ●")
            names.forEach { segment ->
                val name = segment.replace("●", "").trim().split(" ").last()
                if (name.isNotEmpty()) partyMembers.add(name)
            }
        } */

        if (cleanText.contains("Party >")) {
            val actualMessage = cleanText.substringAfterLast(": ").trim()
            val prefixPart = cleanText.substringBeforeLast(":")
            val senderName = prefixPart.split(" ").last().trim()
            val response = handleChat(senderName, actualMessage)

            if (response != null) {
                val client = MinecraftClient.getInstance()
                if (client.networkHandler != null) {
                    client.networkHandler?.sendChatCommand("pc $response")
                } else {
                    client.player?.sendMessage(Text.literal("§d[Test Party Chat] §f/pc $response"), false)
                }
            }
        }
    }

    init {
        // Register commands here
        register(EightBallCommand())
        register(ForcefemCommand())
        register(GambleKickCommand())
    }

    private fun register(cmd: PartyCommand) {
        commands[cmd.name.lowercase()] = cmd
    }

    fun handleChat(sender: String, message: String): String? {
        if (!message.startsWith(PREFIX)) return null

        val parts = message.removePrefix(PREFIX).split(" ")
        val commandName = parts[0].lowercase()
        val args = parts.drop(1)


        return commands[commandName]?.execute(sender, args)
    }
}