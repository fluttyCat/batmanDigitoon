package com.core.dto

import com.google.gson.annotations.SerializedName

data class ApiResultDto<T, E>(

    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: T? = null,

    @SerializedName("errors")
    var errors: E? = null,

    /*  @SerializedName("status")
      var statusCode: Int? = null
  */
)