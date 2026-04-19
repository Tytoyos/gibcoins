package commands

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class FunFactCommand : BaseCommand() {

    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("funfact")
                .executes { context ->
                    context.source.sendFeedback(Text.literal("§d[Fun Fact] §eKaiine is everyone's pet!"))
                    1
                }
        )
    }
}