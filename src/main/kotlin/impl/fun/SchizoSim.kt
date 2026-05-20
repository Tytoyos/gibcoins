package impl.`fun`

import clickgui.GibCoinsConfig

object SchizoSim {
    private var enabled = true

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