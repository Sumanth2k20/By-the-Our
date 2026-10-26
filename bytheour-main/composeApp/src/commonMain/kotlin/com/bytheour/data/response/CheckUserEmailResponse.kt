package com.bytheour.data.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckEmailResponse(
    val `data`: Data = Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("contact")
        val contact: Contact = Contact(),
        @SerialName("deleted")
        val deleted: Boolean = false,
        @SerialName("exists")
        val exists: Boolean = false
    ) {
        @Serializable
        data class Contact(
            @SerialName("iso")
            val iso: String = "",
            @SerialName("number")
            val number: String = ""
        )
    }
}