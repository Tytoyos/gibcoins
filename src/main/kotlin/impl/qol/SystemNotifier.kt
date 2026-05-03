package impl.qol

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import utils.modMessage
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import kotlin.random.Random

object SystemNotifier {
    private const val ROLL_CD = 24000 // 20 minutes at 20 TPS.
    private const val SAVE_INTERVAL_TICKS = 20

    private val stateDir: Path = FabricLoader.getInstance().configDir.resolve("gibcoins")

    private var tickCounter = 0
    private var loadedUserKey: String? = null
    private var ticksSinceLastSave = 0

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (client.player == null) return@register

            ensureStateLoaded(client)

            tickCounter++
            ticksSinceLastSave++

            if (tickCounter >= ROLL_CD) {
                tickCounter = 0
                roll()
            }

            if (ticksSinceLastSave >= SAVE_INTERVAL_TICKS) {
                saveState()
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            saveState()
        }
    }

    fun roll() {
        val name = listOf("§c[§6ዞ§c] Benjamin Netanyahu","§c[§6ዞ§c] Donald Trump", "§c[§6ዞ§c] Jeffrey Epstein", "§c[§6ዞ§c] Hypixel", "§b[MVP§a+§b] Cata50GodLovesM7")
        val playerName = MinecraftClient.getInstance().session.username
        val message = listOf("nice weather.", "feet :drool:", "$playerName seems kinda gay...", "yea you're getting banned, $playerName.","$playerName could use some estrogen.")


        if (Random.nextDouble() < 0.5) {
            modMessage("${name.random()}§f: ${message.random()}","")
        }
    }

    private fun ensureStateLoaded(client: MinecraftClient) {
        val userKey = sanitizeUserKey(client.session.username)

        if (loadedUserKey == userKey) {
            return
        }

        saveState()
        loadState(userKey)
    }

    private fun loadState(userKey: String) {
        loadedUserKey = userKey
        tickCounter = 0
        ticksSinceLastSave = 0

        val stateFile = stateFileFor(userKey)
        if (!Files.exists(stateFile)) {
            return
        }

        val properties = Properties()
        Files.newInputStream(stateFile).use(properties::load)
        tickCounter = properties.getProperty("tickCounter")?.toIntOrNull()?.coerceAtLeast(0) ?: 0
    }

    private fun saveState() {
        val userKey = loadedUserKey ?: return

        Files.createDirectories(stateDir)

        val properties = Properties().apply {
            setProperty("tickCounter", tickCounter.toString())
        }

        Files.newOutputStream(stateFileFor(userKey)).use { output ->
            properties.store(output, "GibCoins per-user state")
        }

        ticksSinceLastSave = 0
    }

    private fun stateFileFor(userKey: String): Path = stateDir.resolve("$userKey.properties")

    private fun sanitizeUserKey(username: String): String {
        return username.replace(Regex("[^A-Za-z0-9._-]"), "_")
    }
}
