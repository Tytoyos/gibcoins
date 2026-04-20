package commands

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object CommandManager {

    fun processIncomingChat(plainText: String) {
<<<<<<< HEAD
        val cleanText = plainText.replace("§[0-9a-fk-orx]".toRegex(), "")
        if (cleanText.contains("Party >")) {
            val actualMessage = cleanText.substringAfter(": ").trim()
=======
        if (plainText.contains("Party >")) {
            val actualMessage = plainText.substringAfter(": ").trim()
>>>>>>> 950f7f09dec512bc8c13a18c12dcf08d83e99a18
            val response = handleChat(actualMessage)

            if (response != null) {
                val client = MinecraftClient.getInstance()
                if (client.networkHandler != null) {
                    client.networkHandler?.sendChatMessage("/pc $response")
                } else {
                    client.player?.sendMessage(Text.literal("§d[Mock Party Chat] §f/pc $response"), false)
                }
            }
        }
    }

    private val commands = mutableMapOf<String, PartyCommand>()
    private const val PREFIX = "!"

    init {
        // Register commands here
        register(EightBallCommand())
    }

    private fun register(cmd: PartyCommand) {
        commands[cmd.name.lowercase()] = cmd
    }

    fun handleChat(message: String): String? {
        if (!message.startsWith(PREFIX)) return null

        val parts = message.removePrefix(PREFIX).split(" ")
        val commandName = parts[0].lowercase()
        val args = parts.drop(1)

        return commands[commandName]?.execute(args)
    }
}
