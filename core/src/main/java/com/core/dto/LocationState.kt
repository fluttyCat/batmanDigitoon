package ir.co.rayanpay.common.dto


enum class StateL {
    FAILED,
    LOADING,
    LOADED
}

data class LocationState constructor(
    val status  : StateL,
    val msg     : String? = null) {

    companion object {
        public val LOADED  = LocationState(StateL.LOADED)
        public val LOADING = LocationState(StateL.LOADING)
        public fun error(msg: String?) = LocationState(status = StateL.FAILED, msg = msg)
    }
}