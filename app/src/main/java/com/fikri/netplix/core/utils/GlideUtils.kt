package com.fikri.netplix.core.utils

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fikri.netplix.R

object GlideUtils {
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(false)
        .error(R.drawable.default_image)

    const val thumbnailQuality = 0.05f
}