package com.leoapps.waterapp.auth.common.domain

import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(): Boolean {
        return repository.isAuthenticated()
    }
}