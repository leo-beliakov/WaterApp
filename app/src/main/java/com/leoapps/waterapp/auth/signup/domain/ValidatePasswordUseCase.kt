package com.leoapps.waterapp.auth.signup.domain

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    operator fun invoke(email: String?): Boolean {
        return email != null && email.matches(VALID_EMAIL_REGEX.toRegex())
    }

    private companion object {
        // - ^(?=.*[0-9]) Ensures at least one digit is present.
        // - (?=.*[a-z]) Ensures at least one lowercase letter is present.
        // - (?=.*[A-Z]) Ensures at least one uppercase letter is present.
        // - (?=.*[@#$%^&+=!]) Ensures at least one special character from the set @#$%^&+=! is present.
        // - (?=\\S+$) Ensures no whitespace is in the password.
        // - .{8,} Ensures the password is at least 8 characters long.
        const val VALID_EMAIL_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    }
}