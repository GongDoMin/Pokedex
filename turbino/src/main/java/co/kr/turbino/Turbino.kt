package co.kr.turbino

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel

interface Turbino <T> {
    suspend fun cancel()

    suspend fun awaitEvent(): Event<T>

    suspend fun awaitItem(): T

    suspend fun awaitComplete()

    suspend fun awaitLastItem(expected: T): T

    fun ensureAllEventsConsumed()
}

internal class TurbinoImpl <T> (
    channel: Channel<T>,
    private val job: Job
) : Turbino<T> {
    private val channel = object : Channel<T> by channel {}

    override suspend fun cancel() {
        channel.close()
        job.cancel()
    }

    override suspend fun awaitEvent(): Event<T> = channel.awaitEvent()

    override suspend fun awaitItem(): T = channel.awaitItem()

    override suspend fun awaitComplete() = channel.awaitComplete()

    override suspend fun awaitLastItem(expected: T): T = channel.awaitLastItem(expected)

    override fun ensureAllEventsConsumed() {
        val unconsumed = mutableListOf<Event<T>>()
        while (true) {
            val event = channel.tryReceive().toEvent() ?: break
            if (event is Event.Complete) break
            unconsumed += event
        }

        if (unconsumed.isNotEmpty()) throw Error("Unconsumed events: $unconsumed")
    }
}