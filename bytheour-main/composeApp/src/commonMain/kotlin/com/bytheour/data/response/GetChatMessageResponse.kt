package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class GetChatMessageHistoryResponse(
    @SerialName("data")
    var `data`: ArrayList<Message> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("id")
        var id: String? = "",
        @SerialName("message")
        var message: String = "",
        @SerialName("sender")
        var sender: String = "",
        @SerialName("type")
        var type: String = ""
    )

}



@Serializable
data class Message(
    @SerialName("message")
    var message: String? = "",
    @SerialName("sender")
    var sender: String? = "",
    @SerialName("support")
    var support: Boolean? = false,
//    @SerialName("sender_type")
    var senderType: String? = "",
    @SerialName("room")
    var room: String? = "",
    @SerialName("from")
    var from: String? = "",
    @SerialName("type")
    var type: String? = "",
    @SerialName("user")
    var user: Boolean? = false,
    @SerialName("time")
    var time: String? = "",
    @SerialName("profile")
    var profile: String? = "",
    @SerialName("showTime")
    var showTime: String? = "",
    @SerialName("created")
    var created: String? = "",
    @SerialName("default_msg")
    var default_msg: String? = "",
    var header: String = ""
)

internal fun parseMessageFromJson(json: String): Message {
    val el = Json.parseToJsonElement(json)
    val obj = el.jsonObject

    fun jp(name: String) = obj[name]?.jsonPrimitive

    val showTime1 = jp("show_time")?.contentOrNull
    val showTime2 = jp("showTime")?.contentOrNull

    val m = Message(
        user = jp("user")?.booleanOrNull,
        profile = jp("profile")?.contentOrNull,
        type = jp("type")?.contentOrNull,
        message = jp("message")?.contentOrNull,
        from = jp("from")?.contentOrNull ?: "",
        default_msg = jp("default_msg")?.contentOrNull ?: "",
        time = jp("time")?.contentOrNull,
        showTime = showTime1 ?: showTime2,
        senderType = jp("senderType")?.contentOrNull
    )

    m.sender = when {
        m.senderType == "admin" -> ""
        m.user == true -> "user"
        else -> "worker"
    }
    return m
}