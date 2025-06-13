package co.kr.turbino

sealed interface Event<out T> {
    data object Complete : Event<Nothing>
    data class Error(val throwable: Throwable) : Event<Nothing>
    data class Item<T>(val value: T) : Event<T>
}