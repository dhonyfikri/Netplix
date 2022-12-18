package com.fikri.netplix.core.data

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.RemoteDataSource
import com.fikri.netplix.core.data.source.remote.response.ApiResultWrapper
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.domain.model.MovieDetail
import com.fikri.netplix.core.domain.model.ProductionCompanies
import com.fikri.netplix.core.domain.repository_interfaces.IMovieRepository

class MovieRepository(private val remoteDataSource: RemoteDataSource) : IMovieRepository {
    override suspend fun getPopularMovieList(apiKey: String): Resource<Movie> {
        when (val result = remoteDataSource.getPopularMovieList(apiKey)) {
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

    override suspend fun getLimitedMovieListByGenre(
        apiKey: String,
        genreId: String
    ): Resource<Movie> {
        when (val result = remoteDataSource.getGenreMember(apiKey, genreId)) {
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

    override suspend fun getDetailMovie(apiKey: String, movieId: Int): Resource<MovieDetail> {
        when (val result = remoteDataSource.getDetailMovie(apiKey, movieId)) {
            is ApiResultWrapper.Success -> {
                val genres =
                    result.response.genres.map { Genre(it.id, it.name) } as ArrayList<Genre>
                val productionCompanies = result.response.productionCompanies.map {
                    ProductionCompanies(
                        it.id,
                        it.logoPath,
                        it.name,
                        it.originCountry
                    )
                } as ArrayList<ProductionCompanies>
                val movieDetail = MovieDetail(
                    result.response.adult,
                    result.response.backdropPath,
                    result.response.budget,
                    genres,
                    result.response.homepage,
                    result.response.id,
                    result.response.imdbId,
                    result.response.originalLanguage,
                    result.response.originalTitle,
                    result.response.overview,
                    result.response.popularity,
                    result.response.posterPath,
                    productionCompanies,
                    result.response.releaseDate,
                    result.response.revenue,
                    result.response.runtime,
                    result.response.status,
                    result.response.tagline,
                    result.response.title,
                    result.response.video,
                    result.response.voteAverage,
                    result.response.voteCount
                )
                return Resource.Success(listOf(movieDetail))
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

    override suspend fun searchMovie(apiKey: String, query: String): Resource<Movie> {
        when (val result = remoteDataSource.searchMovie(apiKey, query)) {
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

    override fun getEndlessMovieByGenre(apiKey: String, genreId: String) =
        remoteDataSource.getEndlessMovieByGenre(apiKey, genreId)
}