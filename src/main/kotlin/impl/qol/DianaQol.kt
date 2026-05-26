package impl.qol

import clickgui.GibCoinsConfig
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries

object DianaQol {
    private val ignoredBlockIds = setOf(
        "minecraft:short_grass",
        "minecraft:tall_grass",
        "minecraft:fern",
        "minecraft:large_fern",
        "minecraft:dead_bush",
        "minecraft:bush",
        "minecraft:red_tulip",
        "minecraft:azure_bluet",
        "minecraft:rose"
    )

    private var enabled = false

    @JvmStatic
    fun toggleEnabled(): Boolean {
        enabled = !enabled
        GibCoinsConfig.save()
        return enabled
    }

    @JvmStatic
    fun setEnabled(value: Boolean) {
        enabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    @JvmStatic
    fun shouldIgnore(state: BlockState): Boolean {
        if (!enabled) {
            return false
        }

        val id = Registries.BLOCK.getId(state.block).toString()
        return id in ignoredBlockIds
    }
}
