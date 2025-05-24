package co.kr.mvisample.data.result

sealed class Result<out T> {
    class Success<T>(val data: T): Result<T>()
    class Loading<T>(val data: T? = null): Result<T>()
    class Error(val throwable: Throwable): Result<Nothing>()
}