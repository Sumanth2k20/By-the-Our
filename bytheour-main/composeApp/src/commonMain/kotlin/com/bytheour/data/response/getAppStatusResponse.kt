package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAppStatusResponse(
    @SerialName("data")
    var `data`: Data? = null,
    @SerialName("meta")
    var meta: Meta? = null
) {
    @Serializable
    data class Data(
        @SerialName("calculation")
        var calculation: Calculation = Calculation(),
        @SerialName("updates")
        var updates: Updates? = null,
        @SerialName("version")
        var version: Version? = null,
        @SerialName("links")
        var links: Links = Links(),
        @SerialName("client_email")
        var clientEmail: String? = null,
        @SerialName("welcome_banner")
        var welcomeBanner: String? = null,
        @SerialName("worker_radius")
        var workerRadius: WorkerRadius = WorkerRadius(),
    )

    @Serializable
    data class WorkerRadius(
        @SerialName("default")
        var default: Float = 10.0F,
        @SerialName("min")
        var min: Float = 3.0F,
        @SerialName("max")
        var max: Float = 20.0F,
    )

    @Serializable
    data class Calculation(
        @SerialName("commission")
        var commission: Commission = Commission(),
        @SerialName("vat")
        var vat: String = "",
        @SerialName("withdrawal")
        var withdrawal: Withdrawal? = null
    )

    @Serializable
    data class Commission(
        @SerialName("customer")
        var customer: String = "",
        @SerialName("service_provider")
        var serviceProvider: String = ""
    )


    @Serializable
    data class Updates(
        @SerialName("countries")
        var serverCountriesUpdates: String? = null,
        @SerialName("services")
        var serverServicesUpdate: String? = null,
        @SerialName("reasons")
        var serverReasonsUpdate: String? = null,
        @SerialName("faqs")
        var serverFaqsUpdate: String? = null
    )

    @Serializable
    data class AppUpdates(
        var appCountriesUpdates: String? = null,
        var appServicesUpdate: String? = null,
        var appReasonsUpdate: String? = null
    )

    @Serializable
    data class Version(
        @SerialName("android")
        var android: Android? = null,
        @SerialName("ios")
        var ios: Ios? = null
    ) {
        @Serializable
        data class Android(
            @SerialName("latest")
            var latest: String? = null,
            @SerialName("minimum")
            var minimum: String? = null
        )

        @Serializable
        data class Ios(
            @SerialName("latest")
            var latest: String? = null,
            @SerialName("minimum")
            var minimum: String? = null
        )

    }

    @Serializable
    data class Withdrawal(
        @SerialName("min")
        var min: Int = 0,
        @SerialName("max")
        var max: Int = -1
    )

    @Serializable
    data class Links(
        @SerialName("about")
        var about: About = About(),
        @SerialName("faqs")
        var faqs: Faqs = Faqs(),
        @SerialName("privacy")
        var privacy: Privacy = Privacy(),
        @SerialName("storage")
        var storage: String = "",
        @SerialName("terms")
        var terms: Terms = Terms()
    ) {
        @Serializable
        data class About(
            @SerialName("en")
            var en: String = ""
        )

        @Serializable
        data class Faqs(
            @SerialName("en")
            var en: String = ""
        )

        @Serializable
        data class Privacy(
            @SerialName("en")
            var en: String = ""
        )

        @Serializable
        data class Terms(
            @SerialName("en")
            var en: String = ""
        )
    }
}