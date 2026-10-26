package com.bytheour.data.model.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelGalleryPlan(
    @SerialName("GalleryPlans")
    val galleryPlans: List<GalleryPlan?>,
    @SerialName("meta")
    val meta: Meta?
)
@Serializable
data class GalleryPlan(
    @SerialName("active")
    val active: String? =null,
    @SerialName("amount")
    val amount: Int? =null,
    @SerialName("created_at")
    val createdAt: String? =null,
    @SerialName("custom_id")
    val customId: String? =null,
    @SerialName("deleted_at")
    val deletedAt: String? =null,
    @SerialName("id")
    val id: Int? =null,
    @SerialName("name")
    val name: String? =null,
    @SerialName("pictures")
    val pictures: Int? =null,
    @SerialName("updated_at")
    val updatedAt: String? =null,
    var hour : Int = 0
)
