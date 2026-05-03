package commands

import com.mojang.brigadier.CommandDispatcher
import impl.qol.NearbyPlayerHider
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import utils.modMessage

class HidePlayersCommand : BaseCommand() {
    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    ) {
        dispatcher.register(
            ClientCommandManager.literal("hideplayers")
                .executes {
                    val enabled = NearbyPlayerHider.toggle()
                    modMessage("Hide nearby players: ${if (enabled) "enabled" else "disabled"}")
                    1
                }
                .then(
                    ClientCommandManager.literal("on")
                        .executes {
                            NearbyPlayerHider.setEnabled(true)
                            modMessage("Hide nearby players: enabled")
                            1
                        }
                )
                .then(
                    ClientCommandManager.literal("off")
                        .executes {
                            NearbyPlayerHider.setEnabled(false)
                            modMessage("Hide nearby players: disabled")
                            1
                        }
                )
        )
    }
}