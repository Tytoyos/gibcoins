package impl.qol

import clickgui.GibCoinsConfig
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks

object ScathaQol {
    private val fullBlockHitboxBlocks = setOf(
        Blocks.CHEST,
        Blocks.TRAPPED_CHEST,
        Blocks.ENDER_CHEST,
        Blocks.GLASS_PANE,
        Blocks.WHITE_STAINED_GLASS_PANE,
        Blocks.ORANGE_STAINED_GLASS_PANE,
        Blocks.MAGENTA_STAINED_GLASS_PANE,
        Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
        Blocks.YELLOW_STAINED_GLASS_PANE,
        Blocks.LIME_STAINED_GLASS_PANE,
        Blocks.PINK_STAINED_GLASS_PANE,
        Blocks.GRAY_STAINED_GLASS_PANE,
        Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
        Blocks.CYAN_STAINED_GLASS_PANE,
        Blocks.PURPLE_STAINED_GLASS_PANE,
        Blocks.BLUE_STAINED_GLASS_PANE,
        Blocks.BROWN_STAINED_GLASS_PANE,
        Blocks.GREEN_STAINED_GLASS_PANE,
        Blocks.RED_STAINED_GLASS_PANE,
        Blocks.BLACK_STAINED_GLASS_PANE
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
    fun shouldUseFullBlockHitbox(state: BlockState): Boolean {
        if (!enabled) {
            return false
        }

        return state.block in fullBlockHitboxBlocks
    }
}
