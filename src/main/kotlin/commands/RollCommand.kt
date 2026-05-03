package commands

import com.mojang.brigadier.CommandDispatcher
import impl.qol.SystemNotifier
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

class RollCommand :BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("roll")
                .executes {SystemNotifier.roll()
                    1})
    }
}
