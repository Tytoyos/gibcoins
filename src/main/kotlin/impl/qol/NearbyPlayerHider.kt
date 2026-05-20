package impl.qol

import clickgui.GibCoinsConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.PlayerLikeEntity
import net.minecraft.entity.player.PlayerEntity
import java.util.UUID

object NearbyPlayerHider {
    private const val DEFAULT_HIDE_DISTANCE = 1.5
    private const val MIN_HIDE_DISTANCE = 0.5
    private const val MAX_HIDE_DISTANCE = 10.0
    private const val DEFAULT_GHOST_OPACITY = 15.0
    private const val MIN_GHOST_OPACITY = 0.0
    private const val MAX_GHOST_OPACITY = 100.0
    private const val PLAYER_VALIDATION_DELAY_MS = 1250L

    private var enabled = false
    private var renderHidingEnabled = false
    private var hideAllEnabled = false
    private var ghostModeEnabled = false
    private var clickThroughEnabled = false
    private var hideDistance = DEFAULT_HIDE_DISTANCE
    private var ghostOpacity = DEFAULT_GHOST_OPACITY
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
    fun toggleRenderHiding(): Boolean {
        renderHidingEnabled = !renderHidingEnabled
        GibCoinsConfig.save()
        return renderHidingEnabled
    }

    @JvmStatic
    fun setRenderHidingEnabled(value: Boolean) {
        renderHidingEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isRenderHidingEnabled(): Boolean = renderHidingEnabled

    @JvmStatic
    fun toggleHideAll(): Boolean {
        hideAllEnabled = !hideAllEnabled
        GibCoinsConfig.save()
        return hideAllEnabled
    }

    @JvmStatic
    fun setHideAllEnabled(value: Boolean) {
        hideAllEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isHideAllEnabled(): Boolean = hideAllEnabled

    @JvmStatic
    fun toggleGhostMode(): Boolean {
        ghostModeEnabled = !ghostModeEnabled
        GibCoinsConfig.save()
        return ghostModeEnabled
    }

    @JvmStatic
    fun setGhostModeEnabled(value: Boolean) {
        ghostModeEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isGhostModeEnabled(): Boolean = ghostModeEnabled

    @JvmStatic
    fun setGhostOpacity(value: Double) {
        ghostOpacity = value.coerceIn(MIN_GHOST_OPACITY, MAX_GHOST_OPACITY)
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun getGhostOpacity(): Double = ghostOpacity

    @JvmStatic
    fun getMinGhostOpacity(): Double = MIN_GHOST_OPACITY

    @JvmStatic
    fun getMaxGhostOpacity(): Double = MAX_GHOST_OPACITY

    @JvmStatic
    fun getGhostOpacityAlpha(): Int = ((ghostOpacity / 100.0) * 255.0).toInt().coerceIn(0, 255)

    @JvmStatic
    fun toggleClickThrough(): Boolean {
        clickThroughEnabled = !clickThroughEnabled
        GibCoinsConfig.save()
        return clickThroughEnabled
    }

    @JvmStatic
    fun setClickThroughEnabled(value: Boolean) {
        clickThroughEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun isClickThroughEnabled(): Boolean = clickThroughEnabled

    @JvmStatic
    fun setHideDistance(value: Double) {
        hideDistance = value.coerceIn(MIN_HIDE_DISTANCE, MAX_HIDE_DISTANCE)
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun getHideDistance(): Double = hideDistance

    @JvmStatic
    fun getHideDistanceSquared(): Double = hideDistance * hideDistance

    @JvmStatic
    fun getMinHideDistance(): Double = MIN_HIDE_DISTANCE

    @JvmStatic
    fun getMaxHideDistance(): Double = MAX_HIDE_DISTANCE

    @JvmStatic
    fun shouldAffectPlayer(localPlayer: PlayerEntity?, otherPlayer: PlayerLikeEntity, x: Double, y: Double, z: Double): Boolean {
        if (
            !enabled ||
            localPlayer == null ||
            otherPlayer == localPlayer ||
            !isRealRemotePlayer(otherPlayer)
        ) {
            return false
        }

        return hideAllEnabled || localPlayer.squaredDistanceTo(x, y, z) <= getHideDistanceSquared()
    }

    @JvmStatic
    fun npcCheck(otherPlayer: PlayerLikeEntity): Boolean {
        if (otherPlayer !is OtherClientPlayerEntity) {
            return false
        }

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

    @JvmStatic
    fun isRealRemotePlayer(entity: Entity?): Boolean {
        val player = entity as? OtherClientPlayerEntity ?: return false
        return npcCheck(player)
    }

    @JvmStatic
    fun shouldHide(localPlayer: PlayerEntity?, otherPlayer: PlayerLikeEntity, x: Double, y: Double, z: Double): Boolean {
        if (!renderHidingEnabled) {
            return false
        }

        return shouldAffectPlayer(localPlayer, otherPlayer, x, y, z)
    }

    @JvmStatic
    fun shouldRenderAsGhost(localPlayer: PlayerEntity?, otherPlayer: PlayerLikeEntity, x: Double, y: Double, z: Double): Boolean {
        return ghostModeEnabled && shouldHide(localPlayer, otherPlayer, x, y, z)
    }

    @JvmStatic
    fun shouldClickThrough(localPlayer: PlayerEntity?, entity: Entity?): Boolean {
        val targetPlayer = entity as? PlayerLikeEntity ?: return false

        if (
            !enabled ||
            !clickThroughEnabled ||
            localPlayer == null ||
            targetPlayer == localPlayer ||
            !npcCheck(targetPlayer)
        ) {
            return false
        }

        return shouldAffectPlayer(localPlayer, targetPlayer, targetPlayer.x, targetPlayer.y, targetPlayer.z)
    }
}
