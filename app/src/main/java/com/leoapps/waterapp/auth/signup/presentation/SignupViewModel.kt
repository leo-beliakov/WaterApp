package com.leoapps.waterapp.auth.signup.presentation

import androidx.lifecycle.ViewModel
import com.leoapps.waterapp.auth.signup.domain.SignupUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUser: SignupUserUseCase
) : ViewModel() {

}