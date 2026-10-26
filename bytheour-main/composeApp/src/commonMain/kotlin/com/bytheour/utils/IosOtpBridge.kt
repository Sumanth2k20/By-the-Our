package com.bytheour.utils

import kotlin.concurrent.Volatile

interface IosOtpBridge {
    fun verifyPhoneNumber(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit, // iOS rarely auto-completes; keep for API parity
        onVerificationFailed: (String) -> Unit
    )

    fun verifyOtpCode(
        verificationId: String,
        code: String,
        onSuccess: (String) -> Unit,   // return Firebase ID token
        onFailure: (String) -> Unit
    )
}

object IosOtpBridgeRegistry {
    @Volatile
    var bridge: IosOtpBridge? = null

    fun register(impl: IosOtpBridge) {
        bridge = impl
    }
}