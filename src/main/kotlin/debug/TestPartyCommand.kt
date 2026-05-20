package debug

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import commands.BaseCommand
import impl.`fun`.partycommands.CommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class TestPartyCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("testpartychat")
                .then(
                    ClientCommandManager.argument("message", StringArgumentType.greedyString())
                    .executes { context ->
                        if (!DebugMode.isEnabled()) {
                            context.source.sendFeedback(Text.literal("Debug mode is disabled."))
                            return@executes 0
                        }
                        val testMessage = StringArgumentType.getString(context, "message")

                        val formattedMessage = "Party > [MVP+] Friend: $testMessage"

                        CommandManager.processIncomingChat(formattedMessage)
                        0
                    }
                )
        )
    }
}