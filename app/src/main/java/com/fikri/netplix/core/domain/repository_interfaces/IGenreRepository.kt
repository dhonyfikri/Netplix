package com.fikri.netplix.core.domain.repository_interfaces

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Genre

interface IGenreRepository {
    suspend fun getAllGenre(apiKey: String): Resource<Genre>
}