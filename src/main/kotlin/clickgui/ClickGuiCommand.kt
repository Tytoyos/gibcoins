package clickgui

import com.mojang.brigadier.CommandDispatcher
import commands.BaseCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

class ClickGuiCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("gc")
                .executes { context ->
                    val client = context.source.client
                    client.execute {
                        client.setScreen(ClickGuiScreen())
                    }
                    1
                }
        )
    }
}