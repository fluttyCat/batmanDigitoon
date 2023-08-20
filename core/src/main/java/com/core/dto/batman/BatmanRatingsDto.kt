package com.core.dto.batman

import com.google.gson.annotations.SerializedName

data class BatmanRatingsDto(

    @SerializedName("Source") val source: String,
    @SerializedName("Value") val value: String
)
