package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.domain.repository_interfaces.IMovieRepository

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override suspend fun getPopularMovieList(apiKey: String) =
        movieRepository.getPopularMovieList(apiKey)

    override suspend fun getLatestMovieList(apiKey: String) =
        movieRepository.getLatestMovieList(apiKey)

    override suspend fun getLimitedMovieListByGenre(
        apiKey: String,
        genreId: String
    ) = movieRepository.getLimitedMovieListByGenre(apiKey, genreId)

    override suspend fun getDetailMovie(apiKey: String, movieId: Int) =
        movieRepository.getDetailMovie(apiKey, movieId)

    override suspend fun searchMovie(apiKey: String, query: String) =
        movieRepository.searchMovie(apiKey, query)

    override fun getEndlessMovieByGenre(
        apiKey: String,
        genreId: String
    ) = movieRepository.getEndlessMovieByGenre(apiKey, genreId)
}