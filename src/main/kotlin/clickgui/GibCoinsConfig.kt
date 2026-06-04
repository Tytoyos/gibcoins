package clickgui

import impl.qol.NearbyPlayerHider
import impl.qol.PlayerSize
import impl.qol.DianaQol
import impl.qol.GoldenFishCI
import impl.qol.LeapFrog
import debug.DebugMode
import impl.qol.InvMeow
import impl.qol.SmartTermAC
import impl.`fun`.partycommands.PartyCommandSettings
import impl.`fun`.Overlay
import impl.`fun`.SchizoSim
import impl.`fun`.SystemNotifier
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

object GibCoinsConfig {
    private val configDir: Path = FabricLoader.getInstance().configDir.resolve("gibcoins")
    private val configFile: Path = configDir.resolve("features.properties")

    fun load() {
        if (!Files.exists(configFile)) {
            return
        }

        val properties = Properties()
        Files.newInputStream(configFile).use(properties::load)

        NearbyPlayerHider.setEnabled(properties.getProperty("playerHider.enabled")?.toBooleanStrictOrNull() ?: NearbyPlayerHider.isEnabled())
        NearbyPlayerHider.setRenderHidingEnabled(
            properties.getProperty("playerHider.renderHidingEnabled")?.toBooleanStrictOrNull()
                ?: NearbyPlayerHider.isRenderHidingEnabled()
        )
        NearbyPlayerHider.setHideAllEnabled(
            properties.getProperty("playerHider.hideAllEnabled")?.toBooleanStrictOrNull()
                ?: NearbyPlayerHider.isHideAllEnabled()
        )
        NearbyPlayerHider.setGhostModeEnabled(
            properties.getProperty("playerHider.ghostModeEnabled")?.toBooleanStrictOrNull()
                ?: NearbyPlayerHider.isGhostModeEnabled()
        )
        NearbyPlayerHider.setClickThroughEnabled(
            properties.getProperty("playerHider.clickThroughEnabled")?.toBooleanStrictOrNull()
                ?: NearbyPlayerHider.isClickThroughEnabled()
        )
        NearbyPlayerHider.setGhostOpacity(
            properties.getProperty("playerHider.ghostOpacity")?.toDoubleOrNull() ?: NearbyPlayerHider.getGhostOpacity()
        )
        NearbyPlayerHider.setHideDistance(
            properties.getProperty("playerHider.hideDistance")?.toDoubleOrNull() ?: NearbyPlayerHider.getHideDistance()
        )

        Overlay.setEnabled(properties.getProperty("overlay.enabled")?.toBooleanStrictOrNull() ?: Overlay.isEnabled())
        DebugMode.setEnabled(
            properties.getProperty("debugMode.enabled")?.toBooleanStrictOrNull() ?: DebugMode.isEnabled()
        )
        InvMeow.setEnabled(properties.getProperty("invMeow.enabled")?.toBooleanStrictOrNull() ?: InvMeow.isEnabled())
        InvMeow.setVolume(properties.getProperty("invMeow.volume")?.toDoubleOrNull() ?: InvMeow.getVolume())
        PlayerSize.setEnabled(
            properties.getProperty("playerSize.enabled")?.toBooleanStrictOrNull() ?: PlayerSize.isEnabled()
        )
        PlayerSize.setScaleAllPlayers(
            properties.getProperty("playerSize.scaleAllPlayers")?.toBooleanStrictOrNull()
                ?: PlayerSize.isScaleAllPlayersEnabled()
        )
        PlayerSize.setXScale(properties.getProperty("playerSize.xScale")?.toDoubleOrNull() ?: PlayerSize.getXScale())
        PlayerSize.setYScale(properties.getProperty("playerSize.yScale")?.toDoubleOrNull() ?: PlayerSize.getYScale())
        PlayerSize.setZScale(properties.getProperty("playerSize.zScale")?.toDoubleOrNull() ?: PlayerSize.getZScale())
        DianaQol.setEnabled(
            properties.getProperty("Diana qol.enabled")?.toBooleanStrictOrNull() ?: DianaQol.isEnabled()
        )
        GoldenFishCI.setEnabled(
            properties.getProperty("goldenFishCI.enabled")?.toBooleanStrictOrNull() ?: GoldenFishCI.isEnabled()
        )
        LeapFrog.setEnabled(
            properties.getProperty("leapFrog.enabled")?.toBooleanStrictOrNull() ?: LeapFrog.isEnabled()
        )
        SmartTermAC.setEnabled(
            properties.getProperty("smartTermAC.enabled")?.toBooleanStrictOrNull() ?: SmartTermAC.isEnabled()
        )
        SchizoSim.setEnabled(properties.getProperty("schizoSim.enabled")?.toBooleanStrictOrNull() ?: SchizoSim.isEnabled())
        SystemNotifier.setEnabled(
            properties.getProperty("systemNotifier.enabled")?.toBooleanStrictOrNull() ?: SystemNotifier.isEnabled()
        )
        PartyCommandSettings.setEnabled(
            properties.getProperty("partyCommands.enabled")?.toBooleanStrictOrNull() ?: PartyCommandSettings.isEnabled()
        )
        PartyCommandSettings.setForcefemEnabled(
            properties.getProperty("partyCommands.forcefem.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isForcefemEnabled()
        )
        PartyCommandSettings.setOwnerOnlyEnabled(
            properties.getProperty("partyCommands.ownerOnly.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isOwnerOnlyEnabled()
        )
        PartyCommandSettings.setGambleKickEnabled(
            properties.getProperty("partyCommands.gamblekick.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isGambleKickEnabled()
        )
        PartyCommandSettings.setFunFactEnabled(
            properties.getProperty("partyCommands.funfact.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isFunFactEnabled()
        )
        PartyCommandSettings.setShitterCheckEnabled(
            properties.getProperty("partyCommands.shittercheck.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isShitterCheckEnabled()
        )
        PartyCommandSettings.setKillEnabled(
            properties.getProperty("partyCommands.kill.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isKillEnabled()
        )
        PartyCommandSettings.setDomEnabled(
            properties.getProperty("partyCommands.dom.enabled")?.toBooleanStrictOrNull()
                ?: PartyCommandSettings.isDomEnabled()
        )
        PartyCommandSettings.setBlacklist(
            properties.getProperty("partyCommands.blacklist") ?: PartyCommandSettings.getBlacklist()
        )
    }

    fun save() {
        Files.createDirectories(configDir)

        val properties = Properties().apply {
            setProperty("playerHider.enabled", NearbyPlayerHider.isEnabled().toString())
            setProperty("playerHider.renderHidingEnabled", NearbyPlayerHider.isRenderHidingEnabled().toString())
            setProperty("playerHider.hideAllEnabled", NearbyPlayerHider.isHideAllEnabled().toString())
            setProperty("playerHider.ghostModeEnabled", NearbyPlayerHider.isGhostModeEnabled().toString())
            setProperty("playerHider.clickThroughEnabled", NearbyPlayerHider.isClickThroughEnabled().toString())
            setProperty("playerHider.ghostOpacity", NearbyPlayerHider.getGhostOpacity().toString())
            setProperty("playerHider.hideDistance", NearbyPlayerHider.getHideDistance().toString())
            setProperty("overlay.enabled", Overlay.isEnabled().toString())
            setProperty("debugMode.enabled", DebugMode.isEnabled().toString())
            setProperty("invMeow.enabled", InvMeow.isEnabled().toString())
            setProperty("invMeow.volume", InvMeow.getVolume().toString())
            setProperty("playerSize.enabled", PlayerSize.isEnabled().toString())
            setProperty("playerSize.scaleAllPlayers", PlayerSize.isScaleAllPlayersEnabled().toString())
            setProperty("playerSize.xScale", PlayerSize.getXScale().toString())
            setProperty("playerSize.yScale", PlayerSize.getYScale().toString())
            setProperty("playerSize.zScale", PlayerSize.getZScale().toString())
            setProperty("Diana qol.enabled", DianaQol.isEnabled().toString())
            setProperty("goldenFishCI.enabled", GoldenFishCI.isEnabled().toString())
            setProperty("leapFrog.enabled", LeapFrog.isEnabled().toString())
            setProperty("smartTermAC.enabled", SmartTermAC.isEnabled().toString())
            setProperty("schizoSim.enabled", SchizoSim.isEnabled().toString())
            setProperty("systemNotifier.enabled", SystemNotifier.isEnabled().toString())
            setProperty("partyCommands.enabled", PartyCommandSettings.isEnabled().toString())
            setProperty("partyCommands.ownerOnly.enabled", PartyCommandSettings.isOwnerOnlyEnabled().toString())
            setProperty("partyCommands.forcefem.enabled", PartyCommandSettings.isForcefemEnabled().toString())
            setProperty("partyCommands.gamblekick.enabled", PartyCommandSettings.isGambleKickEnabled().toString())
            setProperty("partyCommands.funfact.enabled", PartyCommandSettings.isFunFactEnabled().toString())
            setProperty("partyCommands.shittercheck.enabled", PartyCommandSettings.isShitterCheckEnabled().toString())
            setProperty("partyCommands.kill.enabled", PartyCommandSettings.isKillEnabled().toString())
            setProperty("partyCommands.dom.enabled", PartyCommandSettings.isDomEnabled().toString())
            setProperty("partyCommands.blacklist", PartyCommandSettings.getBlacklist())
        }

        Files.newOutputStream(configFile).use { output ->
            properties.store(output, "GibCoins feature config")
        }
    }
}
