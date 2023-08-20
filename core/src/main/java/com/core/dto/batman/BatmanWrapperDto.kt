package com.core.dto.batman

import com.google.gson.annotations.SerializedName

data class BatmanWrapperDto(
    @SerializedName("Search") var search: List<BatmanDto>? = emptyList(),
    @SerializedName("totalResults") var totalResults: Int? = null,
    @SerializedName("Response") var response: Boolean? = null
)