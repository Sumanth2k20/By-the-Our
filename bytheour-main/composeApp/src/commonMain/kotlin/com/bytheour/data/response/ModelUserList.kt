package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.Transient

@Serializable
data class ModelUserList(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("meta")
    val meta: Meta?,
)

@Serializable
data class Data(
    @SerialName("records")
    val records: List<Record?>? =null,
    @SerialName("total")
    val total: Int?=null,
    @SerialName("categories")
    val categories: List<Category?>? =null,
    @SerialName("bookings")
    val bookings: Bookings? =null,
    @SerialName("reviews")
    val reviews: Reviews? =null,
    @SerialName("android_downloads")
    val androidDownloads: String? =null,
    @SerialName("ios_downloads")
    val iosDownloads: String? =null,
    @SerialName("users_count")
    val usersCount: String? =null,
    @SerialName("workers_count")
    val workersCount: String? =null,
    @SerialName("booking_count")
    val bookingCount: String? =null,
    @SerialName("download_count")
    val downloadCount: String? =null,
    @SerialName("services")
    val services: List<Service?>? = null,
    @SerialName("documents")
    val documents: List<Document?>? = null,
    @SerialName("wallet_amount")
    val walletAmount: Double? = null,
    @SerialName("no_of_services")
    val noOfServices: Int? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("verification_status")
    val verificationStatus: String? =null,
    @SerialName("rejection_status")
    val rejectionStatus: String? =null,
    @SerialName("rejection_reason")
    val rejectionReason: String? =null,
)

@Serializable
data class Document(
    @SerialName("name")
    val documentName: String? = null,
    @SerialName("path")
    val documentUrl: String? = null
)

@Serializable
data class Service(
    @SerialName("title")
    val title: String?,
    @SerialName("category")
    val category: String?,
    @SerialName("rate")
    val rate: Int?,
    @SerialName("min_hour")
    val minHour: Int?
)

@Serializable
data class Record(
    @SerialName("contact_number")
    val contactNumber: String?=null,
    @SerialName("document_status")
    val documentStatus: String?=null,
    @SerialName("type")
    val type: String?=null,
    @SerialName("registered_date")
    val registeredDate: String?=null,
    @SerialName("dType")
    val dType: String?=null,
    @SerialName("email")
    val email: String?=null,
    @SerialName("first_name")
    val firstName: String?=null,
    @SerialName("id")
    val id: Int?=null,
    @SerialName("is_active")
    var isActive: String?=null,
    @SerialName("last_name")
    val lastName: String?=null,
    @SerialName("profile")
    val profile: String?=null,

    @SerialName("category")
    val category: String?=null,
    @SerialName("category_id")
    val categoryId: Int?=null,
    @SerialName("description")
    val description: String?=null,
    @SerialName("image")
    val image: String?=null,
    @SerialName("sequence")
    val sequence: Int?=null,
    @SerialName("service_status")
    val serviceStatus: String?=null,
    @SerialName("title")
    val title: String?=null,
    @SerialName("worker_name")
    val workerName: String?=null,
    @Transient
    var isSelected: Boolean = false

)

@Serializable
data class Category(
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("custom_id")
    val customId: String?,
    @SerialName("deleted_at")
    val deletedAt: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("image")
    val image: String?,
    @SerialName("is_active")
    val isActive: String?,
    @SerialName("sequence")
    val sequence: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("updated_at")
    val updatedAt: String?
)


@Serializable
data class Bookings(
    @SerialName("completed")
    val completed: Int?,
    @SerialName("ongoing")
    val ongoing: Int?,
    @SerialName("upcoming")
    val upcoming: Int?
)

@Serializable
data class Reviews(
    @SerialName("count")
    val count: Int?,
    @SerialName("average")
    val average: String?="0",
)