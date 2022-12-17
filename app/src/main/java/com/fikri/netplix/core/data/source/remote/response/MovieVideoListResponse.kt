package com.fikri.netplix.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieVideoListResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("results") var results: ArrayList<MovieVideoResponse> = arrayListOf()
)
