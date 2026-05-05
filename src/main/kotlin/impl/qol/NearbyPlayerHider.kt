package impl.qol

object NearbyPlayerHider {
    private const val DEFAULT_HIDE_DISTANCE = 1.5
    private const val MIN_HIDE_DISTANCE = 0.5
    private const val MAX_HIDE_DISTANCE = 6.0

    private var enabled = false
    private var hideDistance = DEFAULT_HIDE_DISTANCE

    @JvmStatic
    fun toggle(): Boolean {
        enabled = !enabled
        return enabled
    }

    @JvmStatic
    fun setEnabled(value: Boolean) {
        enabled = value
    }

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    @JvmStatic
    fun setHideDistance(value: Double) {
        hideDistance = value.coerceIn(MIN_HIDE_DISTANCE, MAX_HIDE_DISTANCE)
    }

    @JvmStatic
    fun getHideDistance(): Double = hideDistance

    @JvmStatic
    fun getHideDistanceSquared(): Double = hideDistance * hideDistance

    @JvmStatic
    fun getMinHideDistance(): Double = MIN_HIDE_DISTANCE

    @JvmStatic
    fun getMaxHideDistance(): Double = MAX_HIDE_DISTANCE
}
