package impl.qol

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.util.Identifier
import kotlin.random.Random

object Overlay {
    private const val DISPLAY_DURATION_NANOS = 1_000_000_000L
    private var displayUntilNanos = 0L
    private var tickCounter = 0
    private var rollCD = 240000

    fun register() {
        HudElementRegistry.attachElementAfter(
            VanillaHudElements.SUBTITLES,
            Identifier.of("gibcoins", "overlay")
        ) { context, _ ->
            if (System.nanoTime() < displayUntilNanos) {
                val screenWidth = context.scaledWindowWidth
                val screenHeight = context.scaledWindowHeight

                val textureWidth = 3440f
                val textureHeight = 1440f

                val scale = maxOf(
                    screenWidth / textureWidth,
                    screenHeight / textureHeight
                )

                val drawWidth = textureWidth * scale
                val drawHeight = textureHeight * scale

                val x = ((screenWidth - drawWidth) / 2f).toInt()
                val y = ((screenHeight - drawHeight) / 2f).toInt()

                context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    Identifier.of("gibcoins", "textures/gui/importantoverlay.png"),
                    x,
                    y,
                    0f,
                    0f,
                    drawWidth.toInt(),
                    drawHeight.toInt(),
                    textureWidth.toInt(),
                    textureHeight.toInt(),
                    textureWidth.toInt(),
                    textureHeight.toInt()
                )
            }
        }

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (client.player == null) return@register

            tickCounter++

            if (tickCounter >= rollCD) {
                tickCounter = 0
                show()
            }
        }
    }

    fun show() {
        if (Random.nextDouble() < 1)
        displayUntilNanos = System.nanoTime() + DISPLAY_DURATION_NANOS
    }
}
