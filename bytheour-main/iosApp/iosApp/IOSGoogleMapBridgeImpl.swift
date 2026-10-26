//
//  IOSGoogleMapBridgeImpl.swift
//  iosApp
//
//  Created by Japp Tech on 19/08/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import SwiftUI
import GoogleMaps
import ComposeApp

final class IOSGoogleMapBridgeImpl: NSObject, IosGoogleMapBridge {
    func createMapView(
        latitude: String,
        longitude: String,
        onLocationChange: @escaping (String, String) -> Void,
        onMarkerClick: @escaping (String, String) -> Void
    ) -> Any {
        let swiftUIView = IOSGoogleMapView(
            latitude: latitude,
            longitude: longitude,
            onLocationChange: onLocationChange,
            onMarkerClick: onMarkerClick
        )
        let controller = UIHostingController(rootView: swiftUIView)
        return controller.view!
    }
}


