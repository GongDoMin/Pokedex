package co.kr.mvisample.data

import co.kr.mvisample.data.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> resultMapper(action: suspend () -> T): Flow<Result<T>> = flow {
    emit(Result.Loading)
    try {
        val data = action()
        emit(Result.Success(data))
    } catch (e: Exception) {
        emit(Result.Error(e))
    }
}