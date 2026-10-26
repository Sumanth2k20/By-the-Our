package com.bytheour.data.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediationGuidelineResponse(
    @SerialName("MediationGuidelines")
    val mediationGuidelines: List<MediationGuidelineItem> = emptyList(),
    @SerialName("meta")
    val meta: Meta? = null
)

@Serializable
data class MediationGuidelineItem(
    @SerialName("Num")
    val num: Int? = null,
    @SerialName("Title")
    val title: String? = null,
    @SerialName("Description")
    val description: String? = null
)

