package com.fikri.netplix.core.domain.repository_interfaces

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.domain.model.MovieDetail

interface IMovieRepository {
    suspend fun getPopularMovieList(apiKey: String): Resource<Movie>
    suspend fun getLatestMovieList(apiKey: String): Resource<Movie>
    suspend fun getLimitedMovieListByGenre(apiKey: String, genreId: String): Resource<Movie>
    suspend fun getDetailMovie(apiKey: String, movieId: Int): Resource<MovieDetail>
    suspend fun searchMovie(apiKey: String, query: String): Resource<Movie>
    fun getEndlessMovieByGenre(apiKey: String, genreId: String): LiveData<PagingData<Movie>>
}