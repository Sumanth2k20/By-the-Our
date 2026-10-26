package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelSwitchStatus(
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("account")
        val account: Account = Account(),
        @SerialName("verification")
        val verification: Verification = Verification()
    ) {
        @Serializable
        data class Account(
            @SerialName("active")
            val active: Boolean = false, // false
            @SerialName("contact")
            val contact: Contact = Contact(),
            @SerialName("deleted")
            val deleted: Boolean = false, // false
            @SerialName("email")
            val email: String = "",
            @SerialName("exists")
            val exists: Boolean = false, // false
            @SerialName("first_name")
            val firstName: String = "",
            @SerialName("last_name")
            val lastName: String = ""
        ) {
            @Serializable
            data class Contact(
                @SerialName("code")
                val code: String = "",
                @SerialName("number")
                val number: String = ""
            )
        }

        @Serializable
        data class Verification(
            @SerialName("email")
            val email: Boolean = false, // false
            @SerialName("number")
            val number: Boolean = false // false
        )
    }

    @Serializable
    data class Meta(
        @SerialName("api")
        val api: String = "", // v.1.0
        @SerialName("message")
        val message: String = "",
        @SerialName("url")
        val url: String = "" // http://la.webdevprojects.cloud/by-the-hour/api/user/v1/switch-account/status
    )
}