package com.fikri.netplix.core.domain.model

data class MovieDetail(
    var adult: Boolean? = null,
    var backdropPath: String? = null,
    var budget: Int? = null,
    var genres: ArrayList<Genre> = arrayListOf(),
    var homepage: String? = null,
    var id: Int? = null,
    var imdbId: String? = null,
    var originalLanguage: String? = null,
    var originalTitle: String? = null,
    var overview: String? = null,
    var popularity: Double? = null,
    var posterPath: String? = null,
    var productionCompanies: ArrayList<ProductionCompanies> = arrayListOf(),
    var releaseDate: String? = null,
    var revenue: Int? = null,
    var runtime: Int? = null,
    var status: String? = null,
    var tagline: String? = null,
    var title: String? = null,
    var video: Boolean? = null,
    var voteAverage: Double? = null,
    var voteCount: Int? = null
)