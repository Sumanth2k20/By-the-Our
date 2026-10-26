package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CancelReasonsResponse(
    @SerialName("data")
    var `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("id")
        var id: String = "",
        @SerialName("reason")
        var reason: String = "",
        @SerialName("isSelected")
        var isSelected : Boolean = false
    )

}