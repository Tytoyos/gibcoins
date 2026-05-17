package debug

import com.github.tytoyos.gibcoins.GibCoins
import com.mojang.brigadier.CommandDispatcher
import commands.BaseCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class TestInvProc : BaseCommand() {
    private val text = listOf(
        "Your \u269A Bonzo's Mask saved your life!",
        "Your Bonzo's Mask saved your life!",
        "Second Wind Activated! Your Spirit Mask saved your life!",
        "Your Phoenix Pet saved you from certain death!"
    )

    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("inv")
                .executes { context ->
                    if (!DebugMode.isEnabled()) {
                        context.source.sendFeedback(Text.literal("Debug mode is disabled."))
                        return@executes 0
                    }
                    val trigger = text.random()
                    MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(trigger))
                    GibCoins.handleIncomingChatLine(trigger)
                    1
                }
        )
    }
}