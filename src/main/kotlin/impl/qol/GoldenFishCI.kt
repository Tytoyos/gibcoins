package impl.qol

import clickgui.GibCoinsConfig
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand

object GoldenFishCI {
    private var enabled = false

    fun register() {
        UseEntityCallback.EVENT.register { player, world, hand, _, _ ->
            if (!world.isClient || !enabled || !isHoldingFishingRod(player, hand)) {
                return@register ActionResult.PASS
            }

            val client = MinecraftClient.getInstance()
            if (client.player != player) {
                return@register ActionResult.PASS
            }

            val useHand = rodHand(player, hand) ?: hand
            val result = client.interactionManager?.interactItem(player, useHand)
                ?: ActionResult.PASS

            if (result is ActionResult.Success && result.swingSource() == ActionResult.SwingSource.CLIENT) {
                player.swingHand(useHand)
            }

            ActionResult.FAIL
        }
    }

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

    private fun isHoldingFishingRod(player: PlayerEntity, interactionHand: Hand): Boolean {
        return player.getStackInHand(interactionHand).isOf(Items.FISHING_ROD) ||
            player.getStackInHand(Hand.MAIN_HAND).isOf(Items.FISHING_ROD) ||
            player.getStackInHand(Hand.OFF_HAND).isOf(Items.FISHING_ROD)
    }

    private fun rodHand(player: PlayerEntity, preferredHand: Hand): Hand? {
        if (player.getStackInHand(preferredHand).isOf(Items.FISHING_ROD)) {
            return preferredHand
        }

        return Hand.entries.firstOrNull { hand -> player.getStackInHand(hand).isOf(Items.FISHING_ROD) }
    }
}
