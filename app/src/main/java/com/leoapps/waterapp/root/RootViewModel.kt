package com.leoapps.waterapp.root

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<Unit>(Unit)
    private val state = _state.asStateFlow()
}