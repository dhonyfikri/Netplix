package com.fikri.netplix.core.data

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.RemoteDataSource
import com.fikri.netplix.core.data.source.remote.response.ApiResultWrapper
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.domain.repository_interfaces.IMovieRepository

class MovieRepository(private val remoteDataSource: RemoteDataSource) : IMovieRepository {
    override suspend fun getLatestMovieList(apiKey: String): Resource<Movie> {
        when (val result = remoteDataSource.getLatestMovieList(apiKey)) {
            is ApiResultWrapper.Success -> {
                val listMovie: ArrayList<Movie> = arrayListOf()
                result.response.results.forEach {
                    val movie = Movie(
                        it.adult,
                        it.backdropPath,
                        it.genreIds,
                        it.id,
                        it.originalLanguage,
                        it.originalTitle,
                        it.overview,
                        it.popularity,
                        it.posterPath,
                        it.releaseDate,
                        it.title,
                        it.video,
                        it.voteAverage,
                        it.voteCount
                    )
                    listMovie.add(movie)
                }
                return Resource.Success(listMovie)
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