package impl.`fun`.partycommands

import clickgui.GibCoinsConfig

object PartyCommandSettings {
    private var enabled = false
    private var ownerOnlyEnabled = false
    private var forcefemEnabled = true
    private var gambleKickEnabled = true
    private var funFactEnabled = true
    private var shitterCheckEnabled = true
    private var killEnabled = true

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    @JvmStatic
    fun setEnabled(value: Boolean) {
        enabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleEnabled(): Boolean {
        enabled = !enabled
        GibCoinsConfig.save()
        return enabled
    }

    @JvmStatic
    fun isOwnerOnlyEnabled(): Boolean = ownerOnlyEnabled

    @JvmStatic
    fun setOwnerOnlyEnabled(value: Boolean) {
        ownerOnlyEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleOwnerOnly(): Boolean {
        ownerOnlyEnabled = !ownerOnlyEnabled
        GibCoinsConfig.save()
        return ownerOnlyEnabled
    }

    @JvmStatic
    fun isForcefemEnabled(): Boolean = forcefemEnabled

    @JvmStatic
    fun setForcefemEnabled(value: Boolean) {
        forcefemEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleForcefem(): Boolean {
        forcefemEnabled = !forcefemEnabled
        GibCoinsConfig.save()
        return forcefemEnabled
    }

    @JvmStatic
    fun isGambleKickEnabled(): Boolean = gambleKickEnabled

    @JvmStatic
    fun setGambleKickEnabled(value: Boolean) {
        gambleKickEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleGambleKick(): Boolean {
        gambleKickEnabled = !gambleKickEnabled
        GibCoinsConfig.save()
        return gambleKickEnabled
    }

    @JvmStatic
    fun isFunFactEnabled(): Boolean = funFactEnabled

    @JvmStatic
    fun setFunFactEnabled(value: Boolean) {
        funFactEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleFunFact(): Boolean {
        funFactEnabled = !funFactEnabled
        GibCoinsConfig.save()
        return funFactEnabled
    }

    @JvmStatic
    fun isShitterCheckEnabled(): Boolean = shitterCheckEnabled

    @JvmStatic
    fun setShitterCheckEnabled(value: Boolean) {
        shitterCheckEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleShitterCheck(): Boolean {
        shitterCheckEnabled = !shitterCheckEnabled
        GibCoinsConfig.save()
        return shitterCheckEnabled
    }

    @JvmStatic
    fun isKillEnabled(): Boolean = killEnabled

    @JvmStatic
    fun setKillEnabled(value: Boolean) {
        killEnabled = value
        GibCoinsConfig.save()
    }

    @JvmStatic
    fun toggleKill(): Boolean {
        killEnabled = !killEnabled
        GibCoinsConfig.save()
        return killEnabled
    }
}
