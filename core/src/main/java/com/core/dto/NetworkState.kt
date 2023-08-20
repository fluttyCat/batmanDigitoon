package com.core.dto

import com.core.R


enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

enum class ErrorType(val value: Int) {

    //api error type
    EOFException(R.string.eofException),
    IOException(R.string.ioException),
    SQLiteException(R.string.sqLiteException),
    InternetConnection(0),
    Authorization(R.string.authorization),
    Forbidden(R.string.forbidden),
    HttpException(0),
    FileNotFound(0),
    JsonSyntaxException(R.string.jsonFormat),
    TimeCheck(0),
    UnProcessable(0),
    NullPointException(0),

    Undefine(0)
}

data class NetworkState(
    val status: Status,
    val tag: String? = null,
    val event: Int = R.string.forbid,
    val type: ErrorType   = ErrorType.Undefine,
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

        public fun error(event: ErrorType, tag: String, msg: String = "") = NetworkState(status = Status.FAILED, type = event, msg = msg, tag = tag)

    }
}