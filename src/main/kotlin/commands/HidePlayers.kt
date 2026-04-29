package commands

import com.github.tytoyos.gibcoins.impl.HidePlayersConfig
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text


object HidePlayers {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommandManager.literal("hideplayers")
                .executes { context ->
                    HidePlayersConfig.hidePlayers = !HidePlayersConfig.hidePlayers

                    val status = if (HidePlayersConfig.hidePlayers) "ENABLED" else "DISABLED"
                    val color = if (HidePlayersConfig.hidePlayers) 0x55FF55 else 0xFF5555 // Green or Red

                    context.source.sendFeedback(
                        Text.literal("Player Hider is now $status")
                            .withColor(color)
                    )

                    1
                }
        )
    }
}
