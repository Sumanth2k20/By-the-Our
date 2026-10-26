package com.bytheour.presentations.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JTURLS.ROLETYPE
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.LoginRequest
import com.bytheour.useCases.EntryModuleUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.COUNTRY_CODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTLoginViewModel(
    private val entryModuleUseCase: EntryModuleUseCases<Any?>,
    sharedPreferenceUseCase: SharedPreferenceUseCase,
):ViewModel() {

    data class UIStates(
        val number: String? = "",
        val voiceText: String? = null,
        val errorMessage: String? = null,
        val isSelected: Boolean? = false,
        val isNavigate: Boolean? = false,
        val isExistCheck: Boolean? = false,
        val countryCode: String = "91",
        val mobileNumber: String = "",
        val mobileNumberError:String? = null,
        val message:String? = null
    )

    private val _uiStates = MutableStateFlow(UIStates())
    val uiStates =_uiStates.asStateFlow()
    val userType = sharedPreferenceUseCase.getUser()

    init {
        ROLETYPE = userType
    }
    fun mobileNumberChange(number: String) {
        if (number.length <= 10 && number.all { it.isDigit() }) {
            _uiStates.update { currentValue ->
                currentValue.copy(
                    number = number,
                    errorMessage = null,
                    mobileNumber = number // Update the mobileNumber property
                )
            }
        }
    }



    fun validation(): Boolean {
        val currentNumber = uiStates.value.number.orEmpty()

        val mobileNumberError = when {
            currentNumber.isBlank() -> "Please Enter Mobile Number"
            currentNumber.length != 10 -> "Mobile Number Must Be Exactly 10 Digits"
            else -> ""
        }

        _uiStates.update { currentState ->
            currentState.copy(mobileNumberError = mobileNumberError)
        }

        return mobileNumberError.isEmpty()
    }

    fun clearMobileNumberError(){
        _uiStates.update {
            it.copy(mobileNumberError = null)
        }
    }

    fun updateMessage(msg:String){
        _uiStates.update {
            it.copy(message = msg)
        }
    }

    fun updateErrorMessage(msg:String){
        _uiStates.update {
            it.copy(errorMessage = msg)
        }
    }

    fun clearError(){
        _uiStates.update {
            it.copy(
                errorMessage = null,
                message = null
            )
        }
    }

    fun makeLogin(
        number: String,
        onOtpSend: () -> Unit = {},
        onOtpFailed: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            val request = LoginRequest(field = "$COUNTRY_CODE$number")
            val result = entryModuleUseCase.loginUseCase(request)

            if (result is JtApiResult.Success) {
                _uiStates.update { state ->
                    state.copy(
                        isExistCheck = result.data.data.exists,
                        number = number,
                        isNavigate = true,
                        mobileNumber = result.data.data.contact.number
                    )
                }
                onOtpSend()
            } else if (result is JtApiResult.Failure) {
                _uiStates.update {
                    it.copy(
                        errorMessage = result.error.message,
                        isNavigate = false,
                        isExistCheck = false
                    )
                }
                onOtpFailed(Throwable(result.error.message))
            }
        }
    }



}