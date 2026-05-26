package com.github.tytoyos.gibcoins

import impl.`fun`.partycommands.CommandManager
import clickgui.ClickGuiCommand
import commands.FunFactCommand
import debug.DebugCommand
import commands.HelpCommand
import commands.HidePlayersCommand
import debug.OverlayTestCommand
import debug.RollCommand
import debug.TestPartyCommand
import clickgui.GibCoinsConfig
import debug.TestInvProc
import impl.qol.InvMeow
import impl.qol.SmartTermAC
import impl.`fun`.Overlay
import impl.`fun`.SystemNotifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import org.slf4j.LoggerFactory

object GibCoins : ClientModInitializer {
	private val logger = LoggerFactory.getLogger("gibcoins")

	fun handleIncomingChatLine(plainText: String) {
		CommandManager.processIncomingChat(plainText)
		InvMeow.meow(plainText)
	}

	override fun onInitializeClient() {
		GibCoinsConfig.load()

		Overlay.register()
		SystemNotifier.register()
		SmartTermAC.register()

		ClickGuiCommand().initialize()
		DebugCommand().initialize()
		FunFactCommand().initialize()
		HelpCommand().initialize()
		HidePlayersCommand().initialize()
		OverlayTestCommand().initialize()
		RollCommand().initialize()
		TestInvProc().initialize()
		TestPartyCommand().initialize()

		ClientReceiveMessageEvents.CHAT.register { message, _, _, _, _ ->
			handleIncomingChatLine(message.string)
		}

		ClientReceiveMessageEvents.GAME.register { message, overlay ->
			if (!overlay) {
				handleIncomingChatLine(message.string)
			}
		}

		logger.info("GibCoins loaded successfully")
	}
}
