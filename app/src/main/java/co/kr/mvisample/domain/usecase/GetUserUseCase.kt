package co.kr.mvisample.domain.usecase

import co.kr.mvisample.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor() {
    operator fun invoke(): Flow<User> = flow {
        emit(
            User(
                name = "bruce lee",
                age = 21,
                weight = 60,
                height = 170,
            )
        )
    }
}