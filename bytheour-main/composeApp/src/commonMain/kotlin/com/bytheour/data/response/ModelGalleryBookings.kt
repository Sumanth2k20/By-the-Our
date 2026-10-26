package com.bytheour.data.model.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelGalleryBookings(
    @SerialName("data")
    val `data`: List<ModelGallery?> = emptyList(),
    @SerialName("meta")
    val meta: Meta?
)

@Serializable
data class ModelGallery(
    @SerialName("amount")
    val amount: Int?,
    @SerialName("custom_id")
    val customId: String?,
    @SerialName("end_date")
    val endDate: String?,
    @SerialName("gallery_plan_id")
    val galleryPlanId: Int?,
    @SerialName("is_unlimited")
    val isUnlimited: String?,
    @SerialName("no_of_pictures")
    val noOfPictures: Int?,
    @SerialName("no_of_pictures_used")
    val noOfPicturesUsed: Int?,
    @SerialName("start_date")
    val startDate: String?
)