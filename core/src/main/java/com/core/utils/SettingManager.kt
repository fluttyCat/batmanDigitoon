package com.core.utils

interface SettingManager {

    fun setRegister(isRegister: Boolean)
    fun isRegister(): Boolean

    fun setOnBoardingDisplayed(value: Boolean)
    fun isOnBoardingDisplayed(): Boolean


}

class SettingManagerImpl(private val preference: Preference) : SettingManager {

    companion object {
        private const val isRegistered = "isRegistered"
        private const val onBoardingDisplay = "onBoardingDisplay"
    }

    override fun setRegister(isRegister: Boolean) {
        preference.put(isRegistered, isRegister)
    }

    override fun isRegister(): Boolean = preference.getBoolean(isRegistered)

    override fun setOnBoardingDisplayed(value: Boolean) {
        preference.put(onBoardingDisplay, value)
    }

    override fun isOnBoardingDisplayed(): Boolean = preference.getBoolean(onBoardingDisplay)

}
