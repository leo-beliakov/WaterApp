package com.leoapps.waterapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.common.data.GoogleAuthHelper
import com.leoapps.waterapp.auth.common.presentation.rememberGoogleAuthHelper
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            WaterAppTheme {
                TestScreen()
            }
        }
    }
}

//GetCredentialCancellationException: [16] Cancelled by user.

sealed interface TestUiEffect {
    data class Launch(
        val request: GetCredentialRequest
    ) : TestUiEffect
}


@HiltViewModel
class TestViewModel @Inject constructor(
    val googleAuthHelper: GoogleAuthHelper,
) : ViewModel() {

    private val _sideEffects = MutableSharedFlow<TestUiEffect>()
    val sideEffects: SharedFlow<TestUiEffect> = _sideEffects

    fun handleSignIn(result: GetCredentialResponse) = viewModelScope.launch {
        googleAuthHelper
            .handleSuccessSignIn(result)
            .collect { authResult ->
                when (authResult) {
                    TaskResult.Loading -> {}
                    is TaskResult.Failure -> {}
                    is TaskResult.Success -> {}
                }
            }
    }

    fun handleFailure(e: GetCredentialException) {
        Log.d("MyTag", "GoogleButton Exception: $e")
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(
                TestUiEffect.Launch(googleAuthHelper.signInRequest)
            )
        }
    }
}

@Composable
fun TestScreen(
    viewModel: TestViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val authHelper = rememberGoogleAuthHelper()

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is TestUiEffect.Launch -> {
                //For some reason, without switching to this scope we are getting
                //"Sign-in request cancelled by WaterApp" toast on UI
                scope.launch {
                    authHelper.launchAuthModal(
                        request = effect.request,
                        onSuccessfulAuth = viewModel::handleSignIn,
                        onFailedAuth = viewModel::handleFailure
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = viewModel::onLoginClicked,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = "LOGIN")
        }
    }
}

//TODO DONE:
//1.1. Make beverages buttons work - DONE
//2. Make progress change
//3. Make progress change animated
//4. Make progress animated when the screen first shown
//5. Make vibrations on buttons clicks
//6. Create a button for Onboarding screen
//7. Create a pulsation animation for that button
//8. Make current state persistent over sessions
//10. Make the size of the circle progress composable adaptive
//11. Refactor persistance code (Repo, etc) + clean up the VM
//13. Onboarding screen UI
//14. Onboarding screen showing logic
//16. Remove Splash and Onboarding from the back stack
//17. Insets for Onboarding screen
//18. Introduce Navigator classes
//17.1 Keyboard Insets for Auth
//17. Simple Auth Screen UI
//22. Make textbuttons clickable without ripple

//TODO:
//9. Think about corner cases like negative balance or balance > 100%
//11. Predefined TextStyles
//12. Implement a weekly/monthly chart
//15. Screen transition animations
//16. Simple Profile UI
//-. Simple Signup Screen UI
//18. Auth logic
//19. Retrieving profile logic
//20. Storing data about the progress in Firebase
//21. I don't like that all features' screens are exposed to the root. (See Login screens).
//23. Navigation issues on the Main screen with BackStack