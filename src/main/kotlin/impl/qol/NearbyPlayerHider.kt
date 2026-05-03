package impl.qol

object NearbyPlayerHider {
    private const val HIDE_DISTANCE = 1.5
    private const val HIDE_DISTANCE_SQUARED = HIDE_DISTANCE * HIDE_DISTANCE

    private var enabled = false

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
    fun getHideDistanceSquared(): Double = HIDE_DISTANCE_SQUARED
}
