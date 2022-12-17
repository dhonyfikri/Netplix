package com.fikri.netplix.core.domain.use_case

import com.fikri.netplix.core.domain.repository_interfaces.IVideoRepository

class VideoInteractor(private val videoRepository: IVideoRepository) : VideoUseCase {
    override suspend fun getVideoTrailer(apiKey: String, movieId: Int) =
        videoRepository.getVideoTrailer(apiKey, movieId)
}