package com.fikri.netplix.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    var id: Int? = null,
    var name: String? = null
) : Parcelable
