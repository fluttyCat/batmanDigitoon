package com.core.dto

import com.core.R


enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}



data class NetworkState(
    val status: Status,
    val tag: String? = null,
    val event: Int = R.string.forbid,
    val msg: String? = null,
    val code: Int? = null,
    val errorBody: ErrorResultDto? = null
    //val error: ResultDto? = null
) {

    companion object {
        fun loaded(tag: String? = null) = NetworkState(Status.SUCCESS, tag = tag)
        fun loading(tag: String? = null) = NetworkState(Status.RUNNING, tag = tag)

        fun error(
            event: Int = R.string.forbid,
            tag: String? = null,
            msg: String? = null,
            code: Int? = null,
            errorBody: ErrorResultDto? = null
            //error: ResultDto? = null
        ) =
            NetworkState(
                status = Status.FAILED,
                event = event,
                msg = msg,
                tag = tag,
                code = code,
                errorBody = errorBody

            )


    }
}