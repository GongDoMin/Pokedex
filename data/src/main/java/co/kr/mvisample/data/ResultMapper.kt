package co.kr.mvisample.data

import co.kr.mvisample.data.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <DataModel> resultMapper(action: suspend () -> DataModel): Flow<Result<DataModel>> = flow {
    emit(Result.Loading(null))
    try {
        val data = action()
        emit(Result.Success(data))
    } catch (e: Exception) {
        emit(Result.Error(e))
    }
}

fun <DataModel> resultMapperWithLocal(
    localAction: suspend () -> DataModel,
    remoteAction: suspend () -> DataModel,
) : Flow<Result<DataModel>> = flow {
    emit(Result.Loading(localAction()))
    try {
        val data = remoteAction()
        emit(Result.Success(data))
    } catch (e: Exception) {
        emit(Result.Error(e))
    }
}