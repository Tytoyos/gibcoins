package commands

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class HelpCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("tyhelp")
                .executes { context ->
                    context.source.sendFeedback(Text.literal("List of Party Commands: \n" +
                            "!forcefem <name>  -> forcefully feminizes a player. \n" +
                            "!gamblekick <name> -> has a 25% chance to kick the target. \n"))
                    1
                }
        )
    }
}