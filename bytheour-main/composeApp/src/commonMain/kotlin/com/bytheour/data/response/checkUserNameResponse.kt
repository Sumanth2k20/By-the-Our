package com.bytheour.data.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserNameResponse(
    val `data`: Data = Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("contact")
        val contact: Contact = Contact(),
        @SerialName("del")
        val deleted: Boolean = false, // false
        @SerialName("exists")
        val exists: Boolean = false // true
    ) {
        @Serializable
        data class Contact(
            @SerialName("iso")
            val iso: String = "", // IN
            @SerialName("number")
            val number: String = "" // 919625454962
        )
    }
}