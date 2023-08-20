package com.core.dto.batman

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Batman(
    @PrimaryKey(autoGenerate = false)
    var imdbID: String,

    var title: String? = null,
    var year: String? = null,
    var type: String? = null,
    var poster: String? = null
)