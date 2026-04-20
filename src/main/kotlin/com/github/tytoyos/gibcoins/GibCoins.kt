package com.github.tytoyos.gibcoins

import commands.CommandManager
import commands.FunFactCommand
import commands.TestPartyCommand
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
<<<<<<< HEAD
=======
import net.minecraft.client.MinecraftClient
>>>>>>> 950f7f09dec512bc8c13a18c12dcf08d83e99a18
import org.slf4j.LoggerFactory

object GibCoins : ClientModInitializer {
	private val logger = LoggerFactory.getLogger("gibcoins")
	override fun onInitializeClient() {
		FunFactCommand().initialize()
		TestPartyCommand().initialize()

		ClientReceiveMessageEvents.CHAT.register { message, _, _, _, _ ->
			CommandManager.processIncomingChat(message.string)
		}

<<<<<<< HEAD
		ClientReceiveMessageEvents.GAME.register { message, overlay ->
			if (!overlay) {
				CommandManager.processIncomingChat(message.string)
			}
		}

=======
>>>>>>> 950f7f09dec512bc8c13a18c12dcf08d83e99a18
		logger.info("GibCoins loaded successfully")
	}
}
