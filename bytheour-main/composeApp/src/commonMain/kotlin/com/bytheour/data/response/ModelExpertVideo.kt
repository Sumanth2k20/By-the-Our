package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelExpertVideo(
    @SerialName("data")
    var `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("file")
        var `file`: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("poster")
        var poster: String = "",
        @SerialName("title")
        var title: String = ""
    )

}
