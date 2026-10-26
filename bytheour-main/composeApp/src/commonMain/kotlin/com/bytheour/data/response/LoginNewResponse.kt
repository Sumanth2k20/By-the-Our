package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginNewResponse(
    @SerialName("data")
    var `data`: Data = Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("contact")
        var contact: Contact = Contact(),
        @SerialName("email")
        var email: String = "",
        @SerialName("username")
        var username: String = "",
        @SerialName("experience")
        var experience: Double = 0.0,
        @SerialName("first_name")
        var firstName: String = "",
        @SerialName("flags")
        var flags: Flags = Flags(),
        @SerialName("rejection")
        var rejection: Rejection = Rejection(),
        @SerialName("id")
        var id: String = "",
        @SerialName("last_name")
        var lastName: String = "",
        @SerialName("profile")
        var profile: String = "",
        @SerialName("portfolio")
        var portfolio: Portfolio = Portfolio(),
        @SerialName("radius")
        var radius: Float = 3f,
        @SerialName("wallet")
        var wallet: String = "",
        @SerialName("code")
        var code: String = "",
        @SerialName("about")
        var about: String = "",
        @SerialName("tax_classification")
        var taxClassification: TaxClassification? = null
    )

    @Serializable
    data class Rejection(
        @SerialName("comment")
        var comment: String = "",
        @SerialName("status")
        var status: String = ""
    )

    @Serializable
    data class Portfolio(
        @SerialName("ratings")
        var ratings: Float? = null,
        @SerialName("type")
        var type: String? = null
    )

    @Serializable
    data class TaxClassification(
        @SerialName("id")
        var id: String = "",
        @SerialName("type")
        var type: String = "",
        @SerialName("classification_number")
        var classificationNumber: String = ""
    )

    @Serializable
    data class Contact(
        @SerialName("code")
        var code: String = "",
        @SerialName("number")
        var number: String = ""
    )

    @Serializable
    data class Flags(
        @SerialName("notification")
        var notification: Boolean = false,
        @SerialName("password")
        var password: Boolean = false,
        @SerialName("status")
        var status: Boolean = false,
        @SerialName("documents")
        var documents: Boolean = false,
        @SerialName("videos")
        var videos: Boolean = false,
        @SerialName("address")
        var address: Boolean = false,
        @SerialName("tax_details")
        var taxDetails: Boolean = false,
        @SerialName("is_admin")
        var IsAdmin: Boolean = false,
        @SerialName("profile_incomplete")
        var profileIncomplete: Boolean = false,
        @SerialName("linked")
        var linked: Boolean = false,
        @SerialName("verification_screen")
        var verificationScreen: Boolean = false,
        @SerialName("verified")
        var verified: String? = "",
        @SerialName("verified_at")
        var verifiedAt: String = ""
    )

}