package impl.`fun`.partycommands

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object CommandManager {

    private val commands = mutableMapOf<String, PartyCommand>()
    private const val PREFIX = "!"

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
                    client.networkHandler?.sendChatMessage(response)
                } else {
                    client.player?.sendMessage(Text.literal("§d[Test Party Chat] §f/pc $response"), false)
                }
            }
        }
    }

    init {
        register(ForcefemCommand())
        register(GambleKickCommand())
        register(PartyFunFact())
        register(ShitterCheck())
        register(KillCommand())
        register(DomCommand())
        register(SubCommand())
    }

    private fun register(cmd: PartyCommand) {
        commands[cmd.name.lowercase()] = cmd
    }

    fun handleChat(sender: String, message: String): String? {
        if (!PartyCommandSettings.isEnabled() || !message.startsWith(PREFIX)) return null
        val localPlayerName = MinecraftClient.getInstance().session.username
        if (PartyCommandSettings.isBlacklisted(sender)) {
            return null
        }
        if (PartyCommandSettings.isOwnerOnlyEnabled() && !sender.equals(localPlayerName, ignoreCase = true)) {
            return null
        }

        val parts = message.removePrefix(PREFIX).split(" ")
        val commandName = parts[0].lowercase()
        val args = parts.drop(1)

        if (!isCommandEnabled(commandName)) {
            return null
        }

        return commands[commandName]?.execute(sender, args)
    }

    private fun isCommandEnabled(commandName: String): Boolean {
        return when (commandName) {
            "forcefem" -> PartyCommandSettings.isForcefemEnabled()
            "gamblekick" -> PartyCommandSettings.isGambleKickEnabled()
            "funfact" -> PartyCommandSettings.isFunFactEnabled()
            "shittercheck" -> PartyCommandSettings.isShitterCheckEnabled()
            "kill" -> PartyCommandSettings.isKillEnabled()
            "dom" -> PartyCommandSettings.isDomEnabled()
            "sub" -> PartyCommandSettings.isSubEnabled()
            else -> true
        }
    }
}
