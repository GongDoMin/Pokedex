package co.kr.mvisample.data.result

sealed class Result<out T> {
    class Success<T>(val data: T): Result<T>()
    data object Loading : Result<Nothing>()
    class Error(val throwable: Throwable): Result<Nothing>()
}