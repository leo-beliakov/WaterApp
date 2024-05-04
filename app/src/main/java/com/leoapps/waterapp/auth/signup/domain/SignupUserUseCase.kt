package com.leoapps.waterapp.auth.signup.domain

import com.leoapps.waterapp.auth.common.domain.AuthRepository
import javax.inject.Inject

class SignupUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator suspend fun invoke(email: String, password: String): Boolean {
        return repository.signupUser(email, password)
    }
}