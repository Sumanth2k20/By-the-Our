package com.bytheour.data.response

import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetEphemeraKeyResponse(
    @SerialName("data")
    var `data`: Data = Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("id")
        var id: String = "",
        @SerialName("object")
        var objec: String = "",
        @SerialName("associated_objects")
        var associatedObjects: List<AssociatedObjects> = emptyList(),
        @SerialName("created")
        var created: Long = 0,
        @SerialName("expires")
        var expires: Long = 0,
        @SerialName("livemode")
        var livemode: Boolean = false,
        @SerialName("secret")
        var secret: String = "",

        )

    @Serializable
    data class AssociatedObjects(
        @SerialName("id")
        var id: String = "",
        @SerialName("type")
        var type: String = "",
    )
}

