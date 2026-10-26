package com.bytheour.presentations.screens.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.EntryModuleUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel(
    private val entryModuleUseCases: EntryModuleUseCases<Any?>,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {

    data class UIState(
        val otp: String = "",
        val mobile: String? = null,
        val isSelected: Boolean? = false,
        val otpError: String? = null,
        val message: String? = null,
        val errorMessage: String? = null,
        val isAdmin: Boolean = false,
        val isOtpVerified: Boolean = false // ✅ new flag
    )

    private val _uiStates = MutableStateFlow(UIState())
    val uiStates = _uiStates.asStateFlow()

    fun updateMessage(message: String) {
        _uiStates.update {
            it.copy(message = message)
        }
    }

    fun updateErrorMessage(msg: String) {
        _uiStates.update {
            it.copy(errorMessage = msg)
        }
    }

    fun clearError() {
        _uiStates.update {
            it.copy(errorMessage = null, message = null)
        }
    }

    fun markOtpVerified() {
        _uiStates.update {
            it.copy(isOtpVerified = true)
        }
    }

    fun clearOtpVerifiedFlag() {
        _uiStates.update {
            it.copy(isOtpVerified = false)
        }
    }

    fun login(
        contact: String? = null,
        securityToken: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = entryModuleUseCases.login(
                contact = contact.orEmpty(),
                securityToken = securityToken
            )
            when (result) {
                is JtApiResult.Success -> {
                    if (result.data.data != null) {
                        sharedPreferenceUseCase.putUserInfo(result.data.data)
                        markOtpVerified()
                        onSuccess()
                        _uiStates.update {
                            it.copy(
                                isAdmin = result.data.data.flags.IsAdmin
                            )
                        }
                        sharedPreferenceUseCase.putIsAdmin(result.data.data.flags.IsAdmin)
                        sharedPreferenceUseCase.putUserInfo(result.data.data)
                    } else {
                        _uiStates.update {
                            it.copy(errorMessage = result.data.meta.message)
                        }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(errorMessage = result.error.message)
                    }
                }
            }
        }
    }



    fun onOtpChange(newOtp: String) {
        _uiStates.update { currentState ->
            currentState.copy(otp = newOtp, otpError = null)
        }
    }
    fun validateOtp(): Boolean {
        val currentOtp = uiStates.value.otp
        val otpError = when {
            currentOtp.isEmpty() -> "Please Enter Otp Number"
            currentOtp.length < 6 -> "Otp Number Must Be 6 Digits"
            else -> ""
        }

        _uiStates.update { currentState ->
            currentState.copy(otpError = otpError)
        }
        return currentOtp.length == 6
    }
    fun isSelected(){
        _uiStates.update {
            it.copy(isSelected = true)
        }
    }

    fun clearOtp() {
        _uiStates.update {
            it.copy(otp = "")
        }
    }
}
