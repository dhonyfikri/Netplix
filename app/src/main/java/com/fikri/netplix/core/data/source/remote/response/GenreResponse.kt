package com.fikri.netplix.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)
