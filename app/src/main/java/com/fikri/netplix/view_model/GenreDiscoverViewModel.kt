package com.fikri.netplix.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.network.Token
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.MovieDetail
import com.fikri.netplix.core.domain.model.MovieVideo
import com.fikri.netplix.core.domain.use_case.MovieUseCase
import com.fikri.netplix.core.domain.use_case.VideoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenreDiscoverViewModel(
    private val movieUseCase: MovieUseCase,
    private val videoUseCase: VideoUseCase
) : ViewModel() {
    private val _isShowingDetailMovie = MutableLiveData<Boolean>()
    val isShowingDetailMovie: LiveData<Boolean> = _isShowingDetailMovie
    private val _isShowingShimmer = MutableLiveData(false)
    val isShowingShimmer: LiveData<Boolean> = _isShowingShimmer
    private val _isShowingLoadingModal = MutableLiveData<Boolean>()
    val isShowingLoadingModal: LiveData<Boolean> = _isShowingLoadingModal

    var selectedGenre: Genre? = null
    lateinit var movieDetail: MovieDetail
    var movieVideo: MovieVideo? = null

    fun getEndlessMovieByGenre(apiKey: String) =
        movieUseCase.getEndlessMovieByGenre(apiKey, selectedGenre?.id.toString())
            .cachedIn(viewModelScope)

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