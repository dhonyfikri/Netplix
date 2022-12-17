package com.fikri.netplix.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fikri.netplix.core.data.source.remote.network.ApiService
import com.fikri.netplix.core.domain.model.Movie

class MoviePagingSource(
    private val apiService: ApiService,
    private val apiKey: String,
    private val genreId: String
) : PagingSource<Int, Movie>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getEndlessListMovieByGenre(
                apiKey = apiKey,
                page = position,
                genreId = genreId
            ).results
            val responseData: ArrayList<Movie> = arrayListOf()
            response.forEach {
                responseData.add(
                    Movie(
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
                )
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}