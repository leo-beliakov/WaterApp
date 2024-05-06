package com.leoapps.waterapp.auth.signup.presentation.mapper

import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.signup.domain.PasswordStrength
import com.leoapps.waterapp.auth.signup.domain.PasswordValidationResult
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import javax.inject.Inject

class SignupMapper @Inject constructor() {

    fun mapPasswordValidationResult(result: PasswordValidationResult): List<PasswordStrengthItemState> {
        return result.map { (strength, isPresent) ->
            PasswordStrengthItemState(
                textResId = when (strength) {
                    PasswordStrength.DIGITS_PRESENT -> R.string.password_validation_digits
                    PasswordStrength.LOWERCASE_LETTERS_PRESENT -> R.string.password_validation_lowercase
                    PasswordStrength.UPPERCASE_LETTERS_PRESENT -> R.string.password_validation_uppercase
                    PasswordStrength.SPECIAL_CHAR_PRESENT -> R.string.password_validation_special_caracter
                    PasswordStrength.NO_WHITE_SPACES -> R.string.password_validation_white_spaces
                    PasswordStrength.VALID_LENGTH -> R.string.password_validation_length
                },
                checkResult = if (isPresent) {
                    PasswordStrengthItemState.CheckResult.CHECK_SUCCED
                } else {
                    PasswordStrengthItemState.CheckResult.NOT_CHECKED
                },
            )
        }
    }
}