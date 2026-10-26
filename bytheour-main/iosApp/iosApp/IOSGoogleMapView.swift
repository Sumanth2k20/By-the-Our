//
//  IOSGoogleMapView.swift
//  iosApp
//
//  Created by Japp Tech on 19/08/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import GoogleMaps
import CoreLocation

struct IOSGoogleMapView: UIViewRepresentable {
    var latitude: String
    var longitude: String
    var onLocationChange: (String, String) -> Void
    var onMarkerClick: (String, String) -> Void

    func makeUIView(context: Context) -> GMSMapView {
        let mapView = GMSMapView(frame: .zero)
        mapView.delegate = context.coordinator
        mapView.settings.myLocationButton = false
        mapView.isMyLocationEnabled = false // do not auto-overwrite with GPS

        // initialize camera and marker from Kotlin-provided coords
        if let lat = Double(latitude), let lng = Double(longitude), lat != 0.0 || lng != 0.0 {
            let camera = GMSCameraPosition.camera(withLatitude: lat, longitude: lng, zoom: 15)
            mapView.camera = camera
            context.coordinator.setMarker(at: CLLocationCoordinate2D(latitude: lat, longitude: lng), on: mapView)
        }

        return mapView
    }

    func updateUIView(_ mapView: GMSMapView, context: Context) {
        // Apply new coords coming from Kotlin only if they're different from current camera
        if let lat = Double(latitude), let lng = Double(longitude) {
            let current = mapView.camera.target
            // small epsilon to avoid tiny differences causing re-centering
            if abs(current.latitude - lat) > 0.0001 || abs(current.longitude - lng) > 0.0001 {
                let camera = GMSCameraPosition.camera(withLatitude: lat, longitude: lng, zoom: 15)
                mapView.animate(to: camera)
                context.coordinator.setMarker(at: CLLocationCoordinate2D(latitude: lat, longitude: lng), on: mapView)
            }
        }
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(parent: self, onLocationChange: onLocationChange, onMarkerClick: onMarkerClick)
    }

    class Coordinator: NSObject, GMSMapViewDelegate {
        var parent: IOSGoogleMapView
        var marker: GMSMarker = GMSMarker()
        private let onLocationChange: (String, String) -> Void
        private let onMarkerClick: (String, String) -> Void

        init(parent: IOSGoogleMapView,
             onLocationChange: @escaping (String, String) -> Void,
             onMarkerClick: @escaping (String, String) -> Void) {
            self.parent = parent
            self.onLocationChange = onLocationChange
            self.onMarkerClick = onMarkerClick
            super.init()
        }

        func setMarker(at coordinate: CLLocationCoordinate2D, on mapView: GMSMapView) {
            // replace marker
            marker.map = nil
            marker = GMSMarker(position: coordinate)
            marker.map = mapView
        }

        // user tapped on map -> move marker, inform Kotlin
        func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
            setMarker(at: coordinate, on: mapView)
            onLocationChange(String(coordinate.latitude), String(coordinate.longitude))
        }

        // user tapped marker -> inform Kotlin
        func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
            onMarkerClick(String(marker.position.latitude), String(marker.position.longitude))
            return true
        }
    }
}

