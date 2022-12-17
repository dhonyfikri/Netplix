package com.fikri.netplix.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.network.Token
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.domain.model.MovieDetail
import com.fikri.netplix.core.domain.model.MovieVideo
import com.fikri.netplix.core.domain.use_case.GenreUseCase
import com.fikri.netplix.core.domain.use_case.MovieUseCase
import com.fikri.netplix.core.domain.use_case.VideoUseCase
import com.fikri.netplix.core.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val movieUseCase: MovieUseCase,
    private val genreUseCase: GenreUseCase,
    private val videoUseCase: VideoUseCase
) : ViewModel() {
    private val _listLatestMovie = MutableLiveData<Resource<Movie>>()
    val listLatestMovie: LiveData<Resource<Movie>> = _listLatestMovie
    private val _listGenre = MutableLiveData<Event<Resource<Genre>>>()
    val listGenre: LiveData<Event<Resource<Genre>>> = _listGenre
    private val _listGenreWithMember = MutableLiveData<ArrayList<Genre>>()
    val listGenreWithMember: LiveData<ArrayList<Genre>> = _listGenreWithMember
    private val _isShowingDetailMovie = MutableLiveData<Boolean>()
    val isShowingDetailMovie: LiveData<Boolean> = _isShowingDetailMovie
    private val _isShowingLoadingModal = MutableLiveData<Boolean>()
    val isShowingLoadingModal: LiveData<Boolean> = _isShowingLoadingModal

    lateinit var movieDetail: MovieDetail
    var movieVideo: MovieVideo? = null

    var scrollviewPositionY = 0

    init {
        getLatestMovieList()
        getAllGenre()
    }

    fun getLatestMovieList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = movieUseCase.getLatestMovieList(Token.TMDB_TOKEN_V3)
                _listLatestMovie.postValue(result)
            }
        }
    }

    fun getAllGenre() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = genreUseCase.getAllGenre(Token.TMDB_TOKEN_V3)
                _listGenre.postValue(Event(result))
            }
        }
    }

    fun getGenresMembers(listGenre: ArrayList<Genre>) {
        val tempListGenreWithMember = listGenre
        listGenre.mapIndexed { index, it ->

            Log.d("idnya", it.id.toString())
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val result = movieUseCase.getLimitedMovieListByGenre(
                        Token.TMDB_TOKEN_V3,
                        it.id.toString()
                    )
                    if (result is Resource.Success) {
                        tempListGenreWithMember[index].member = result.data as ArrayList<Movie>
                        _listGenreWithMember.postValue(tempListGenreWithMember)
                    }
                }
            }
        }
    }

    fun getDetailMoview(movieId: Int) {
        _isShowingLoadingModal.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val movieDetailResult = movieUseCase.getDetailMovie(Token.TMDB_TOKEN_V3, movieId)
                val movieVideoResult = videoUseCase.getVideoTrailer(Token.TMDB_TOKEN_V3, movieId)

                if (movieDetailResult is Resource.Success && movieVideoResult is Resource.Success) {
                    movieDetail = movieDetailResult.data[0]
                    if (movieDetailResult.data.isNotEmpty()) {
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