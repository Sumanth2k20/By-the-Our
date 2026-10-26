package com.bytheour.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun getFormattedDistanceInMiles(
    lat1: Double?,
    lon1: Double?,
    lat2: Double?,
    lon2: Double?,
    decimals: Int = 2
): String? {
    if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) return null

    val earthRadiusMiles = 3958.8

    val dLat = (lat2 - lat1).toRadians()
    val dLon = (lon2 - lon1).toRadians()

    val a = sin(dLat / 2).pow(2.0) +
            cos(lat1.toRadians()) * cos(lat2.toRadians()) *
            sin(dLon / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val miles = earthRadiusMiles * c

    return miles.roundToDecimals(decimals)
}

private fun Double.toRadians(): Double = this * (kotlin.math.PI / 180.0)

private fun Double.roundToDecimals(decimals: Int): String {
    if (decimals < 0) return this.toString()
    val factor = 10.0.pow(decimals)
    val rounded = (this * factor).toLong().toDouble() / factor
    return rounded.toString()
}