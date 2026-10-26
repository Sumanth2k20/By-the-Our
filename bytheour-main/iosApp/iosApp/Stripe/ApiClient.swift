//
//  ApiClient.swift
//  iosApp
//
//  Created by Mounika Sakinapally on 31/08/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import Stripe

//MARK: - Strip Key Provider Class (MyAPIClinet)
class MyAPIClient: NSObject, STPCustomerEphemeralKeyProvider {
    static let sharedClient = MyAPIClient()
    var userData:[AnyHashable : Any]?

    //Create Customer Key for Stripe Completion Block
    func createCustomerKey(withAPIVersion apiVersion: String, completion: @escaping STPJSONResponseCompletionBlock) {
        if let data = userData{
            completion(data, nil)
        }else{
            completion(nil, NSError(domain: "EphemeralKey", code: -1, userInfo: nil))
        }
    }

}
