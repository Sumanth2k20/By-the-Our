package com.bytheour.data.model.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelAddImage(
    @SerialName("data")
    val data : String?=null,
    @SerialName("meta")
    val meta : Meta?=null
)
