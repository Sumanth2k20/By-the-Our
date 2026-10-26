package com.bytheour.presentations.screens.tabs.reports.services

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.useCases.AdminUseCases
import com.bytheour.useCases.CommonUseCases
import com.bytheour.utils.FileUpload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageServiceEditScreenViewModel(
    private val adminUseCases: AdminUseCases,
    commonUseCases: CommonUseCases
) : ViewModel() {

    data class Category(
        val id: Int,
        val title: String
    )

    @Suppress("ArrayInDataClass")
    data class AdminEditServiceRequestUiState(
        val title: String? = null,
        val description: String? = null,
        val category: String? = null,
        val categoryId:Int? = null,
        val sequence: String? = null,
        val avatarFileName: String ? = null,
        val serviceStatus: String? = null,
        val titleError: String? = null,
        val descriptionError: String? = null,
        val categoryError: String? = null,
        val sequenceError: String? = null,
        val avatarError: String? = null,
        val serviceStatusError: String? = null,
        val message: String? = null,
        val imageByteArray:ByteArray? = null,
        val imageBitmap: ImageBitmap? = null,
        val categories: List<Category> = emptyList(),
        val categoryNames: List<String> = emptyList(),
        val categoryIds :List<Int> = emptyList(),
    )

    private val _uiState = MutableStateFlow(AdminEditServiceRequestUiState())
    val uiState: StateFlow<AdminEditServiceRequestUiState> = _uiState.asStateFlow()


    fun imageByteArrayChange(byteArray: ByteArray) {
        _uiState.update { it.copy(imageByteArray = byteArray) }
    }

    fun imageBitmapChange(bitmap:ImageBitmap) {
        _uiState.update { it.copy(imageBitmap = bitmap) }
    }


    private val fileUploadUseCase = FileUpload(commonUseCases)

    fun uploadUserImage(filePath:ByteArray) {
        viewModelScope.launch {
            fileUploadUseCase.uploadImage(
                file = filePath,
                filename = { uploadedFileName ->
                    _uiState.update { currentState ->
                        currentState.copy(avatarFileName = uploadedFileName)
                    }
                },
                onMessage = {
                    _uiState.update { it.copy() }
                }
            )
        }
    }

    fun submitEditRequest(
        serviceId: Int,
        title: String?,
        description: String?,
        categoryId: Int,
        sequence: String?,
        picture: String?,
        serviceStatus: String?,
    ) {
        viewModelScope.launch {

            when (val result = adminUseCases.adminEditManageServiceRequestList(
                id = serviceId.toString(),
                title = title?:"",
                description = description?:"",
                categoryId = categoryId.toString(),
                sequence = sequence?:"",
                picture = picture,
                serviceStatus = serviceStatus?:""

            )) {
                is Success -> {
                    _uiState.update {
                        it.copy(message = result.data.meta?.message)
                    }
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }



    fun getCategoriesList(
        limit: String,
        offset: String
    ){
        viewModelScope.launch {
            val result = adminUseCases.getServicesList(
                limit = limit,
                offset = offset
            )

            when (result) {

                is Success -> {
                    val categoriesData = result.data.data?.categories
                    val categories = categoriesData?.map { categoryData ->
                        Category(
                            id = categoryData?.id?:0,
                            title = categoryData?.title?:""
                        )
                    } ?: emptyList()

                    val categoryNames = categories.map { it.title }

                    _uiState.update { currentState ->
                        currentState.copy(
                            categories = categories,
                            categoryNames = categoryNames,
                        )
                    }
                }
                is Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            message = result.error.message,
                            categories = emptyList(),
                            categoryNames = emptyList()
                        )
                    }
                }
            }
        }
    }






    // Input update functions
    fun onTitleChange(newValue: String) {
        _uiState.value = _uiState.value.copy(title = newValue, titleError = null)
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.value = _uiState.value.copy(description = newValue, descriptionError = null)
    }

    fun onCategoryChange(newValue: String) {
        _uiState.value = _uiState.value.copy(category = newValue, categoryError = null)
    }

    fun onSequenceChange(newValue: String) {
        _uiState.value = _uiState.value.copy(sequence = newValue, sequenceError = null)
    }

    fun onAvatarFileNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(avatarFileName = newValue, avatarError = null)
    }




    fun validate(): Boolean {
        var isValid = true

        _uiState.update { state ->
            var newState = state

            if (state.title?.isBlank() == true) {
                newState = newState.copy(titleError = "Title Cannot Be Empty")
                isValid = false
            }

            if (state.description?.isBlank() == true) {
                newState = newState.copy(descriptionError = "Description Cannot Be Empty")
                isValid = false
            }

            if (state.category?.isBlank() == true) {
                newState = newState.copy(categoryError = "Category Cannot Be Empty")
                isValid = false
            }

            if (state.sequence?.isBlank() == true) {
                newState = newState.copy(sequenceError = "Sequence Cannot Be Empty")
                isValid = false
            }

            if (state.avatarFileName?.isBlank() == true) {
                newState = newState.copy(avatarError = "Avatar File Name Cannot Be Empty")
                isValid = false
            }

            if (state.serviceStatus?.isBlank() == true) {
                newState = newState.copy(serviceStatusError = "Service Status Cannot Be Empty")
                isValid = false
            }

            newState
        }

        return isValid
    }


}