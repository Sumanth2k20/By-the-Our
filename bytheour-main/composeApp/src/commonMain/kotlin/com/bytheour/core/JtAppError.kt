package com.bytheour.core

sealed class JtAppError(val message: String) {
    object Network : JtAppError("Please check your internet connection.")
    object Serialization : JtAppError("Response format issue.")
    data class Custom(val msg: String) : JtAppError(msg)
    object Unknown : JtAppError("Something went wrong.")
}