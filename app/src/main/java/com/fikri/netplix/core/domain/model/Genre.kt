package com.fikri.netplix.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    var id: Int? = null,
    var name: String? = null,
    var member: ArrayList<Movie> = arrayListOf()
) : Parcelable
