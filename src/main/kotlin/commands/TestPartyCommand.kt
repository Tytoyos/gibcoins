package commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import commands.CommandManager.processIncomingChat
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

class TestPartyCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("testpartychat")
                .then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
                    .executes { context ->
                        val testMessage = StringArgumentType.getString(context, "message")

                        val formattedMessage = "Party > [MVP+] Friend: $testMessage"

                        processIncomingChat(formattedMessage)
                        0
                    }
                )
        )
    }
}