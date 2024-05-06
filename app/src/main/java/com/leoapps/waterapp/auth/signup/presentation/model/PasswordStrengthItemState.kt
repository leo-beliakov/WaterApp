package com.leoapps.waterapp.auth.signup.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.leoapps.waterapp.R

data class PasswordStrengthItemState(
    @StringRes val textResId: Int,
    val checkResult: CheckResult
) {
    enum class CheckResult(
        @DrawableRes val iconResId: Int
    ) {
        CHECK_SUCCED(iconResId = R.drawable.ic_check_green),
        CHECK_FAILED(iconResId = R.drawable.ic_cross_red),
        NOT_CHECKED(iconResId = R.drawable.ic_check_gray),
    }
}