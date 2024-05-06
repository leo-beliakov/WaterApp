package com.leoapps.waterapp.auth.signup.domain

import com.leoapps.waterapp.auth.common.domain.AuthRepository
import javax.inject.Inject

class SignupUserUseCase @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val repository: AuthRepository
) {
    operator suspend fun invoke(
        name: String,
        email: String,
        password: String
    ): Boolean {
        return validateEmail(email) &&
                validatePassword(password) &&
                repository.signupUser(name, email, password)
    }
}