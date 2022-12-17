package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.domain.repository_interfaces.IGenreRepository

class GenreInteractor(private val genreRepository: IGenreRepository) : GenreUseCase {
    override suspend fun getAllGenre(apiKey: String) = genreRepository.getAllGenre(apiKey)
}