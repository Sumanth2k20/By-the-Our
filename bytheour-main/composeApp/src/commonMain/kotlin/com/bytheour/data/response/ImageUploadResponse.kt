package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelDocUrl(
    val data: Data = Data(),
    val meta: Meta = Meta(),
    var docName: String = "",

    ) {
    @Serializable
    data class Data(
        @SerialName("exipry_time")
        val exipryTime: String = "", // +10 minutes
        val filename: String = "", // workers/UUuRb4QckUR4QoA9SImY/documents/1623316448.png
        val timestamp: Int = 0, // 1623316448
        val url: String = "" // https://bythehour.s3.ap-south-1.amazonaws.com/workers/UUuRb4QckUR4QoA9SImY/documents/1623316448.png?x-amz-meta-user-id=UUuRb4QckUR4QoA9SImY&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAYYLWUSOENSCVDYXR%2F20210610%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Date=20210610T091408Z&X-Amz-SignedHeaders=host%3Bx-amz-meta-user-id&X-Amz-Expires=600&X-Amz-Signature=172f4823e7b184ac9f6322f032d3fc95c98f2a5605f805a08cfd974c38275231
    )
}


@Serializable
data class ModelInviteCode(
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("code")
        val code: String = "", // 7x795QTM
        @SerialName("link")
        val link: String = "", // https://bythehour.page.link/ovfB
        @SerialName("text")
        val text: String = "" // Link to get on board on ByTheHour 🎉 and make money 💰 💸
    )
}