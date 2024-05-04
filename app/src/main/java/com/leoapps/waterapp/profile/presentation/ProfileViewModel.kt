package com.leoapps.waterapp.profile.presentation

import androidx.lifecycle.ViewModel
import com.leoapps.waterapp.auth.common.domain.IsUserAuthenticatedUseCase
import com.leoapps.waterapp.profile.presentation.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val isUserAuthenticated: IsUserAuthenticatedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProfileUiState(
            loginButtonVisible = !isUserAuthenticated()
        )
    )
    val state = _state.asStateFlow()

}