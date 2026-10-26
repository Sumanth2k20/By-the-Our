package com.bytheour.utils

interface FirebaseAuthService {
    fun verifyPhoneNumber(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (Throwable) -> Unit,
        activityContext: PlatformActivityContext?
    )

    fun verifyOtpCode(
        verificationId: String,
        code: String,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun resendOtp(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (Throwable) -> Unit,
        activityContext: PlatformActivityContext?
    )

    fun hasResendToken(): Boolean

}
