package impl.qol

import clickgui.GibCoinsConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.client.render.entity.state.PlayerEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.PlayerLikeEntity
import net.minecraft.util.math.Vec3d
import java.util.UUID
import kotlin.math.round

object PlayerSize {
    private const val DEFAULT_SCALE_TENTHS = 10
    private const val MIN_SCALE_TENTHS = 1
    private const val MIN_Y_SCALE_TENTHS = -30
    private const val MAX_SCALE_TENTHS = 30
    private const val SCALE_STEP = 0.1
    private const val PLAYER_MODEL_RENDER_SCALE = 0.9375
    private const val PLAYER_VALIDATION_DELAY_MS = 1250L

    private var xScaleTenths = DEFAULT_SCALE_TENTHS
    private var yScaleTenths = DEFAULT_SCALE_TENTHS
    private var zScaleTenths = DEFAULT_SCALE_TENTHS
    private var enabled = false
    private var scaleAllPlayers = true
    private val remotePlayerValidationSince = mutableMapOf<UUID, Long>()

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
    fun toggleScaleAllPlayers(): Boolean {
        scaleAllPlayers = !scaleAllPlayers
        GibCoinsConfig.save()
        return scaleAllPlayers
    }

    @JvmStatic
    fun setScaleAllPlayers(value: Boolean) {
        scaleAllPlayers = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isScaleAllPlayersEnabled(): Boolean = scaleAllPlayers

    @JvmStatic
    fun setXScale(value: Double) {
        xScaleTenths = toScaleTenths(value)
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun getXScale(): Double = xScaleTenths * SCALE_STEP

    @JvmStatic
    fun setYScale(value: Double) {
        yScaleTenths = toScaleTenths(value, MIN_Y_SCALE_TENTHS)
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun getYScale(): Double = yScaleTenths * SCALE_STEP

    @JvmStatic
    fun setZScale(value: Double) {
        zScaleTenths = toScaleTenths(value)
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun getZScale(): Double = zScaleTenths * SCALE_STEP

    @JvmStatic
    fun getMinScale(): Double = MIN_SCALE_TENTHS * SCALE_STEP

    @JvmStatic
    fun getMinYScale(): Double = MIN_Y_SCALE_TENTHS * SCALE_STEP

    @JvmStatic
    fun getMaxScale(): Double = MAX_SCALE_TENTHS * SCALE_STEP

    @JvmStatic
    fun getScaleStep(): Double = SCALE_STEP

    @JvmStatic
    fun applyScale(renderState: PlayerEntityRenderState, matrices: MatrixStack) {
        if (!enabled || isDefaultScale()) {
            return
        }

        if (yScaleTenths < 0) {
            matrices.translate(0.0, -renderState.height / PLAYER_MODEL_RENDER_SCALE, 0.0)
        }
        matrices.scale(getXScale().toFloat(), getYScale().toFloat(), getZScale().toFloat())
    }

    @JvmStatic
    fun pullNameLabelWithYScale(renderState: PlayerEntityRenderState, shouldScale: Boolean) {
        if (!shouldScale || yScaleTenths <= 0 || yScaleTenths == DEFAULT_SCALE_TENTHS) {
            return
        }

        val labelPos = renderState.nameLabelPos ?: return
        renderState.nameLabelPos = Vec3d(labelPos.x, labelPos.y * getYScale(), labelPos.z)
    }

    @JvmStatic
    fun shouldScale(renderedPlayer: PlayerLikeEntity): Boolean {
        if (!enabled) {
            return false
        }

        val localPlayer = MinecraftClient.getInstance().player ?: return false
        if (renderedPlayer == localPlayer) {
            return true
        }
        if (!scaleAllPlayers) {
            return false
        }

        return isRealRemotePlayer(renderedPlayer)
    }

    private fun isDefaultScale(): Boolean {
        return xScaleTenths == DEFAULT_SCALE_TENTHS &&
            yScaleTenths == DEFAULT_SCALE_TENTHS &&
            zScaleTenths == DEFAULT_SCALE_TENTHS
    }

    private fun toScaleTenths(value: Double, minScaleTenths: Int = MIN_SCALE_TENTHS): Int {
        if (!value.isFinite()) {
            return DEFAULT_SCALE_TENTHS
        }

        return round(value / SCALE_STEP).toInt().coerceIn(minScaleTenths, MAX_SCALE_TENTHS)
    }

    private fun isRealRemotePlayer(entity: PlayerLikeEntity): Boolean {
        val player = entity as? OtherClientPlayerEntity ?: return false
        return npcCheck(player)
    }

    private fun npcCheck(otherPlayer: OtherClientPlayerEntity): Boolean {
        val networkHandler = MinecraftClient.getInstance().networkHandler ?: return false
        val hasTabEntry = networkHandler.getPlayerListEntry(otherPlayer.uuid) != null
        if (!hasTabEntry) {
            remotePlayerValidationSince.remove(otherPlayer.uuid)
            return false
        }

        val now = System.currentTimeMillis()
        val firstSeenAt = remotePlayerValidationSince.putIfAbsent(otherPlayer.uuid, now) ?: now
        return now - firstSeenAt >= PLAYER_VALIDATION_DELAY_MS
    }
}
