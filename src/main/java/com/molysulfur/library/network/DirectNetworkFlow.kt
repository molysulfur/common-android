package com.molysulfur.library.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.molysulfur.library.result.Result
import com.molysulfur.library.vo.ApiEmptyResponse
import com.molysulfur.library.vo.ApiErrorResponse
import com.molysulfur.library.vo.ApiResponse
import com.molysulfur.library.vo.ApiSuccessResponse
import kotlinx.coroutines.flow.flow
import retrofit2.Response

abstract class DirectNetworkFlow<RequestType, ResultType, ResponseType> {

    fun asFlow() = flow {
        emit(Result.Loading)
        when (val response: ApiResponse<ResponseType> = ApiResponse.create(createCall())) {
            is ApiSuccessResponse -> {
                val result: ResultType = convertToResultType(response.body)
                emit(Result.Success(result))
            }

            is ApiEmptyResponse -> {
                emit(Result.Success(null))
            }
            is ApiErrorResponse -> {
                onFetchFailed(response.errorMessage)
                emit(Result.Error(Exception(response.errorMessage)))
            }
        }
    }


    @MainThread
    protected abstract fun createCall(): Response<ResponseType>

    @MainThread
    protected abstract fun convertToResultType(response: ResponseType): ResultType

    @WorkerThread
    protected abstract fun onFetchFailed(errorMessage: String)

}