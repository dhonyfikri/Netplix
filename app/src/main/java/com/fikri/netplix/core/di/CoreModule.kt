package com.fikri.netplix.core.di

import com.fikri.netplix.core.data.MovieRepository
import com.fikri.netplix.core.data.source.remote.RemoteDataSource
import com.fikri.netplix.core.data.source.remote.network.ApiConfig
import com.fikri.netplix.core.domain.repository_interfaces.IMovieRepository
import org.koin.dsl.module

object CoreModule {
    val networkModule = module {
        single { ApiConfig.getApiService() }
    }
    val repositoryModule = module {
        single { RemoteDataSource(get()) }
//        single<IGenreRepository> { GenreRepository(get()) }
        single<IMovieRepository> { MovieRepository(get()) }
//        single<IReviewRepository> { ReviewRepository(get()) }
//        single<IVideoRepository> { VideoRepository(get()) }
    }
}