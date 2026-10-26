package com.bytheour.presentations.screens.signup

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.LoginRequest
import com.bytheour.data.request.UserEmailRequest
import com.bytheour.data.request.UserNameRequest
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.EntryModuleUseCases
import com.bytheour.utils.COUNTRY
import com.bytheour.utils.FileUpload
import com.bytheour.utils.REFERRAL_CODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTSignUpViewModel(
    private val entryModuleUseCase: EntryModuleUseCases<Any?>,
    commonUseCases: CommonUseCases

) : ViewModel() {

    @Suppress("ArrayInDataClass")
    data class UIStates(
        val number: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val userName: String? = null,
        val email: String? = null,
        val isExistCheck: Boolean? = false,
        val isNavigate: Boolean? = false,
        val firstErrorMessage: String? = null,
        val lastErrorMessage: String? = null,
        val userErrorMessage: String? = null,
        val emailErrorMessage: String? = null,
        val message: String? = null,
        val error: String? = null,
        val imageBitmap: ImageBitmap? = null,
        val imageFromServer: String? = null,
        val imagePathUrl: String? = null,
        val byteArrayImage: ByteArray? = null

    )

    private val _uiStates = MutableStateFlow(UIStates())
    val uiStates = _uiStates.asStateFlow()


    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption

    fun updateSelectedOption(option: String) {
        _selectedOption.value = option
    }


    private val fileUploadUseCase = FileUpload(commonUseCases)

    fun uploadUserImage(filePath: ByteArray) {
        viewModelScope.launch {
            fileUploadUseCase.uploadImage(
                file = filePath,
                filename = { uploadedFileName ->
                    _uiStates.update { currentState ->
                        currentState.copy(imagePathUrl = uploadedFileName)
                    }
                },
                onMessage = { message ->
                    _uiStates.update { it.copy(message = message) }
                },

                )
        }
    }

    fun register(
        onSuccess: () -> Unit,
        number: String,
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        profile: String
    ) {
        viewModelScope.launch {

            val result = entryModuleUseCase.register(
                firstName = firstname,
                lastName = lastname,
                userName = username,
                email = email,
                country = COUNTRY,
                number = number,
                code = REFERRAL_CODE,
                profile = profile
            )

            when (result) {
                is Success -> {
                    val userData = result.data.data
                    if (userData == null) {
                        _uiStates.update { it.copy(message = result.data.meta?.message) }
                    } else {
                        _uiStates.update { it.copy(message = result.data.meta?.message) }
                        onSuccess()
                    }
                }

                is Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message.ifEmpty { "Registration failed with unknown error" }
                        )
                    }
                }
            }
        }
    }



    fun updateMessage(message: String) {
        _uiStates.update {
            it.copy(message = message)
        }
    }

    fun getTaxClassification(
        type: String?,
        navigateToLocation: () -> Unit = {},
    ) {
        viewModelScope.launch {
            when (val result = entryModuleUseCase.tacClassification(type)) {
                is Success -> {
                    _uiStates.update {
                        it.copy()
                    }
                    navigateToLocation()
                }

                is Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }


    fun clearError() {
        _uiStates.update { it.copy(message = null, error = null) }
    }

    fun updateImageBitmap(imageBitmap: ImageBitmap) {
        _uiStates.update { state ->
            state.copy(imageBitmap = imageBitmap)
        }
    }

    fun updateByteArrayImage(byteArrayImage: ByteArray) {
        _uiStates.update { state ->
            state.copy(byteArrayImage = byteArrayImage)
        }
    }

    fun updateImagePathUrl(imagePathUrl: String) {
        _uiStates.update { state ->
            state.copy(imagePathUrl = imagePathUrl)
        }
    }

    fun updateFirstName(firstname: String) {
        _uiStates.update { state ->
            state.copy(firstName = firstname, firstErrorMessage = null)
        }
    }

    fun updateLastName(lastname: String) {
        _uiStates.update { state ->
            state.copy(lastName = lastname, lastErrorMessage = null)
        }
    }

    fun updateUserName(username: String) {
        _uiStates.update { state ->
            state.copy(userName = username, userErrorMessage = null)
        }
    }

    fun updateEmail(email: String) {
        _uiStates.update { state ->
            state.copy(email = email, emailErrorMessage = null)
        }
    }


    private fun validateFirstName(): Boolean {
        val firstname = _uiStates.value.firstName
        return if (firstname.isNullOrBlank()) {
            _uiStates.update { state -> state.copy(firstErrorMessage = "Please Enter Your First Name") }
            false
        } else true
    }

    private fun validateLastName(): Boolean {
        val lastname = _uiStates.value.lastName
        return when {
            lastname.isNullOrBlank() -> {
                _uiStates.update { state -> state.copy(lastErrorMessage = "Please Enter Your Last Name") }
                false
            }
            lastname.length < 3 -> {
                _uiStates.update { state -> state.copy(lastErrorMessage = "Last name must be at least 3 characters") }
                false
            }
            else -> {
                _uiStates.update { state -> state.copy(lastErrorMessage = null) }
                true
            }
        }
    }


    private fun validateUserName(): Boolean {
        val lastname = _uiStates.value.userName
        return if (lastname.isNullOrBlank()) {
            _uiStates.update { state -> state.copy(userErrorMessage = "Please Enter Your User Name") }
            false
        } else true
    }

     fun validateUserNameFormat(userName: String?): Boolean {
        // Only allow letters, numbers, underscores (you can adjust regex as needed)
        val userNameRegex = "^[A-Za-z0-9_]+$".toRegex()

         return when {
             userName.isNullOrBlank() -> {
                 _uiStates.update { it.copy(userErrorMessage = "Please Enter Your User Name") }
                 false
             }

             userName.length < 4 -> {
                 _uiStates.update { it.copy(userErrorMessage = "Username must be at least 4 characters") }
                 false
             }

             !userName.matches(userNameRegex) -> {
                 _uiStates.update { it.copy(userErrorMessage = "Username cannot contain spaces or special characters") }
                 false
             }

             else -> {
                 _uiStates.update { it.copy(userErrorMessage = null) }
                 true
             }
         }
     }


    private fun validateEmail(): Boolean {
        val email = _uiStates.value.email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return if (email.isNullOrBlank() || !email.matches(emailRegex)) {
            _uiStates.update { state -> state.copy(emailErrorMessage = "Please Enter a Valid Email Address") }
            false
        } else true
    }

    fun validateAllFields(): Boolean {
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isEmailValid = validateEmail()
        val isUserValid = validateUserName()

        return isFirstNameValid && isLastNameValid && isEmailValid && isUserValid
    }

    fun checkUserName(
        userName: String
    ) {
        viewModelScope.launch {
            val request = UserNameRequest(
                username = userName
            )

            val result = entryModuleUseCase.checkUserNameUseCase(request)

            if (result is Success) {
                _uiStates.update {
                    it.copy(
                        number = result.data.data.contact.number
                    )
                }

            } else if (result is Failure) {
                _uiStates.update {
                    it.copy(
                        message = result.error.message
                    )
                }
            }
        }
    }


    fun checkUserEmail(
        userMail: String
    ) {
        viewModelScope.launch {
            val request = UserEmailRequest(
                userMail = userMail
            )

            val result = entryModuleUseCase.checkUserMailUseCase(request)

            if (result is Success) {
                _uiStates.update {
                    it.copy(
                        number = result.data.data.contact.number
                    )
                }
            } else if (result is Failure) {
                _uiStates.update {
                    it.copy(
                        message = result.error.message
                    )
                }
            }
        }
    }


    fun makeUserCheckContact(
        number: String,
    ) {
        viewModelScope.launch {
            val request = LoginRequest(
                field = number
            )
            val result = entryModuleUseCase.loginUseCase(request)


            if (result is Success) {
                _uiStates.update { state ->
                    state.copy(
                        isExistCheck = result.data.data.exists,
                        number = number,
                        isNavigate = true,
                    )
                }
            } else if (result is Failure) {
                _uiStates.update {
                    it.copy(
                        message = result.error.message,
                    )
                }
            }
        }
    }

}