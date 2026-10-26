package com.bytheour.core

import com.bytheour.presentations.navigations.navigateToLoginScreen
import com.bytheour.useCases.SharedPreferenceUseCase
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend inline fun <reified T> safeApiCall(
    client: HttpClient,
    crossinline block: suspend HttpClient.() -> HttpResponse
): JtApiResult<T> {
    return try {
        val response = client.block()
        val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())


        if ((response.request.url.toString().contains(JTURLS.loginUserLogin()) ||
                    response.request.url.toString().contains(JTURLS.register()) ||
                    response.request.url.toString().contains(JTURLS.SWITCH_ACCOUNT()))) {
            val authToken = response.headers["authorization_token"]
            if (!authToken.isNullOrEmpty()) {
                println("authToken---------------------$authToken")
                sharedPreferenceUseCase.putToken(authToken)
            }
        }



        val responseText = response.bodyAsText()
        val json = Json.parseToJsonElement(responseText)
        val message = json.jsonObject["meta"]?.jsonObject?.get("message")?.jsonPrimitive?.contentOrNull

        if (message?.contains("session has expired", ignoreCase = true) == true) {
            navigateToLoginScreen?.invoke("Session expired")
            return JtApiResult.Failure(JtAppError.Custom("Session expired. Please log in again."))
        }



        val body: T = response.body()
        JtApiResult.Success(body)
    } catch (e: Exception) {
        handleError(e)
    }
}

suspend inline fun <reified T> safeApiCallFormData(
    client: HttpClient,
    crossinline block: suspend HttpClient.() -> HttpResponse
): JtApiResult<T> {
    return try {
        val response = client.block()

        val responseText = response.bodyAsText()
        val json = Json.parseToJsonElement(responseText)
        val message = json.jsonObject["meta"]?.jsonObject?.get("message")?.jsonPrimitive?.contentOrNull

        if (message?.contains("session has expired", ignoreCase = true) == true) {
            navigateToLoginScreen?.invoke("Session expired")
            return JtApiResult.Failure(JtAppError.Custom("Session expired. Please log in again."))
        }
        JtApiResult.Success(response.body())
    } catch (e: Exception) {
        handleError(e)
    }
}

inline fun <T> handleError(e: Exception): JtApiResult<T> {
    return when (e) {
        is ClientRequestException -> JtApiResult.Failure(JtAppError.Custom("Client error: ${e.response.status}"))
        is ServerResponseException -> JtApiResult.Failure(JtAppError.Custom("Server error: ${e.response.status}"))
        is IOException ->JtApiResult.Failure(JtAppError.Network)
        is SerializationException -> JtApiResult.Failure(JtAppError.Serialization)
        is NoTransformationFoundException -> JtApiResult.Failure(JtAppError.Custom("No transformation found"))
        else ->  JtApiResult.Failure(JtAppError.Unknown)
    }
}