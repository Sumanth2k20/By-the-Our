package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.model.response.ModelGallery
import com.bytheour.data.model.response.ServerImages
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.GalleryUseCases
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.utils.FileUpload
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageUploadViewModel(
    private val providerBookingUseCases: ProviderBookingUseCases,
    private val galleryUseCases: GalleryUseCases,
    commonUseCases: CommonUseCases,
) : ViewModel() {

    private val fileUploadUseCase = FileUpload(commonUseCases)

    data class UiState(
        val uploadedImages: List<ServerImages> = emptyList(),
        val showImagePicker: Boolean = false,
        val message: String? = null,
        val isUploading: Boolean = false,
        val galleryImages: ArrayList<ServerImages>? = null,
        val galleryBookings: List<ModelGallery?>? = null,
        val maxPhotos: Int? = null,
        val usedPhotos: Int? = null,
        val isUnlimited: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiStates = _uiState.asStateFlow()

    fun onUploadClick() {
        val current = _uiState.value
        val remaining = (current.maxPhotos ?: Int.MAX_VALUE) - (current.usedPhotos ?: 0)
        if (!current.isUnlimited && remaining <= 0) {
            _uiState.update { it.copy(message = "Upload limit reached. Please upgrade your plan.") }
            return
        }
        _uiState.update { it.copy(showImagePicker = true) }
    }


    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
    fun onDismissImagePicker() {
        _uiState.update { it.copy(showImagePicker = false) }
    }

    fun fetchGalleryBookings() {
        viewModelScope.launch {
            when (val result = galleryUseCases.getGalleryBookings()) {
                is JtApiResult.Success -> {
                    val bookings = result.data.data
                    val isUnlimited = bookings.any { it?.isUnlimited == "y" }
                    val totalMaxPhotos = if (isUnlimited) {
                        null
                    } else {
                        bookings.sumOf { it?.noOfPictures ?: 0 }
                    }

                    val totalUsedPhotos = bookings.sumOf { it?.noOfPicturesUsed ?: 0 }

                    _uiState.update {
                        it.copy(
                            galleryBookings = bookings,
                            maxPhotos = totalMaxPhotos,
                            usedPhotos = totalUsedPhotos,
                            isUnlimited = isUnlimited
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update { it.copy(message = result.error.message) }
                }
            }
        }
    }


    fun onImageSelected(images: List<ImageBitmap>?) {
        images?.let { list ->
            val current = _uiState.value
            val remaining = (current.maxPhotos ?: Int.MAX_VALUE) - (current.usedPhotos ?: 0)
            val filtered = if (current.isUnlimited) list else list.take(remaining.coerceAtLeast(0))
            if (filtered.isEmpty()) {
                _uiState.update { it.copy(message = "Upload limit reached.") }
                return
            }
            GlobalImageStore.addImagePreviews(filtered)
            _uiState.update { it.copy(showImagePicker = false) }
        }
    }

    fun addPendingImages(bytes: List<ByteArray?>) {
        val current = _uiState.value
        val remaining = (current.maxPhotos ?: Int.MAX_VALUE) - (current.usedPhotos ?: 0)
        val filtered = if (current.isUnlimited) bytes.filterNotNull() else bytes.filterNotNull().take(remaining.coerceAtLeast(0))
        GlobalImageStore.addPendingUploads(filtered)
    }

    fun saveAllImages(
        serviceId: String,
        onSuccess: () -> Unit,
        uploads: List<ByteArray> = GlobalImageStore.pendingUploads.value
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }

            val newUploadedUrls = mutableListOf<String>()
            for (bytes in uploads) {
                val url = uploadSingleImage(bytes)
                if (url != null) newUploadedUrls.add(url)
            }

            val fileMap = newUploadedUrls.mapIndexed { index, url ->
                "image[$index][file_name]" to url
            }.toMap() + newUploadedUrls.mapIndexed { index, url ->
                "image[$index][file_path]" to url
            }.toMap()

            val result = galleryUseCases.galleryAddImageUseCase(fileMap, serviceId)

            _uiState.update {
                when (result) {
                    is JtApiResult.Success -> {
                        onSuccess()
                        GlobalImageStore.clearAll()
                        it.copy(
                            uploadedImages = it.uploadedImages + newUploadedUrls.map { url -> ServerImages(path = url) },
                            usedPhotos = (it.usedPhotos ?: 0) + newUploadedUrls.size,
                            message = result.data.meta?.message,
                            isUploading = false
                        )
                    }
                    is JtApiResult.Failure -> it.copy(
                        message = result.error.message,
                        isUploading = false
                    )
                }
            }
        }
    }

    private suspend fun uploadSingleImage(bytes: ByteArray): String? {
        var uploadedUrl: String? = null

        fileUploadUseCase.uploadGalleryImage(
            file = bytes,
            filename = { fileName ->
                uploadedUrl = fileName
            },
            onMessage = { msg ->
                _uiState.update { it.copy(message = msg) }
            },
            onSuccess = { }
        )

        return uploadedUrl
    }

    fun removeImage(index: Int) {
        GlobalImageStore.removeImageAt(index)
    }

    fun loadServices(serviceId: String) {
        viewModelScope.launch {
            val request = ServiceListRequest(limit = PAGINATION_LIMIT.toString(), offset = "0")

            when (val result = providerBookingUseCases.getService(request)) {
                is JtApiResult.Success -> {
                    val serviceData = result.data.data.firstOrNull { it.id == serviceId }
//                    val gallery = result.data.galleryBookings.firstOrNull()
                    if (serviceData != null) {
                        _uiState.update { it.copy(uploadedImages = serviceData.images ?: emptyList()) }
                        _uiState.update { current ->
                            current.copy(
                                uploadedImages = serviceData.images ?: emptyList(),
                            )
                        }
                    }
                }
                is JtApiResult.Failure -> _uiState.update { it.copy(message = "Failed to Load Services") }
            }
        }
    }

    fun deleteGalleryImage(imagePath: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }

            val result = galleryUseCases.galleryDeleteImageUseCase(imagePath)

            _uiState.update {
                when (result) {
                    is JtApiResult.Success -> it.copy(
                        uploadedImages = it.uploadedImages.filter { img -> img.customId != imagePath },
                        message = "Image Deleted Successfully",
                        isUploading = false
                    )
                    is JtApiResult.Failure -> it.copy(
                        message = result.error.message.ifEmpty { "Failed to delete image" },
                        isUploading = false
                    )
                }
            }

            fetchGalleryBookings()
        }
    }
}
