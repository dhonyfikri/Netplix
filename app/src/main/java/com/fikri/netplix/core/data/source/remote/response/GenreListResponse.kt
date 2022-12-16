package com.fikri.netplix.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    @SerializedName("genres") var genres: ArrayList<GenreResponse> = arrayListOf()
)
