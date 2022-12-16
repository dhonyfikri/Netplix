package com.fikri.netplix.core.domain.repository_interfaces

import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Movie

interface IMovieRepository {
    suspend fun getLatestMovieList(apiKey: String): Resource<Movie>
//    fun getDetailMovie(apiKey: String, movieId: Int): Flow<Resource<MovieDetail>>
//    fun getMovieByGenre(apiKey: String, genreId: String): LiveData<PagingData<Movie>>
}