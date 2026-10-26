//
//  StripeUiBridgeImpl.swift
//  iosApp
//
//  Created by Mounika Sakinapally on 31/08/25.
//  Copyright © 2025 orgName. All rights reserved.
//


import Foundation
import Stripe
import UIKit
import ComposeApp

class StripeUiBridgeImpl: NSObject, StripeUiBridge, StripePlatformBridge, STPPaymentContextDelegate {
    func paymentContext(_ paymentContext: Stripe.STPPaymentContext, didFailToLoadWithError error: any Error) {
        print("---------------")
    }
    
    
    var userData:[String:Any] = [:]
    var associatedObjectsArray: [[String: Any]] = []
    private var customerContext: STPCustomerContext!
    private var paymentContext: STPPaymentContext!
    var paymentResultsForIntent :STPPaymentResult?

    // MARK: - Setup from Ephemeral Key
    func showStripeBillingScreen(ephemeralKey: GetEphemeraKeyResponse.Data) {
        // Convert ephemeralKey to JSON
        associatedObjectsArray.removeAll()
        for associatedObject in ephemeralKey.associatedObjects {
            let obj: [String: Any] = [
                "id": associatedObject.id,
                "type": associatedObject.type
            ]
            associatedObjectsArray.append(obj)
        }
        
        let jsonObject: [String: Any] = [
            "id": ephemeralKey.id,
            "object": ephemeralKey.objec,
            "associated_objects": associatedObjectsArray,
            "created": ephemeralKey.created,
            "expires": ephemeralKey.expires,
            "livemode": ephemeralKey.livemode,
            "secret": ephemeralKey.secret
        ]
        
        userData = jsonObject
        prepareStripe()
    }
    
    func prepareStripe(){
        let apiClient = MyAPIClient()
        apiClient.userData = userData
        customerContext = STPCustomerContext(keyProvider: apiClient)

        guard let cusContext = customerContext else { return }
        // 2) Payment configuration
        let config = STPPaymentConfiguration.shared
        config.requiredBillingAddressFields = .none
        config.requiredShippingAddressFields = []
        config.companyName = "Your App"
        

        // 3) Payment context
        paymentContext = STPPaymentContext(customerContext: cusContext,
                                           configuration: config,
                                           theme: STPTheme.defaultTheme)
        paymentContext.hostViewController = UIApplication.shared.windows.first?.rootViewController
        paymentContext.delegate = self
        paymentContext.paymentCurrency = "USD"
        paymentContext.paymentAmount = 0
        
        let userInformation = STPUserInformation()
        paymentContext.prefilledInformation = userInformation

        StripeBridgeRegistry.shared.registerBridge(b: self)

    }
    
    // MARK: - StripePlatformBridge methods
    
    func addPaymentMethod() {
        paymentContext.presentPaymentOptionsViewController()
    }
    
    func doInitCustomerSession(ephemeralKey: GetEphemeraKeyResponse.Data) {
        print("")
    }
    
    func initializePaymentSession(amountInMinorUnits: Int64, currency: String) {
        paymentContext.paymentAmount = Int(amountInMinorUnits)
        paymentContext.paymentCurrency = currency
    }
    
    func processPayment(paymentMethod: String) {
        // Stripe SDK will handle using the selected payment method in paymentContext
        paymentContext.defaultPaymentMethod = paymentMethod
        paymentContext.requestPayment()
    }
    
    func reset() {
        customerContext = nil
        paymentContext = nil
    }
    
    func retrievePaymentIntnt(clientSecret: String) {
        print("Secret key", clientSecret)
        let params = STPPaymentIntentParams(clientSecret: clientSecret)
        if let paymentResults = paymentResultsForIntent{
            params.configure(with: paymentResults)
        }
        params.returnURL = "payments-example://stripe-redirect"
        
        STPPaymentHandler.shared().confirmPayment(params, with: paymentContext) { status, intent, error in
            switch status {
            case .succeeded:
                if let stripeId = intent?.stripeId {
                    StripeBridgeRegistry.shared.callCompletePaymentAPI(stripeId: stripeId, mode: "stripe")
                }
            case .failed, .canceled:
                if let error = error {
                    StripeBridgeRegistry.shared.notifyFailure(message: error.localizedDescription)
                }
            @unknown default:
                break
            }
        }
//        STPPaymentHandler.shared().confirmPayment(params, with: paymentContext) { status, intent, error in
//               switch status {
//               case .succeeded:
//                   print("✅ success: \(String(describing: intent?.stripeId))")
//               case .failed:
//                   print("❌ failed: \(String(describing: error?.localizedDescription))")
//               case .canceled:
//                   print("⚠️ cancelled")
//               @unknown default:
//                   print("❓ unknown error")
//               }
//           }
    }
    
    func selectPaymentMethod() {
        paymentContext.presentPaymentOptionsViewController()
    }
    
    // MARK: - STPPaymentContextDelegate
    
    func paymentContextDidChange(_ paymentContext: STPPaymentContext) {
        print("Payment context updated")
        if let paymentLabel = paymentContext.selectedPaymentOption?.label{
            StripeBridgeRegistry.shared.savePaymentMethodIOS(paymentMethod: paymentLabel)
        }
    }
    
    func paymentContext(_ paymentContext: STPPaymentContext, didCreatePaymentResult paymentResult: STPPaymentResult, completion: @escaping STPPaymentStatusBlock) {
        if let paymentMethod = paymentResult.paymentMethod {
            do {
                let jsonData = try JSONSerialization.data(withJSONObject: paymentMethod.allResponseFields, options: [])
                let jsonString = String(data: jsonData, encoding: .utf8) ?? "{}"
                paymentResultsForIntent = paymentResult
                StripeBridgeRegistry.shared.callPaymentIntentAPI(paymentMethod: jsonString)
            } catch {
                print("❌ Failed to serialize paymentMethod: \(error)")
            }
        }

        
    }
    
    func paymentContext(_ paymentContext: STPPaymentContext, didFinishWith status: STPPaymentStatus, error: Error?) {
        switch status {
        case .success:
            print("Payment succeeded")
        case .error:
            StripeBridgeRegistry.shared.notifyFailure(message: error?.localizedDescription ?? "Unknown error")
        case .userCancellation:
            print("User canceled payment")
            StripeBridgeRegistry.shared.notifyFailure(message: error?.localizedDescription ?? "User canceled payment")

        @unknown default:
            break
        }
    }
}
