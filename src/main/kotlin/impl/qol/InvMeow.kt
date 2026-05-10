package impl.qol

import clickgui.GibCoinsConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvents

object InvMeow {
    private val triggers = setOf(
        "Your \u269A Bonzo's Mask saved your life!",
        "Your Bonzo's Mask saved your life!",
        "Second Wind Activated! Your Spirit Mask saved your life!",
        "Your Phoenix Pet saved you from certain death!"
    )
    private val sounds = listOf(
        SoundEvents.ENTITY_CAT_PURREOW,
        SoundEvents.ENTITY_CAT_BEG_FOR_FOOD,
        SoundEvents.ENTITY_CAT_AMBIENT
    )

    private var enabled = false

    fun toggleEnabled(): Boolean {
        enabled = !enabled
        GibCoinsConfig.save()
        return enabled
    }

    fun setEnabled(value: Boolean) {
        enabled = value
        GibCoinsConfig.save()
    }

    fun isEnabled(): Boolean = enabled

    fun meow(plainText: String) {
        if (!enabled || plainText.trim() !in triggers) {
            return
        }

        MinecraftClient.getInstance().player?.playSound(sounds.random(), 1.3F, 1.0F)
    }
}