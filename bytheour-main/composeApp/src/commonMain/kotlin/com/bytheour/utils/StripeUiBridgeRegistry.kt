package com.bytheour.utils

import com.bytheour.data.response.GetEphemeraKeyResponse
import kotlin.concurrent.Volatile

object StripeUiBridgeRegistry {
    @Volatile
    var bridge: StripeUiBridge? = null

    fun register(impl: StripeUiBridge) {
        bridge = impl
    }
}

interface StripeUiBridge {
    fun showStripeBillingScreen(ephemeralKey: GetEphemeraKeyResponse.Data)
}