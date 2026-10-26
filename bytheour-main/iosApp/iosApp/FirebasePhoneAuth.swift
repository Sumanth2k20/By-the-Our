//
//  FirebasePhoneAuth.swift
//  iosApp
//
//  Created by Sai Sumanth on 05/06/25.
//  Copyright © 2025 orgName. All rights reserved.
//


import Foundation
import FirebaseAuth
import ComposeApp



 class FirebasePhoneAuth: NSObject {

    static func verifyPhoneNumber(
        _ phoneNumber: String,
        onCodeSent: @escaping (String) -> Void,
        onVerificationCompleted: @escaping (String) -> Void,
        onVerificationFailed: @escaping (NSError) -> Void
    ) {
        let phoneNumberWithCountryCode = phoneNumber
        print("FirebasePhoneAuth ====================== $\(phoneNumberWithCountryCode)")
        PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumberWithCountryCode, uiDelegate: nil) { verificationID, error in
            if let error = error {
                onVerificationFailed(error as NSError)
                return
            }

            if let verificationID = verificationID {
                onCodeSent(verificationID)
            }
        }
    }

    static func verifyOtpCode(
        _ verificationID: String,
        code: String,
        onSuccess: @escaping (String) -> Void,
        onFailure: @escaping (NSError) -> Void
    ) {
        let credential = PhoneAuthProvider.provider().credential(withVerificationID: verificationID, verificationCode: code)

        Auth.auth().signIn(with: credential) { authResult, error in
            if let error = error {
                onFailure(error as NSError)
                return
            }

            authResult?.user.getIDToken(completion: { tokenResult, tokenError in
                if let tokenError = tokenError {
                    onFailure(tokenError as NSError)
                } else if let token = tokenResult {
                    onSuccess(token)
                }
            })
        }
    }
}

//class IOSFirebaseAuthService: NSObject {
//   static let shared = IOSFirebaseAuthService()
//
//     func verifyPhoneNumber(
//         phoneNumber: String,
//         onCodeSent: @escaping (String) -> Void,
//         onVerificationCompleted: @escaping (String) -> Void,
//         onVerificationFailed: @escaping (NSError) -> Void
//     ) {
//         FirebasePhoneAuth.verifyPhoneNumber(
//             phoneNumber,
//             onCodeSent: onCodeSent,
//             onVerificationCompleted: onVerificationCompleted,
//             onVerificationFailed: onVerificationFailed
//         )
//     }
//
//   func verifyOtpCode(
//         verificationID: String,
//         code: String,
//         onSuccess: @escaping (String) -> Void,
//         onFailure: @escaping (NSError) -> Void
//     ) {
//         FirebasePhoneAuth.verifyOtpCode(
//             verificationID,
//             code: code,
//             onSuccess: onSuccess,
//             onFailure: onFailure
//         )
//     }
// }
