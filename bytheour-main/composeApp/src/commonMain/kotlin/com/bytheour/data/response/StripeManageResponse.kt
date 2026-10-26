package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankDetailsStripeManageResponse(
    @SerialName("data")
    val `data`: Data,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class Data(
        @SerialName("bank_account")
        val bankAccount: BankAccount? = null,
        @SerialName("capabilities")
        val capabilities: Capabilities? = null,
        @SerialName("charges_enabled")
        val chargesEnabled: Boolean = false, // true
        @SerialName("has_bank_account")
        val hasBankAccount: Boolean = false, // true
        @SerialName("id")
        val id: String? = null, //
        @SerialName("payouts_enabled")
        val payoutsEnabled: Boolean = false, // true
        @SerialName("status")
        val status: String? = null, // Active
        @SerialName("link")
        val link: String? = null, // https://connect.stripe.com/setup/e/acct_1LzZ5IPEgf2w0brj/YMRRlVZIZ5e8
        @SerialName("created_at")
        val createdAt: String? = null, // 2022-11-02T05:03:42.000000Z
        @SerialName("valid_till")
        val validTil: String? = null, // 2022-11-02T05:08:42.000000Z
        @SerialName("success_url")
        val successUrl: String? = null, // http://127.0.0.1:8000/stripe/webhook/account-link-redirection
    ) {
        @Serializable
        data class BankAccount(
            @SerialName("data")
            val dataBankAccount: List<DataBankAccount>,
            @SerialName("has_more")
            val hasMore: Boolean, // false
            @SerialName("object")
            val objectX: String, // list
            @SerialName("total_count")
            val totalCount: Int, // 1
            @SerialName("url")
            val url: String // /v1/accounts//external_accounts
        ) {
            @Serializable
            data class DataBankAccount(
                @SerialName("account")
                val account: String, //
                @SerialName("account_holder_name")
                val accountHolderName: String? = "", // null
                @SerialName("account_holder_type")
                val accountHolderType: String? = "", // null
                @SerialName("account_type")
                val accountType: String? = "", // null
                @SerialName("available_payout_methods")
                val availablePayoutMethods: List<String>,
                @SerialName("bank_name")
                val bankName: String? = "", // STRIPE TEST BANK
                @SerialName("country")
                val country: String, // US
                @SerialName("currency")
                val currency: String, // usd
                @SerialName("default_for_currency")
                val defaultForCurrency: Boolean, // true
                @SerialName("fingerprint")
                val fingerprint: String, // 45AIk6zKz3AoNvl3
                @SerialName("id")
                val id: String, // ba_1LzILmPHzq6W9isJPD6uwAlW
                @SerialName("last4")
                val last4: String, // 6789
                @SerialName("metadata")
                val metadata: List<String>,
                @SerialName("object")
                val objectX: String, // bank_account
                @SerialName("routing_number")
                private val routingNumberPrivate: String? = "", // 110000000
                @SerialName("status")
                val status: String // new
            ){
                val routeNumber: String
                    get() = if(routingNumberPrivate.isNullOrEmpty())
                        "N/A"
                    else
                        routingNumberPrivate

            }
        }

        @Serializable
        data class Capabilities(
            @SerialName("card_payments")
            val cardPayments: String, // active
            @SerialName("transfers")
            val transfers: String // active
        )
    }

    @Serializable
    data class Meta(
        @SerialName("api")
        val api: String, // v.1.0
        @SerialName("message")
        val message: String, // Account fetched Successfully
        @SerialName("url")
        val url: String // http://127.0.0.1:8000/api/worker/v1/stripe/connect/manage
    )
}