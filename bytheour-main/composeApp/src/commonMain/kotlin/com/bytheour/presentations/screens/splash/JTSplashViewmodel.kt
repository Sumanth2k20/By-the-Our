package com.bytheour.presentations.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JTURLS.ROLETYPE
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTSplashViewmodel(
    private val generalUseCases: GeneralUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {

    enum class SplashDestination {
        LOCATION, WELCOME
    }

    data class SplashUIState(
        val appVersion: String? = null,
        val showUpdateDialog: Boolean = false,
        val errorMessage: String? = null,
        val moveToNextScreen : Boolean? = null,
        val destination: SplashDestination? = null,
        val isUpdateInProgress: Boolean = false,
        val openStoreUrl: String? = null

    )

    private val _uiState = MutableStateFlow(SplashUIState())
    val uiState = _uiState.asStateFlow()

    init {
        ROLETYPE = sharedPreferenceUseCase.getUser()
    }


    fun onCancelUpdate() {
        _uiState.update { it.copy(showUpdateDialog = false) }
        decideNavigation()
    }

    fun onUpdateClick() {
        startUpdateProcess()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }



    fun appVersion(currentAppVersion: String) {
        viewModelScope.launch {
            when (val response = generalUseCases.getAppVersion()) {

                is JtApiResult.Success -> {

                    val latestVersion = if (platform.equals("iOS", ignoreCase = true)) {
                        response.data?.iOSVersion ?: currentAppVersion
                    } else {
                        response.data?.andriodVersion ?: currentAppVersion
                    }

                    _uiState.update { it.copy(appVersion = latestVersion) }

                    val shouldUpdate = if (platform.equals("Android", ignoreCase = true)){
                        compareVersions(currentAppVersion, latestVersion)
                    }else{
                        currentAppVersion != latestVersion
                    }


                    if (shouldUpdate) {
                        _uiState.update { it.copy(showUpdateDialog = true) }
                    } else {
                        decideNavigation()
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update { it.copy(errorMessage = response.error.message) }
                    decideNavigation()
                }
            }
        }
    }

    private fun startUpdateProcess() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdateInProgress = true) }

            // TODO: Replace this with your real API if required
            val result = generalUseCases.appStatusUseCase()

            if (result is JtApiResult.Success) {
                _uiState.update {
                    it.copy(
                        isUpdateInProgress = false,
                        showUpdateDialog = false
                    )
                }
                openPlayStore()
            } else if (result is JtApiResult.Failure) {
                _uiState.update {
                    it.copy(
                        isUpdateInProgress = false,
                        errorMessage = result.error.message
                    )
                }
            }
        }
    }

    private fun openPlayStore() {
        val url = if (platform.equals("Android", ignoreCase = true)) {
            "https://play.google.com/store/apps/details?id=com.bytheour"
        } else {
            "https://apps.apple.com/in/app/bytheour-service-pros-rental/id6471470381"
        }

        _uiState.update { it.copy(openStoreUrl = url) }
    }
    fun resetOpenStoreUrl() {
        _uiState.update { it.copy(openStoreUrl = null) }
    }

    private fun decideNavigation() {
        val roleType = sharedPreferenceUseCase.getUser()
        val token = sharedPreferenceUseCase.getToken()

        val destination = if (roleType.isNotBlank() && !token.isNullOrBlank()) {
            SplashDestination.LOCATION
        } else {
            SplashDestination.WELCOME
        }

        _uiState.update { it.copy(destination = destination) }
    }

    fun compareVersions(current: String, latest: String): Boolean {
        val currentParts = current.split('.').map { it.toIntOrNull() ?: 0 }
        val latestParts = latest.split('.').map { it.toIntOrNull() ?: 0 }

        for (i in 0 until maxOf(currentParts.size, latestParts.size)) {
            val currentPart = currentParts.getOrElse(i) { 0 }
            val latestPart = latestParts.getOrElse(i) { 0 }

            when {
                latestPart > currentPart -> return true
                latestPart < currentPart -> return false
            }
        }
        return false
    }

    fun getAppStatus() {
        viewModelScope.launch {
            when (val result = generalUseCases.appStatusUseCase()) {
                is JtApiResult.Success -> {
                    // Just update state if needed, but DO NOT navigate here
                    _uiState.update { it.copy(errorMessage = null) }
                }
                is JtApiResult.Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = result.error.message
                        )
                    }
                }
            }
        }
    }

}
