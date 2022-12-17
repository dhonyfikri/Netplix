package com.fikri.netplix.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.network.Token
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.domain.model.MovieDetail
import com.fikri.netplix.core.domain.model.MovieVideo
import com.fikri.netplix.core.domain.use_case.MovieUseCase
import com.fikri.netplix.core.domain.use_case.VideoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchMovieViewModel(
    private val movieUseCase: MovieUseCase,
    private val videoUseCase: VideoUseCase
) : ViewModel() {
    private val _listMovieSearchResult = MutableLiveData<ArrayList<Movie>>()
    val listMovieSearchResult: LiveData<ArrayList<Movie>> = _listMovieSearchResult
    private val _isShowingDetailMovie = MutableLiveData<Boolean>()
    val isShowingDetailMovie: LiveData<Boolean> = _isShowingDetailMovie
    private val _isShowingShimmer = MutableLiveData(false)
    val isShowingShimmer: LiveData<Boolean> = _isShowingShimmer
    private val _isShowingLoadingModal = MutableLiveData<Boolean>()
    val isShowingLoadingModal: LiveData<Boolean> = _isShowingLoadingModal

    lateinit var movieDetail: MovieDetail
    var movieVideo: MovieVideo? = null

    fun searchMovie(query: String) {
        _isShowingShimmer.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = movieUseCase.searchMovie(Token.TMDB_TOKEN_V3, query)
                if (result is Resource.Success) {
                    _listMovieSearchResult.postValue(result.data as ArrayList<Movie>)
                }
                _isShowingShimmer.postValue(false)
            }
        }
    }

    fun getDetailMovie(movieId: Int) {
        _isShowingLoadingModal.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val movieDetailResult = movieUseCase.getDetailMovie(Token.TMDB_TOKEN_V3, movieId)
                val movieVideoResult = videoUseCase.getVideoTrailer(Token.TMDB_TOKEN_V3, movieId)

                if (movieDetailResult is Resource.Success && movieVideoResult is Resource.Success) {
                    movieDetail = movieDetailResult.data[0]
                    if (movieVideoResult.data.isNotEmpty()) {
                        movieVideo = movieVideoResult.data[0]
                    }
                    _isShowingDetailMovie.postValue(true)
                }
                _isShowingLoadingModal.postValue(false)
            }
        }
    }

    fun dismissDetailMovie() {
        _isShowingDetailMovie.value = false
    }
}