package com.fikri.netplix.core.data

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.RemoteDataSource
import com.fikri.netplix.core.data.source.remote.response.ApiResultWrapper
import com.fikri.netplix.core.domain.model.MovieVideo
import com.fikri.netplix.core.domain.repository_interfaces.IVideoRepository

class VideoRepository(private val remoteDataSource: RemoteDataSource) : IVideoRepository {
    override suspend fun getVideoTrailer(apiKey: String, movieId: Int): Resource<MovieVideo> {
        when (val result = remoteDataSource.getVideoTrailer(apiKey, movieId)) {
            is ApiResultWrapper.Success -> {
                val listMovieVideo: ArrayList<MovieVideo> = arrayListOf()
                result.response.results.map {
                    if (it.type?.lowercase() == "trailer") {
                        listMovieVideo.add(
                            MovieVideo(
                                it.iso6391,
                                it.iso31661,
                                it.name,
                                it.key,
                                it.site,
                                it.size,
                                it.type,
                                it.official,
                                it.publishedAt,
                                it.id
                            )
                        )
                    }
                }
                return Resource.Success(listMovieVideo)
            }
            is ApiResultWrapper.Error -> {
                val code: Int? = result.code
                val failedType: String? = result.failedType
                val message: String? = result.message
                return Resource.Error(code, failedType, message)
            }
            is ApiResultWrapper.NetworkError -> {
                val failedType: String? = result.failedType
                val message: String? = result.message
                return Resource.NetworkError(failedType, message)
            }
        }
    }
}