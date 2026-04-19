package commands

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

abstract class BaseCommand {
    protected abstract fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        registryAccess: CommandRegistryAccess
    )

    fun initialize() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            register(dispatcher, registryAccess)
        }
    }
}