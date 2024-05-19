package com.leoapps.waterapp.auth.common.presentation

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import timber.log.Timber

@Composable
fun rememberFacebookAuthHelper(): FacebookAuthUiHelper {
    val loginManager = LoginManager.getInstance()
    val callbackManager = CallbackManager.Factory.create()

    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {}

    val facebookAuthUiHelper = remember {
        FacebookAuthUiHelper(
            loginManager = loginManager,
            callbackManager = callbackManager,
            launcher = launcher
        )
    }

    DisposableEffect(Unit) {
        facebookAuthUiHelper.register()

        onDispose {
            facebookAuthUiHelper.unregister()
        }
    }

    return facebookAuthUiHelper
}

class FacebookAuthUiHelper(
    private val loginManager: LoginManager,
    private val callbackManager: CallbackManager,
    private val launcher: ManagedActivityResultLauncher<Collection<String>, CallbackManager.ActivityResultParameters>
) {
    var onSuccess: ((result: LoginResult) -> Unit)? = null
    var onFailure: ((e: FacebookException) -> Unit)? = null

    fun register() {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Timber.i("Facebook Auth onCancel")
            }

            override fun onError(error: FacebookException) {
                Timber.e(error, "Facebook Auth onError")
                onFailure?.invoke(error)
            }

            override fun onSuccess(result: LoginResult) {
                Timber.i("Facebook Auth onSuccess")
                onSuccess?.invoke(result)
            }
        })
    }

    fun unregister() {
        loginManager.unregisterCallback(callbackManager)
    }

    fun auth(
        onSuccess: (LoginResult) -> Unit,
        onFailure: (FacebookException) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure
        launcher.launch(listOf("email", "public_profile"))
    }
}