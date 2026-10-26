import Foundation
import FirebaseCore
import FirebaseAuth
import ComposeApp

final class IosOtpBridgeImpl: IosOtpBridge { // Protocol is generated from Kotlin
    private var verificationId: String? = nil

    func verifyPhoneNumber(
        phoneNumber: String,
        onCodeSent: @escaping (String) -> Void,
        onVerificationCompleted: @escaping (String) -> Void,
        onVerificationFailed: @escaping (String) -> Void
    ) {
        PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, uiDelegate: nil) { verificationID, error in
            if let error = error {
                onVerificationFailed(error.localizedDescription)
                return
            }
            if let verificationID = verificationID {
                self.verificationId = verificationID
                // Treat verificationID as the "resend token"
                onCodeSent(verificationID)
            }
            // iOS usually doesn't auto-complete; if you implement instant verification somehow:
            // onVerificationCompleted("custom-token")
        }
    }

    func verifyOtpCode(
        verificationId: String,
        code: String,
        onSuccess: @escaping (String) -> Void,
        onFailure_ onFailure: @escaping (String) -> Void   // <— note the underscore here, NOT in the name
    ) {
        let credential = PhoneAuthProvider.provider()
            .credential(withVerificationID: verificationId, verificationCode: code)

        Auth.auth().signIn(with: credential) { authResult, error in
            if let error = error {
                onFailure(error.localizedDescription)
                return
            }
            authResult?.user.getIDToken(completion: { token, err in
                if let err = err {
                    onFailure(err.localizedDescription)
                } else if let token = token {
                    onSuccess(token)
                } else {
                    onFailure("Empty token")
                }
            })
        }
    }

    func resendOtp(
        phoneNumber: String,
        onCodeSent: @escaping (String) -> Void,
        onVerificationCompleted: @escaping (String) -> Void,
        onVerificationFailed: @escaping (String) -> Void
    ) {
        guard let _ = verificationId else {
            onVerificationFailed("Resend token not available")
            return
        }

        // In Firebase iOS, resend is still just calling verifyPhoneNumber again
        PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, uiDelegate: nil) { verificationID, error in
            if let error = error {
                onVerificationFailed(error.localizedDescription)
                return
            }
            if let verificationID = verificationID {
                self.verificationId = verificationID
                onCodeSent(verificationID)
            }
        }
    }

    func hasResendToken() -> Bool {
        return verificationId != nil
    }
}
