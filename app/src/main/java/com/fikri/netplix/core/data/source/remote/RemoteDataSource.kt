package com.fikri.netplix.core.data.source.remote

import com.fikri.netplix.core.data.source.remote.network.ApiService
import com.fikri.netplix.core.data.source.remote.response.ApiResultWrapper
import com.fikri.netplix.core.data.source.remote.response.GenreListResponse
import com.fikri.netplix.core.data.source.remote.response.MovieListResponse
import com.fikri.netplix.core.ui.modal.ResponseModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException

@Suppress("BlockingMethodInNonBlockingContext")
class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getLatestMovieList(apiKey: String): ApiResultWrapper<MovieListResponse> {
        val apiRequest = apiService.getListMovie(apiKey = apiKey)

        try {
            val response: Response<MovieListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }

    suspend fun getAllGenre(
        apiKey: String
    ): ApiResultWrapper<GenreListResponse> {
        val apiRequest = apiService.getAllGenre(apiKey = apiKey)

        try {
            val response: Response<GenreListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }
}


//class GenreRepository(private val remoteDataSource: RemoteDataSource) : IGenreRepository {
//    override fun getAllGenre(apiKey: String): Flow<Resource<Genre>> {
//        return flow {
//            emit(Resource.Loading())
//            when (val result = remoteDataSource.getAllGenre(apiKey)) {
//                is ApiResultWrapper.Success -> {
//                    val listGenre: ArrayList<Genre> = arrayListOf()
//                    result.response.genres.forEach {
//                        val genre = Genre(it.id, it.name)
//                        listGenre.add(genre)
//                    }
//                    emit(Resource.Success(listGenre))
//                }
//                is ApiResultWrapper.Error -> {
//                    val code: Int? = result.code
//                    val failedType: String? = result.failedType
//                    val message: String? = result.message
//                    emit(Resource.Error(code, failedType, message))
//                }
//                is ApiResultWrapper.NetworkError -> {
//                    val failedType: String? = result.failedType
//                    val message: String? = result.message
//                    emit(Resource.NetworkError(failedType, message))
//                }
//            }
//        }
//    }
//}