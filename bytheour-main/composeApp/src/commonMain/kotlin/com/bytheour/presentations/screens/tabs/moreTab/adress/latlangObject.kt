package com.bytheour.presentations.screens.tabs.moreTab.adress

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object LatLngObject {

    private val _latitude = MutableStateFlow("")
    val latitude = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow("")
    val longitude = _longitude.asStateFlow()

    fun update(lat: String, lng: String) {
        _latitude.value = lat
        _longitude.value = lng
    }

    fun clear() {
        _latitude.value = ""
        _longitude.value = ""
    }

    private val _fromSign = MutableStateFlow("")
    val fromSign = _fromSign.asStateFlow()

    private val _fromDialog = MutableStateFlow("")
    val fromDialog = _fromDialog.asStateFlow()


    fun updateFromSign(sign: String) {
        _fromSign.value = sign
    }

    fun updateFromDialog(dialog: String) {
        _fromDialog.value = dialog
    }

    fun clearFromSign() {
        _fromSign.value = ""
    }

    fun clearFromDialog() {
        _fromDialog.value = ""
    }

}
