package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue

@Serializable
data class
ProviderDashBoardResponse(
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("amount")
        val amount: Amount = Amount(),
        @SerialName("bookings")
        val bookings: Bookings = Bookings(),
        @SerialName("earnings")
        val earnings: Earnings = Earnings(),
        @SerialName("verification_status")
        var verificationStatus: String? = null,
    ) {
        @Serializable
        data class Amount(
            @SerialName("wallet")
            val wallet: String = "", // 0
            @SerialName("withdrawal")
            val withdrawal: String = "", // 0
            @SerialName("processing")
            val processing: String = "" // 0
        ) {
            val strWithdrawal: String
                get() {
                    return if (withdrawal.isNotEmpty()) {
                        try {
                            "${withdrawal.toDouble().absoluteValue}"
                        } catch (e: Exception) {
                            withdrawal
                        }
                    } else
                        "0.00"
                }
            val strWallet: String
                get() {
                    return if (wallet.isNotEmpty()) {
                        try {
                            "${wallet.toDouble().absoluteValue}"
                        } catch (e: Exception) {
                            wallet
                        }
                    } else
                        "0.00"
                }
        }

        @Serializable
        data class Bookings(
            @SerialName("assigned_leads")
            val assignedLeads: Int = 0, // 0
            @SerialName("completed_leads")
            val completedLeads: Int = 0, // 0
            @SerialName("new_leads")
            val newLeads: Int = 0, // 0
            @SerialName("total_leads")
            val totalLeads: Int = 0 // 0
        )

        @Serializable
        data class Earnings(
            @SerialName("total")
            val total: Double? = 0.0, // 0
            @SerialName("week")
            val week: Double? = 0.0// 0
        ){
            val absTotal: Double
                get() = total?.absoluteValue ?:0.0
            val absWeek: Double
                get() = week?.absoluteValue ?:0.0
        }
    }
}