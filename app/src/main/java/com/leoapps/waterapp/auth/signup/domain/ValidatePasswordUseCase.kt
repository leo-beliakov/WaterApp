package com.leoapps.waterapp.auth.signup.domain

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    operator fun invoke(password: String?): PasswordValidationResult {
        return passwordStrengthRegexMap.mapValues { (strength, regex) ->
            password?.matches(regex.toRegex()) ?: false
        }
    }

    private companion object {
        val passwordStrengthRegexMap = mapOf(
            PasswordStrength.DIGITS_PRESENT to ".*[0-9].*",
            PasswordStrength.LOWERCASE_LETTERS_PRESENT to ".*[a-z].*",
            PasswordStrength.UPPERCASE_LETTERS_PRESENT to ".*[A-Z].*",
            PasswordStrength.SPECIAL_CHAR_PRESENT to ".*[@#%^&+=!].*",
            PasswordStrength.NO_WHITE_SPACES to "\\S+\$",
            PasswordStrength.VALID_LENGTH to ".{8,}",
        )
    }
}

typealias PasswordValidationResult = Map<PasswordStrength, Boolean>

enum class PasswordStrength {
    DIGITS_PRESENT,
    LOWERCASE_LETTERS_PRESENT,
    UPPERCASE_LETTERS_PRESENT,
    SPECIAL_CHAR_PRESENT,
    NO_WHITE_SPACES,
    VALID_LENGTH,
}