package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.MovieVideo

interface VideoUseCase {
    suspend fun getVideoTrailer(apiKey: String, movieId: Int): Resource<MovieVideo>
}