package utils

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

fun modMessage(message: Any?, prefix: String = "§dGC §8»§r ") {
    val text = Text.literal("$prefix$message")

    MinecraftClient.getInstance().inGameHud.chatHud.addMessage(text)
}