package co.kr.mvisample.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlusNumberUseCase @Inject constructor() {
    operator fun invoke(): Flow<Unit> = flow {
        delay(500)
        emit(Unit)
    }
}