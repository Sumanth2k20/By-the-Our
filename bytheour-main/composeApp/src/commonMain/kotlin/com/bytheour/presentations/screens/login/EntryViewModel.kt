package com.bytheour.presentations.screens.login

import androidx.lifecycle.ViewModel
import com.bytheour.showProgress
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.FirebaseAuthService
import com.bytheour.utils.PlatformActivityContext


class EntryViewModel(
    private val otpSender: FirebaseAuthService,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase,
) : ViewModel() {

    private var activityInstance: PlatformActivityContext? = null

    fun assignActivityContext(context: PlatformActivityContext?) {
        activityInstance = context
    }

    fun sendFirebaseNumber(
        phoneNumber: String,
        onCodeSent: () -> Unit = {},
        onVerificationCompleted: (String) -> Unit = {},
        onVerificationFailed: (Throwable) -> Unit = {}
    ) {
        println("EntryViewModel sendFirebaseNumber called with: $phoneNumber")
        showProgress.value = true
        otpSender.verifyPhoneNumber(
            phoneNumber = phoneNumber,
            onCodeSent = { id ->
               sharedPreferenceUseCase.putVerificationId(id)
                showProgress.value = false
                onCodeSent()
            },
            onVerificationCompleted = { token ->
                onVerificationCompleted(token)
                showProgress.value = false
            },
            onVerificationFailed = { error ->
                onVerificationFailed(error)
                showProgress.value = false
            },
            activityContext = activityInstance
        )
    }

    fun resendOtp(
        phoneNumber: String,
        onCodeSent: () -> Unit = {},
        onVerificationCompleted: (String) -> Unit = {},
        onVerificationFailed: (Throwable) -> Unit = {}
    ) {
        println("EntryViewModel resendOtp called with: $phoneNumber")
        showProgress.value = true
        otpSender.resendOtp(
            phoneNumber = phoneNumber,
            onCodeSent = { id ->
                sharedPreferenceUseCase.putVerificationId(id)
                showProgress.value = false
                onCodeSent()
            },
            onVerificationCompleted = { token ->
                onVerificationCompleted(token)
                showProgress.value = false
            },
            onVerificationFailed = { error ->
                onVerificationFailed(error)
                showProgress.value = false
            },
            activityContext = activityInstance
        )
    }

    fun verifyOtpCode(
        code: String,
        onSuccess: (String) -> Unit = {},
        onFailure: (Throwable) -> Unit = {},
    ) {
        val id =   sharedPreferenceUseCase.getVerificationId()
        if (id == null) {
            onFailure(Throwable("Verification ID is missing. Please request OTP again."))
            return
        }
        showProgress.value = true
        otpSender.verifyOtpCode(
            verificationId = id,
            code = code,
            onSuccess = { token ->
                showProgress.value = false
                println("OTP Verified, -------------------------  token: $token")
                onSuccess(token)
            },
            onFailure = { error ->
                showProgress.value = false
                println("OTP Verification Failed: ${error.message}")
                onFailure(error)
            }
        )
    }

    fun hasResendToken(): Boolean = otpSender.hasResendToken()

}
