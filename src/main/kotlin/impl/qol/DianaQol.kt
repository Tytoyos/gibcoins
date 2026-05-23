package impl.qol

import clickgui.GibCoinsConfig
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks

object DianaQol {
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
        return enabled && (
            state.isOf(Blocks.SHORT_GRASS) ||
                state.isOf(Blocks.TALL_GRASS) ||
                state.isOf(Blocks.FERN) ||
                state.isOf(Blocks.LARGE_FERN) ||
                state.isOf(Blocks.DEAD_BUSH)
            )
    }
}
