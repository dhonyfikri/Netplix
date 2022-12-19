package com.fikri.netplix.core.data

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.RemoteDataSource
import com.fikri.netplix.core.data.source.remote.response.ApiResultWrapper
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.repository_interfaces.IGenreRepository

class GenreRepository(private val remoteDataSource: RemoteDataSource) : IGenreRepository {
    override suspend fun getAllGenre(apiKey: String): Resource<Genre> {
        when (val result = remoteDataSource.getAllGenre(apiKey)) {
            is ApiResultWrapper.Success -> {
                val listGenre: ArrayList<Genre> = arrayListOf()
                result.response.genres.forEach {
                    val genre = Genre(it.id, it.name)
                    listGenre.add(genre)
                }
                return Resource.Success(listGenre)
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