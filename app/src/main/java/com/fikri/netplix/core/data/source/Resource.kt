package com.fikri.netplix.core.data.source

sealed class Resource<out T> {
    class Success<out T>(val data: List<T>, val message: String? = null) : Resource<T>()
    class Loading<T> : Resource<T>()
    data class Error(
        val code: Int? = null,
        val failedType: String? = null,
        val message: String? = null
    ) : Resource<Nothing>()

    data class NetworkError(val failedType: String? = null, val message: String? = null) :
        Resource<Nothing>()
}