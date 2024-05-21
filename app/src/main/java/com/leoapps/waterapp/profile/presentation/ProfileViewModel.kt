package com.leoapps.waterapp.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.common.domain.DeleteUserUseCase
import com.leoapps.waterapp.auth.common.domain.GetCurrentUserAsFlowUseCase
import com.leoapps.waterapp.auth.common.domain.LogoutUserUseCase
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.profile.presentation.model.ProfileUiEffect
import com.leoapps.waterapp.profile.presentation.model.ProfileUiState
import com.leoapps.waterapp.water.domain.GetUserWaterDataAsFlowUseCase
import com.leoapps.waterapp.water.domain.model.WaterData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserAsFlow: GetCurrentUserAsFlowUseCase,
    private val getUserWaterDataAsFlow: GetUserWaterDataAsFlowUseCase,
    private val logOutUser: LogoutUserUseCase,
    private val deleteUser: DeleteUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<ProfileUiEffect>()
    val sideEffects: SharedFlow<ProfileUiEffect> = _sideEffects

    init {
        getCurrentUserAsFlow()
            .onEach(::onUserUpdated)
            .distinctUntilChanged { old, new -> new?.id == old?.id }
            .flatMapLatest { user ->
                user?.id?.let { getUserWaterDataAsFlow(it) } ?: emptyFlow()
            }
            .onEach(::onWaterDataUpdated)
            .launchIn(viewModelScope)
    }

    private fun onUserUpdated(user: User?) {
        Timber.d("New User observed: $user")

        _state.update {
            it.copy(
                username = user?.name ?: "",
                email = user?.email ?: "",
                profileInfoVisible = user != null,
                loginButtonVisible = user == null,
            )
        }
    }

    private fun onWaterDataUpdated(data: WaterData?) {
        _state.update {
            it.copy(
                goal = data?.goal.toString(),
                previousRecords = data?.records?.joinToString { it.toString() } ?: "No recordings"
            )
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(ProfileUiEffect.OpenLogin)
        }
    }

    fun onLogOutClicked() {
        viewModelScope.launch {
            logOutUser()
        }
    }

    fun onDeleteAccountClicked() = viewModelScope.launch {
        deleteUser().collectLatest { deleteResult ->
            when (deleteResult) {
                TaskResult.Loading -> setDeleteButtonLoading(true)
                is TaskResult.Failure -> setDeleteButtonLoading(false)
                is TaskResult.Success -> setDeleteButtonLoading(false)
            }
        }
    }

    private fun setDeleteButtonLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                deleteButtonState = it.deleteButtonState.copy(
                    isLoading = isLoading
                )
            )
        }
    }
}