package commands

import com.mojang.brigadier.CommandDispatcher
import impl.qol.Overlay
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

class OverlayTestCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("overlay")
                .executes {
                    Overlay.show()
                    1})
    }
}