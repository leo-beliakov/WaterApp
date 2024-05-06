package com.leoapps.waterapp.auth.common.domain

import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator suspend fun invoke() {
        return repository.logoutUser()
    }
}