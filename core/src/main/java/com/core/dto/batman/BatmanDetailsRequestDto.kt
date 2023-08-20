package com.core.dto.batman

import androidx.annotation.Keep

@Keep
data class BatmanDetailsRequestDto(
    @Keep var apikey: String,
    @Keep var i: String
)