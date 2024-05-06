package com.leoapps.waterapp.auth.signup.domain

import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {

    operator fun invoke(email: String?): Boolean {
        return email != null && email.matches(VALID_EMAIL_REGEX.toRegex())
    }

    private companion object {
        // - Start with alphanumeric characters and may include underscores, plus signs, ampersands, asterisks, and hyphens.
        // - May contain dots followed by alphanumeric and allowed characters.
        // - Ensures the presence of an '@' symbol.
        // - Follows with domain components separated by dots and ending in a domain suffix from 2 to 7 letters long.
        const val VALID_EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    }
}