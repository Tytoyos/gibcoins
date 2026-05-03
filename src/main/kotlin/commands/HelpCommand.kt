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
            ClientCommandManager.literal("gchelp")
                .executes { context ->
                    context.source.sendFeedback(Text.literal("§cList of Party Commands: \n" +
                            "§6!forcefem <name>  -> forcefully feminizes a player. \n" +
                            "§e!gamblekick <name> -> has a 33.33% chance to kick the target. \n" +
                            "§2!shittercheck <name> -> checks if the player is a shitter. \n" +
                            "§9!funfact -> sends a fun fact. \n" +
                            "§5!kill <name> -> will take care of your annoying party members..."))
                    1
                }
        )
    }
}