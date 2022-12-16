package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Movie

interface MovieUseCase {
    suspend fun getLatestMovieList(apiKey: String): Resource<Movie>
}