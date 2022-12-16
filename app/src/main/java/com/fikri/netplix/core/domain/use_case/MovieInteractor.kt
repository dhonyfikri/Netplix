package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.domain.repository_interfaces.IMovieRepository

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override suspend fun getLatestMovieList(apiKey: String) =
        movieRepository.getLatestMovieList(apiKey)
}