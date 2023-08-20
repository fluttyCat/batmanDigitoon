package com.core.dto

import androidx.annotation.Keep

//@JsonClass(generateAdapter = true)
data class ErrorResultDto(

    @Keep var data: Data,
    /*@Keep var access_token: String,
    @Keep var token_type: String,
    @Keep var token_expires_in: Int,*/
    @Keep var statusCode: Int?
)