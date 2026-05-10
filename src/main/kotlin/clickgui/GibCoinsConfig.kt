package clickgui

import impl.qol.NearbyPlayerHider
import impl.qol.Overlay
import impl.qol.SchizoSim
import impl.qol.SystemNotifier
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
        NearbyPlayerHider.setHideDistance(
            properties.getProperty("playerHider.hideDistance")?.toDoubleOrNull() ?: NearbyPlayerHider.getHideDistance()
        )

        Overlay.setEnabled(properties.getProperty("overlay.enabled")?.toBooleanStrictOrNull() ?: Overlay.isEnabled())
        SchizoSim.setEnabled(properties.getProperty("schizoSim.enabled")?.toBooleanStrictOrNull() ?: SchizoSim.isEnabled())
        SystemNotifier.setEnabled(
            properties.getProperty("systemNotifier.enabled")?.toBooleanStrictOrNull() ?: SystemNotifier.isEnabled()
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
            setProperty("playerHider.hideDistance", NearbyPlayerHider.getHideDistance().toString())
            setProperty("overlay.enabled", Overlay.isEnabled().toString())
            setProperty("schizoSim.enabled", SchizoSim.isEnabled().toString())
            setProperty("systemNotifier.enabled", SystemNotifier.isEnabled().toString())
        }

        Files.newOutputStream(configFile).use { output ->
            properties.store(output, "GibCoins feature config")
        }
    }
}
