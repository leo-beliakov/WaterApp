package com.leoapps.waterapp.root.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.onboarding.domain.SetOnboardingShownUseCase
import com.leoapps.waterapp.onboarding.domain.ShouldShowOnboardingUseCase
import com.leoapps.waterapp.root.presentation.model.RootUiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val shouldShowOnboarding: ShouldShowOnboardingUseCase,
    private val setOnboardingShown: SetOnboardingShownUseCase
) : ViewModel() {

    private val _sideEffects = MutableSharedFlow<RootUiEffect>(replay = 1)
    val sideEffects: SharedFlow<RootUiEffect> = _sideEffects

    init {
        viewModelScope.launch {
            if (shouldShowOnboarding()) {
                setOnboardingShown(isShown = true)
                _sideEffects.emit(RootUiEffect.GoToOnboarding)
            } else {
                _sideEffects.emit(RootUiEffect.GoToMain)
            }
        }
    }
}