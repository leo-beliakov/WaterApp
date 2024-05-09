package com.leoapps.waterapp.auth.signup.domain

import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {

    operator fun invoke(fullName: String?): Boolean {
        return fullName != null && fullName.matches(VALID_NAME_REGEX.toRegex())
    }

    private companion object {
        // - Start with letters, may include spaces and hyphens.
        // - Must consist of at least two words, indicating a first name and a last name.
        const val VALID_NAME_REGEX =
            "^[a-zA-Z]+(?:[ -][a-zA-Z]+)+\$"
    }
}