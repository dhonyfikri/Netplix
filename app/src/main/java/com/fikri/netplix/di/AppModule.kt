package com.fikri.netplix.di

import com.fikri.netplix.core.domain.use_case.*
import com.fikri.netplix.view_model.GenreDiscoverViewModel
import com.fikri.netplix.view_model.MainViewModel
import com.fikri.netplix.view_model.SearchMovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module {
        factory<GenreUseCase> { GenreInteractor(get()) }
        factory<MovieUseCase> { MovieInteractor(get()) }
        factory<VideoUseCase> { VideoInteractor(get()) }
    }

    val viewModelModule = module {
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { SearchMovieViewModel(get(), get()) }
        viewModel { GenreDiscoverViewModel(get()) }
    }
}