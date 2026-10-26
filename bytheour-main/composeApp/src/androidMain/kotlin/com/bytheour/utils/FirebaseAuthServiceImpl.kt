package com.bytheour.utils

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseAuthServiceImpl : FirebaseAuthService {
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun verifyPhoneNumber(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (Throwable) -> Unit,
        activityContext: PlatformActivityContext?
    ) {
        if (activityContext?.activity == null) {
            onVerificationFailed(Throwable("Activity not set"))
            return
        }

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activityContext.activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnSuccessListener {
                            it.user?.getIdToken(true)?.addOnSuccessListener { result ->
                                onVerificationCompleted(result.token ?: "")
                            }?.addOnFailureListener(onVerificationFailed)
                        }
                        .addOnFailureListener(onVerificationFailed)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    onCodeSent(verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun resendOtp(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (Throwable) -> Unit,
        activityContext: PlatformActivityContext?
    ) {
        if (activityContext?.activity == null) {
            onVerificationFailed(Throwable("Activity not set"))
            return
        }

        if (resendToken == null) {
            onVerificationFailed(Throwable("Resend token not available"))
            return
        }

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activityContext.activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnSuccessListener {
                            it.user?.getIdToken(true)?.addOnSuccessListener { result ->
                                onVerificationCompleted(result.token ?: "")
                            }?.addOnFailureListener(onVerificationFailed)
                        }
                        .addOnFailureListener(onVerificationFailed)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    onCodeSent(verificationId)
                }
            })
            .setForceResendingToken(resendToken!!) // <- Key for resend
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyOtpCode(
        verificationId: String,
        code: String,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener {
                it.user?.getIdToken(true)?.addOnSuccessListener { result ->
                    onSuccess(result.token ?: "")
                }?.addOnFailureListener(onFailure)
            }
            .addOnFailureListener(onFailure)
    }

    override fun hasResendToken(): Boolean = resendToken != null

}
