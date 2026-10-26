package com.bytheour.presentations.screens.login

import korlibs.crypto.AES
import korlibs.crypto.HMAC
import korlibs.crypto.Padding
import korlibs.crypto.encoding.base64
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock
import kotlin.random.Random

@Serializable
data class CheckSumPayload(val iv: String, val value: String, val mac: String)

class CheckSumUtil {
    private var iv: String? = null
    private val checkSumKey: String = "bkpCRW9tcHdQV2V0ODRrRU1QTlZ4MjJG"

    fun generateChecksum(contact: String): String {
        iv = generateRandomIv()
        println("RANDOM IV: $iv")

        val jsonString = getJsonString(contact)

        val encrypted = encrypt(jsonString, checkSumKey, iv!!)


        val macMessage = "" + encrypted
        val mac = generateHMAC256(macMessage, checkSumKey)

        val payload = CheckSumPayload(
            iv = iv!!.encodeToByteArray().base64,
            value = encrypted,
            mac = mac
        )

        val json = Json.encodeToString(payload)
        val checkSum = json.encodeToByteArray().base64

        println("CHECKSUM:--------------------------- $checkSum")
        return checkSum
    }

    private fun getJsonString(contact: String): String {
        val time = Clock.System.now().toString() // ISO-8601 format
        val json = mapOf("contact_number" to contact, "time" to time)
        return Json.encodeToString(json)
    }

    private fun generateRandomIv(): String {
        return Random.nextLong(1000000000000000, 9999999999999999).toString()
    }

    private fun encrypt(plainText: String, key: String, iv: String): String {
        val keyBytes = key.encodeToByteArray().copyOf(32) // AES-256 key
        val ivBytes = iv.encodeToByteArray().copyOf(16)   // 16-byte IV
        val input = plainText.encodeToByteArray()

        val encrypted = AES.encryptAesCbc(input, keyBytes, ivBytes, padding = Padding.PKCS7Padding)
        return encrypted.base64
    }



    private fun generateHMAC256(data: String, key: String): String {
        val result = HMAC.hmacSHA256(key.encodeToByteArray(), data.encodeToByteArray())
        return result.hexLower
    }
}
