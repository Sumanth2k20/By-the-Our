package com.bytheour.presentations.screens.tabs.reports

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.AdminEditUserRequest
import com.bytheour.data.request.AdminGetUserRequest
import com.bytheour.useCases.AdminUseCases
import com.bytheour.useCases.CommonUseCases
import com.bytheour.utils.FileUpload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("ArrayInDataClass")
data class UserEditUiState(
    val id: String?= "",
    val firstName: String? = "",
    val lastName :String? = "",
    val email :String? = "",
    val contactNumber :String? = "",
    val deviceType :String? = "",
    val avatar :String? = "",
    val completed :Int? = 0,
    val upComing :Int? = 0,
    val onGoing :Int? = 0,
    val message:String?= "",
    val generatedUrl:String? = null,
    val avatarFileName: String ? = null,
    val imageByteArray:ByteArray? = null,
    val imageBitmap: ImageBitmap? = null

)

class UserEditScreenViewModel(
    private val adminUseCases: AdminUseCases,
    commonUseCases: CommonUseCases
): ViewModel() {

    private val _uiState = MutableStateFlow(UserEditUiState())
    val uiStates = _uiState.asStateFlow()



    fun imageByteArrayChange(byteArray: ByteArray) {
        _uiState.update { it.copy(imageByteArray = byteArray) }
    }

    fun updateImageBitmap(imageBitmap: ImageBitmap?) {
        _uiState.update { state ->
            state.copy(imageBitmap = imageBitmap)
        }
    }

    private val fileUploadUseCase = FileUpload(commonUseCases)

    fun uploadUserImage(filePath:ByteArray) {
        viewModelScope.launch {
            fileUploadUseCase.uploadImage(
                file = filePath,
                filename = { uploadedFileName ->
                    _uiState.update { currentState ->
                        currentState.copy(avatar = uploadedFileName)
                    }
                },
                onMessage = { message ->
                    _uiState.update { it.copy(message = message) }
                },

                )
        }
    }

    fun onAvatarFileNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(avatarFileName = newValue)
    }


    fun updateName(firstname: String) {
        _uiState.update {
            it.copy(firstName = firstname)
        }
    }
    fun updateLastName(lastname: String) {
        _uiState.update {
            it.copy(lastName = lastname)
        }
    }
    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }
    fun updateContactNumber(contactNumber: String) {
        _uiState.update {
            it.copy(contactNumber = contactNumber)
        }
    }



    fun adminGetUserDate(
        id: String
    ){
        viewModelScope.launch {
            val request = AdminGetUserRequest(
                id = id
            )
            when(val result = adminUseCases.adminGetUserData(request)) {
                is Success -> {
                    val record = result.data.data?.records?.firstOrNull()

                    _uiState.update {
                        it.copy(
                            id = record?.id?.toString(),
                            firstName = record?.firstName,
                            lastName = record?.lastName,
                            email = record?.email,
                            contactNumber = record?.contactNumber,
                            completed = result.data.data?.bookings?.completed,
                            onGoing = result.data.data?.bookings?.ongoing,
                            upComing = result.data.data?.bookings?.upcoming,
                        )
                    }

                }
                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }


    fun adminUserEdit(
        id: String,
        firstName: String,
        lastName: String,
        profile: String,
        email: String,
        onSuccess:()->Unit
    ){
        viewModelScope.launch {
            val request = AdminEditUserRequest(
                id = id,
                firstName = firstName,
                lastName = lastName,
                profile = profile,
                email = email
            )
            when (val result = adminUseCases.adminUserEdit(request)) {
                is Success -> {
                    val record = result.data.data?.records?.firstOrNull()
                    _uiState.update {
                        it.copy(
                            id = record?.id?.toString(),
                            firstName = record?.firstName,
                            lastName = record?.lastName,
                            email = record?.email,
                        )
                    }
                    onSuccess()
                }
                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

}