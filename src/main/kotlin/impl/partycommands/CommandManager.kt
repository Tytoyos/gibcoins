package impl.partycommands

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object CommandManager {

    private val commands = mutableMapOf<String, PartyCommand>()
    private const val PREFIX = "!"
    private val partyMembers = mutableSetOf<String>()

    fun processIncomingChat(plainText: String) {
        val cleanText = plainText.replace("§[0-9a-fk-orx]".toRegex(), "")
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