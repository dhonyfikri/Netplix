package com.fikri.netplix.di

import com.fikri.netplix.core.domain.use_case.MovieInteractor
import com.fikri.netplix.core.domain.use_case.MovieUseCase
import com.fikri.netplix.view_model.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module {
//        factory<GenreUseCase> { GenreInteractor(get()) }
        factory<MovieUseCase> { MovieInteractor(get()) }
//        factory<ReviewUseCase> { ReviewInteractor(get()) }
//        factory<VideoUseCase> { VideoInteractor(get()) }
    }

    val viewModelModule = module {
        viewModel { MainViewModel(get()) }
//        viewModel { MovieDetailViewModel(get(), get(), get()) }
//        viewModel { GenreDiscoverViewModel(get()) }
//        viewModel { MovieReviewViewModel(get()) }
    }
}