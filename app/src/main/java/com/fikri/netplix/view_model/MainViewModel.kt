package com.fikri.netplix.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.data.source.remote.network.Token
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.domain.use_case.MovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val _listLatestMovie = MutableLiveData<Resource<Movie>>()
    val listLatestMovie: LiveData<Resource<Movie>> = _listLatestMovie

    var scrollviewPositionY = 0

    init {
        getLatestMovieList()
    }

    fun getLatestMovieList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = movieUseCase.getLatestMovieList(Token.TMDB_TOKEN_V3)
                _listLatestMovie.postValue(result)
            }
        }
    }
}