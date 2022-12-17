package com.fikri.netplix.core.domain.repository_interfaces

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.MovieVideo

interface IVideoRepository {
    suspend fun getVideoTrailer(apiKey: String, movieId: Int): Resource<MovieVideo>
}