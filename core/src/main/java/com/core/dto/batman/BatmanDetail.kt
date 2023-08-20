package com.core.dto.batman

data class BatmanDetail(
    var title: String? = null,
    var year: Int? = null,
    var rated: String? = null,
    var released: String? = null,
    var runtime: String? = null,
    var genre: String? = null,
    var director: String? = null,
    var writer: String? = null,
    var actors: String? = null,
    var plot: String? = null,
    var language: String? = null,
    var country: String? = null,
    var awards: String? = null,
    var poster: String? = null,
    var ratings: String? = null,
    //var ratings: List<BatmanRatingsDto>? = emptyList(),
    var metascore: Int? = null,
    var imdbRating: Double? = null,
    var imdbVotes: String? = null,
    var imdbID: String? = null,
    var type: String? = null,
    var dVD: String? = null,
    var boxOffice: String? = null,
    var production: String? = null,
    var website: String? = null,
    var response: Boolean? = null
)