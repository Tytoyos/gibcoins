package debug

import com.mojang.brigadier.CommandDispatcher
import commands.BaseCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class DebugCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("gcdebug")
                .executes { context ->
                    val client = context.source.client
                    val username = client.session.username
                    if (username != "Tytoyos") {
                        context.source.sendFeedback(Text.literal("You are not allowed to use this command."))
                        return@executes 0
                    }

                    val enabled = DebugMode.toggleEnabled()
                    context.source.sendFeedback(Text.literal("Debug mode ${if (enabled) "enabled" else "disabled"}"))
                    1
                }
        )
    }
}