package com.molysulfur.library.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.molysulfur.library.result.Result
import com.molysulfur.library.vo.ApiEmptyResponse
import com.molysulfur.library.vo.ApiErrorResponse
import com.molysulfur.library.vo.ApiResponse
import com.molysulfur.library.vo.ApiSuccessResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber

abstract class NetworkFlow<RequestType, ResultType, ResponseType> {

    fun asFlow() = flow {
        Timber.e("asFlow")
        emit(Result.Loading)
        loadFromDb().collect { data: ResultType ->
            if (shouldFresh(data)) {
                when (val response: ApiResponse<ResponseType> = ApiResponse.create(createCall())) {
                    is ApiSuccessResponse -> {
                        val result: ResultType = convertToResultType(response.body)
                        saveToDb(result)
                        loadFromDb().collect { newData ->
                            emit(Result.Success(newData))
                        }
                    }
                    is ApiEmptyResponse -> {
                        loadFromDb().collect { newData ->
                            emit(Result.Success(newData))
                        }
                    }
                    is ApiErrorResponse -> {
                        onFetchFailed(response.errorMessage)
                        emit(Result.Error(Exception(response.errorMessage)))
                    }
                }
            } else {
                Result.Success(data)
            }
        }


    }

    @MainThread
    protected abstract fun shouldFresh(data: ResultType?): Boolean

    @MainThread
    protected abstract fun createCall(): Response<ResponseType>

    @MainThread
    protected abstract fun convertToResultType(response: ResponseType): ResultType

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @WorkerThread
    protected abstract fun saveToDb(data: ResultType)

    @WorkerThread
    protected abstract fun onFetchFailed(errorMessage: String)

}