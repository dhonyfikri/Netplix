package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Genre

interface GenreUseCase {
    suspend fun getAllGenre(apiKey: String): Resource<Genre>
}