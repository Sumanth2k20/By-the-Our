package com.bytheour.presentations.screens.tabs.servicesTab


import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalImageStore {
    private val _imagePreviews = MutableStateFlow<List<ImageBitmap>>(emptyList())
    val imagePreviews = _imagePreviews.asStateFlow()

    private val _pendingUploads = MutableStateFlow<List<ByteArray>>(emptyList())
    val pendingUploads = _pendingUploads.asStateFlow()

    fun addImagePreviews(images: List<ImageBitmap>) {
        _imagePreviews.value = _imagePreviews.value + images
    }

    fun addPendingUploads(bytes: List<ByteArray>) {
        _pendingUploads.value = _pendingUploads.value + bytes
    }

    fun removeImageAt(index: Int) {
        val previews = _imagePreviews.value.toMutableList()
        val uploads = _pendingUploads.value.toMutableList()

        if (index in previews.indices) previews.removeAt(index)
        if (index in uploads.indices) uploads.removeAt(index)

        _imagePreviews.value = previews
        _pendingUploads.value = uploads
    }

    fun clearAll() {
        _imagePreviews.value = emptyList()
        _pendingUploads.value = emptyList()
    }
}
