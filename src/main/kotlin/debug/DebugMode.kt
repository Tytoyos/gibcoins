package debug

import clickgui.GibCoinsConfig

object DebugMode {
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
}