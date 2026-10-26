package com.bytheour.data.model.response


import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelDocList(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("approved")
        val approved: Boolean = false, // false
        @SerialName("file")
        val `file`: String = "", // https://bythehour.s3.ap-south-1.amazonaws.com/workers/BHUC2tZeov8pPUbNGR8w/documents/1624534907.jpeg
        @SerialName("id")
        val id: String = "", //
        @SerialName("name")
        val name: String = "", // IMG-566192734711880531 2.jpeg
        @SerialName("reply")
        val reply: String = "",
        var localFile: String = ""
    )
}