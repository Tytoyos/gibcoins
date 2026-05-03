package com.github.tytoyos.gibcoins

import impl.partycommands.CommandManager
import commands.FunFactCommand
import commands.HelpCommand
import commands.HidePlayersCommand
import commands.OverlayTestCommand
import commands.RollCommand
import commands.TestPartyCommand
import impl.qol.Overlay
import impl.qol.SystemNotifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import org.slf4j.LoggerFactory

object GibCoins : ClientModInitializer {
	private val logger = LoggerFactory.getLogger("gibcoins")
	override fun onInitializeClient() {
		FunFactCommand().initialize()
		HelpCommand().initialize()
		HidePlayersCommand().initialize()
		Overlay.register()
		OverlayTestCommand().initialize()
		RollCommand().initialize()
		SystemNotifier.register()
		TestPartyCommand().initialize()


		ClientReceiveMessageEvents.CHAT.register { message, _, _, _, _ ->
			CommandManager.processIncomingChat(message.string)
		}

		ClientReceiveMessageEvents.GAME.register { message, overlay ->
			if (!overlay) {
				CommandManager.processIncomingChat(message.string)
			}
		}

		logger.info("GibCoins loaded successfully")
	}
}
