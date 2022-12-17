package com.fikri.netplix.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.use_case.MovieUseCase

class GenreDiscoverViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    var selectedGenre: Genre? = null

    fun getMovieByGenre(apiKey: String) =
        movieUseCase.getEndlessMovieByGenre(apiKey, selectedGenre?.id.toString())
            .cachedIn(viewModelScope)
}