package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InspectionReportRequest(
    @SerialName("booking")
    val booking: String? = null,
    @SerialName("problem")
    val problem: String? = null,
    @SerialName("problem_description")
    val problemDescription: String? = null,
    @SerialName("problem_solution")
    val problemSolution: String? = null,
    @SerialName("estimation")
    val estimation: String? = null,
    @SerialName("available_date_time")
    val availableDateTime: String? = null
)
