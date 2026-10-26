package com.bytheour.utils


class FirebaseAuthServiceImpl() : FirebaseAuthService {

    private var verificationToken: String? = null

    override fun verifyPhoneNumber(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (Throwable) -> Unit,
        activityContext: PlatformActivityContext?
    ) {
        val b = IosOtpBridgeRegistry.bridge ?: run {
            onVerificationFailed(IllegalStateException("iOS OTP bridge not registered"))
            return
        }

        b.verifyPhoneNumber(
            phoneNumber = phoneNumber,
            onCodeSent = { verificationId ->
                println("verificationId of ios $verificationId")
                verificationToken = verificationId  // treat verificationId as "token"
                onCodeSent(verificationId)
            },
            onVerificationCompleted = onVerificationCompleted,
            onVerificationFailed = { msg -> onVerificationFailed(Throwable(msg)) }
        )
    }

    override fun resendOtp(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (Throwable) -> Unit,
        activityContext: PlatformActivityContext?
    ) {
        val b = IosOtpBridgeRegistry.bridge ?: run {
            onVerificationFailed(IllegalStateException("iOS OTP bridge not registered"))
            return
        }

        if (verificationToken == null) {
            onVerificationFailed(IllegalStateException("Resend token not available"))
            return
        }

        // Resend is still just verifyPhoneNumber again in Firebase iOS
        b.verifyPhoneNumber(
            phoneNumber = phoneNumber,
            onCodeSent = { verificationId ->
                verificationToken = verificationId  // update pseudo-token
                onCodeSent(verificationId)
            },
            onVerificationCompleted = onVerificationCompleted,
            onVerificationFailed = { msg -> onVerificationFailed(Throwable(msg)) }
        )
    }

    override fun verifyOtpCode(
        verificationId: String,
        code: String,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val b = IosOtpBridgeRegistry.bridge ?: run {
            onFailure(IllegalStateException("iOS OTP bridge not registered"))
            return
        }

        b.verifyOtpCode(
            verificationId = verificationId,
            code = code,
            onSuccess = onSuccess,
            onFailure = { msg -> onFailure(Throwable(msg)) }
        )
    }

    override fun hasResendToken(): Boolean {
        return verificationToken != null
    }
}


