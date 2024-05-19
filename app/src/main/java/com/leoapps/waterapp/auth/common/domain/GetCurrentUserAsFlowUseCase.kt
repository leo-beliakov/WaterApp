package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.water.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserAsFlowUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(): Flow<User?> {
        return repository.getCurrentUserAsFlow()
    }
}