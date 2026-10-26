package com.bytheour.presentations.screens.tabs.reports.workers

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.useCases.AdminUseCases
import com.bytheour.useCases.CommonUseCases
import com.bytheour.utils.FileUpload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditWorkerDetailsScreenViewModel(
    val adminUseCases: AdminUseCases,
    commonUseCases: CommonUseCases
):ViewModel() {

    data class ServiceItem(
        val id: String? = null,
        val title: String? = null,
        val category: String? = null,
        val rate: Int? = null,
        val minHour: Int? = null
    )
    @Suppress("ArrayInDataClass")
    data class AdminWorkerUiState(
        val titleName: String? = "",
        val accountType: String? = "--",
        val walletAmount: Double? = null,
        val noOfServices: String? = "1",
        val firstName: String? = null,
        val lastName: String? = null,
        val email: String? = null,
        val phoneNumber: String? = null,
        val contactNumber: String? = null,
        val address: String? = null,

        val firstNameError: String? = null,
        val lastNameError: String? = null,
        val emailError: String? = null,
        val phoneNumberError: String? = null,
        val avatarError: String? = null,
        val contactNumberError: String? = null,
        val addressError: String? = null,

        val message: String? = null,

        val verificationStatus: String? = null,
        val comment: String? = null,
        val rejectionStatus: String? = null,

        val serviceVerificationStatus: String? = null,
        val serviceComment: String? = null,
        val serviceRejectionStatus: String? = null,

        val kycVerificationStatus: String? = null,
        val kycComment: String? = null,
        val kycRejectionStatus: String? = null,

        val completed: Int? = null,
        val onGoing: Int? = null,
        val upComing: Int? = null,

        val totalReviews: Int? = null,
        val avgReviews: String? = "",

        // Replace single service fields with services list
        val services: List<ServiceItem> = emptyList(),

        // Keep these for backward compatibility if needed elsewhere
        val serviceTitle: String? = null,
        val category: String? = null,
        val rate: String? = null,
        val minHours: String? = null,

        val avatarFileName: String? = null,
        val generatedUrl: String? = null,
        val imageByteArray: ByteArray? = null,
        val imageBitmap: ImageBitmap? = null,

        val tabs: List<String> = listOf("Personal Info", "Service Details", "KYC Details"),
        val selectedTab: Int = 0,
    )

    private val _uiState = MutableStateFlow(AdminWorkerUiState())
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
                        currentState.copy(avatarFileName = uploadedFileName)
                    }
                },
                onMessage = { message ->
                    _uiState.update { it.copy(message = message) }
                },
            )
        }
    }

    fun updateVerificationStatus(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val state = _uiState.value
            when (
                val result = adminUseCases.adminVerificationStatus(
                    id = id,
                    status = state.verificationStatus,
                    reason = state.comment,
                    rejectionStatus = state.rejectionStatus
                )
            ) {
                is Success -> {
                    _uiState.update {
                        it.copy(message = result.data.meta?.message)
                    }
                    onSuccess()
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                    onFailure(result.error.message)
                }
            }
        }
    }


    fun adminEditWorker(
        id: String,
        firstname: String,
        lastName: String,
        profile: String,
        onSuccess:() -> Unit
    ) {
        viewModelScope.launch {
            when (val result = adminUseCases.adminEditWorker(
                id = id,
                firstName = firstname,
                lastName = lastName,
                profile = profile
            )) {
                is Success -> {
                    val data = result.data.data
                    if (data != null) {
                        _uiState.update {
                            it.copy(
                                message = result.data.meta?.message
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                message = result.data.meta?.message
                            )
                        }
                    }
                    onSuccess()

                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }



    fun fetchAdminWorkerData(id: String) {
        viewModelScope.launch {
            when (val result = adminUseCases.getAdminWorkerData(id)) {
                is Success -> {
                    val data = result.data.data
                    if (data != null) {
                        val record = data.records?.firstOrNull()
                        val service = data.services?.firstOrNull() // Keep for backward compatibility

                        val servicesList = data.services?.map { serviceData ->
                            ServiceItem(
                                title = serviceData?.title,
                                category = serviceData?.category,
                                rate = serviceData?.rate,
                                minHour = serviceData?.minHour
                            )
                        } ?: emptyList()

                        _uiState.update {
                            it.copy(
                                firstName = record?.firstName,
                                lastName = record?.lastName,
                                email = record?.email,
                                phoneNumber = record?.contactNumber,
                                contactNumber = record?.contactNumber,
                                address = data.address,
                                avatarFileName = record?.profile,
                                titleName = record?.workerName ?: "${record?.firstName.orEmpty()} ${record?.lastName.orEmpty()}".trim(),
                                accountType = record?.dType ?: "--",
                                walletAmount = data.walletAmount,
                                noOfServices = data.noOfServices?.toString() ?: "0",
                                verificationStatus = data.verificationStatus ?: "",
                                completed = data.bookings?.completed,
                                onGoing = data.bookings?.ongoing,
                                upComing = data.bookings?.upcoming,
                                totalReviews = data.reviews?.count,
                                avgReviews = data.reviews?.average,

                                // New services list
                                services = servicesList,

                                // Keep these for backward compatibility
                                serviceTitle = service?.title,
                                category = service?.category,
                                rate = service?.rate?.toString(),
                                minHours = service?.minHour?.toString()
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                message = result.data.meta?.message
                            )
                        }
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }












    // Input update functions
    fun onFirstNameChange(newValue: String) {
        _uiState.update { it.copy(firstName = newValue, firstNameError = null) }
    }

    fun onLastNameChange(newValue: String) {
        _uiState.update { it.copy(lastName = newValue, lastNameError = null) }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue, emailError = null) }
    }


    fun onContactNumberChange(newValue: String) {
        _uiState.update { it.copy(contactNumber = newValue, contactNumberError = null) }
    }

    fun onAddressChange(newValue: String) {
        _uiState.update { it.copy(address = newValue, addressError = null) }
    }

    fun onAvatarFileNameChange(newValue: String) {
        _uiState.update { it.copy(avatarFileName = newValue, avatarError = null) }
    }

    fun onVerificationStatusChange(status: String) {
        _uiState.update { it.copy(verificationStatus = status) }
    }

    fun onCommentChange(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    fun onRejectionStatusChange(status: String) {
        _uiState.update { it.copy(rejectionStatus = status) }
    }


    fun onServiceVerificationStatusChange(status: String) {
        _uiState.update { it.copy(serviceVerificationStatus = status) }
    }

    fun onServiceCommentChange(comment: String) {
        _uiState.update { it.copy(serviceComment = comment) }
    }

    fun onServiceRejectionStatusChange(status: String) {
        _uiState.update { it.copy(serviceRejectionStatus = status) }
    }

    fun onKycServiceVerificationStatusChange(status: String) {
        _uiState.update { it.copy(kycVerificationStatus = status) }
    }

    fun onKycServiceCommentChange(comment: String) {
        _uiState.update { it.copy(kycComment = comment) }
    }

    fun onKycServiceRejectionStatusChange(status: String) {
        _uiState.update { it.copy(kycRejectionStatus = status) }
    }


    fun validate(): Boolean {
        var isValid = true

        _uiState.update { state ->
            var newState = state

            if (state.firstName?.isBlank() == true) {
                newState = newState.copy(firstNameError = "First Name Cannot Be Empty")
                isValid = false
            }

            if (state.lastName?.isBlank() == true) {
                newState = newState.copy(lastNameError = "Last Name Cannot Be Empty")
                isValid = false
            }


            if (state.avatarFileName?.isBlank() == true) {
                newState = newState.copy(avatarError = "Avatar File Name Cannot Be Empty")
                isValid = false
            }

            newState
        }

        return isValid
    }


    fun onTabSelected(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

}