package com.bytheour.core

sealed class JtApiResult<out T> {
    data class Success<T>(val data: T) : JtApiResult<T>()
    data class Failure(val error: JtAppError) : JtApiResult<Nothing>()
}