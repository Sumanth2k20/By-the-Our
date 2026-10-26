package com.bytheour.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bytheour.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GoogleMaps(
    latitude: String,
    longitude: String,
    onLocationChange: (String, String) -> Unit,
    onMarkerClick: (String, String) -> Unit
){

    val latitude = latitude.toDoubleOrNull() ?: 0.0
    val longitude = longitude.toDoubleOrNull() ?: 0.0

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 15f)
    }

    var userLatLng by remember { mutableStateOf(LatLng(latitude, longitude)) }
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var permissionDeniedCount by remember { mutableIntStateOf(0) }

    var markerList by remember { mutableStateOf(listOf<LatLng>()) }
    var initialLocationHandled by remember { mutableStateOf(false) }
    var shouldFetchLocation by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (locationPermissionGranted && isGPSEnabled(context) &&
                    latitude == 0.0 && longitude == 0.0) {
                    shouldFetchLocation = true
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(latitude, longitude, locationPermissionGranted, shouldFetchLocation) {
        if (!initialLocationHandled || shouldFetchLocation) {
            if ((latitude == 0.0 && longitude == 0.0) && locationPermissionGranted) {
                if (isGPSEnabled(context)) {
                    fetchCurrentLocation(context) { latLng ->
                        userLatLng = latLng
                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        markerList = listOf(latLng)
                        initialLocationHandled = true
                        shouldFetchLocation = false
                        onLocationChange(latLng.latitude.toString(), latLng.longitude.toString())
                    }
                } else {
                    openLocationSettings(context)
                }
            } else if (latitude != 0.0 || longitude != 0.0) {
                val latLng = LatLng(latitude, longitude)
                userLatLng = latLng
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                markerList = listOf(latLng)
                initialLocationHandled = true
                shouldFetchLocation = false
            }
        } else {
            if (latitude != 0.0 || longitude != 0.0) {
                val latLng = LatLng(latitude, longitude)
                if (userLatLng != latLng) {
                    userLatLng = latLng
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    markerList = listOf(latLng)
                }
            }
        }
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    val markerState = remember { MarkerState(position = userLatLng) }

    LaunchedEffect(userLatLng) {
        markerState.position = userLatLng
    }



    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
        if (!isGranted) {
            permissionDeniedCount++
            if (permissionDeniedCount <= 2) {
                openAppSettings()
            }
        } else {
            if (!isGPSEnabled(context)) {
                openLocationSettings(context)
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(permission)
    }



    GoogleMap(
        modifier = Modifier.fillMaxHeight(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerList = (markerList + latLng).takeLast(1)
            userLatLng = latLng
            onLocationChange(latLng.latitude.toString(), latLng.longitude.toString())
            cameraPositionState.move(CameraUpdateFactory.newLatLng(latLng))
        }
    ) {
        val customMarkerIcon = remember(context) {
            try {
                bitmapDescriptorFromVector(context, R.drawable.marker)
            } catch (e: Exception) {
                null
            }
        }

        if (customMarkerIcon != null) {
            markerList.forEachIndexed { index, latLng ->
                Marker(
                    icon = customMarkerIcon,
                    state = MarkerState(position = latLng),
                    title = "Marker ${index + 1}",
                    snippet = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}",
                    onInfoWindowClick = {
                        onMarkerClick(latLng.latitude.toString(), latLng.longitude.toString())
                    }
                )
            }
        }
    }
}

private fun isGPSEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

@SuppressLint("MissingPermission")
private fun fetchCurrentLocation(
    context: Context,
    onLocationFetched: (LatLng) -> Unit
) {
    // Double check GPS is enabled before fetching
    if (!isGPSEnabled(context)) {
        openLocationSettings(context)
        return
    }

    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationFetched(LatLng(location.latitude, location.longitude))
        } else {
            // If lastLocation is null, try to request a fresh location
            requestFreshLocation(fusedLocationProviderClient, onLocationFetched)
        }
    }.addOnFailureListener {
        onLocationFetched(LatLng(0.0, 0.0))
    }
}

@SuppressLint("MissingPermission")
private fun requestFreshLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    onLocationFetched: (LatLng) -> Unit
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        10000L
    ).build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                onLocationFetched(LatLng(location.latitude, location.longitude))
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }
    }

    fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}


fun bitmapDescriptorFromVector(context: Context, @DrawableRes resId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, resId)!!
    val bitmap = createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}