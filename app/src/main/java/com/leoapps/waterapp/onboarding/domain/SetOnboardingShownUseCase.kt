package com.leoapps.waterapp.onboarding.domain

import javax.inject.Inject

class SetOnboardingShownUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {

    suspend operator fun invoke(isShown: Boolean) {
        return repository.setOnboardingShown(isShown)
    }
}