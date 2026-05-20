package debug

import com.mojang.brigadier.CommandDispatcher
import commands.BaseCommand
import impl.`fun`.SystemNotifier
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class RollCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("roll")
                .executes { context ->
                    if (!DebugMode.isEnabled()) {
                        context.source.sendFeedback(Text.literal("Debug mode is disabled."))
                        return@executes 0
                    }
                    SystemNotifier.roll()
                    1
                })
    }
}