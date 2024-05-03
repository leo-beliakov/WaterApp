package com.leoapps.waterapp.onboarding.domain

import javax.inject.Inject

class ShouldShowOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {

    suspend operator fun invoke(): Boolean {
        return repository.isOnboardingShown()
    }
}