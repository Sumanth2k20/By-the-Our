package com.bytheour.data.response

import kotlinx.serialization.Serializable


@Serializable
data class PlacesAutocompleteResponse(
    val predictions: List<Prediction>?,
    val status: String
)

@Serializable
data class Prediction(
    val description: String?,
    val place_id: String?,
    val structured_formatting: StructuredFormatting?
)

@Serializable
data class StructuredFormatting(
    val main_text: String?,
    val secondary_text: String?
)

@Serializable
data class PlaceDetailsResponse(
    val result: PlaceDetails?,
    val status: String
)

@Serializable
data class PlaceDetails(
    val geometry: Geometry?,
    val formatted_address: String?
)

@Serializable
data class Geometry(
    val location: Location?
)

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)
