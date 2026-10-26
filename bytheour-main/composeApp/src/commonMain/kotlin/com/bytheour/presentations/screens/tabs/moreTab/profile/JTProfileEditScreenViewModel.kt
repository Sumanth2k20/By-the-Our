package com.bytheour.presentations.screens.tabs.moreTab.profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.presentations.screens.tabs.moreTab.called
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.FileUpload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTProfileEditScreenViewModel(
    private val commonUseCases: CommonUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
): ViewModel() {

    @Suppress("ArrayInDataClass")
    data class UIStates(
        val number:String? = null,
        val firstName:String? = null,
        val lastName:String? = null,
        val userName:String? = null,
        val email:String? = null,
        val dateOfBirth: String? = "4-may-2000",
        val numberErrorMessage: String? =null,
        val firstErrorMessage: String? =null,
        val lastErrorMessage: String? =null,
        val userErrorMessage: String? =null,
        val emailErrorMessage: String? =null,
        val dateOfBirthErrorMessage: String? = null,
        val imageBitmap: ImageBitmap? = null,
        val imageFromServer: String? = null,
        val message:String? = null,
        val imageErrorMessage: String? = null,
        val imagePathUrl:String? =  null,
        val byteArrayImage:ByteArray? = null,
        val filename:String? = null,


    )

    private val _uiStates = MutableStateFlow(UIStates())
    val uiStates =_uiStates.asStateFlow()

    fun clearError(){
        _uiStates.update {
            it.copy(
                    message = null,
                     imageErrorMessage = null
                )
        }
    }


    private val fileUploadUseCase = FileUpload(commonUseCases)

    fun uploadUserImage(filePath:ByteArray) {
        viewModelScope.launch {
            fileUploadUseCase.uploadImage(
                file = filePath,
                filename = { uploadedFileName ->
                    _uiStates.update { currentState ->
                        currentState.copy(filename = uploadedFileName)
                    }
                },
                onMessage = { message ->
                    _uiStates.update { it.copy(message = message) }
                },
                showSuccessMessage = false

            )
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


    fun updateImageBitmap(imageBitmap: ImageBitmap?) {
        _uiStates.update { state ->
            state.copy(imageBitmap = imageBitmap, imageErrorMessage = null)
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

    fun updateNumber(number: String) {
        if (number.length <= 10 && number.all { it.isDigit() }) {
            _uiStates.update { currentValue ->
                currentValue.copy(number = number, numberErrorMessage = null)
            }
        }
    }


    private fun validateFirstName(): Boolean {
        val firstname = _uiStates.value.firstName
        return when {
            firstname.isNullOrBlank() -> {
                _uiStates.update { state ->
                    state.copy(firstErrorMessage = "Please enter your first name")
                }
                false
            }
            firstname.length < 3 -> {
                _uiStates.update { state ->
                    state.copy(firstErrorMessage = "At least 3 characters")
                }
                false
            }
            else -> true
        }
    }


    private fun validateImage(): Boolean {
        val isImageMissing = _uiStates.value.imageFromServer.isNullOrEmpty() && _uiStates.value.imageBitmap == null
        return if (isImageMissing) {
            _uiStates.update { state -> state.copy(imageErrorMessage = "Please upload an image") }
            false
        } else {
            _uiStates.update { state -> state.copy(imageErrorMessage = null) } // clear error if image is valid
            true
        }
    }


    private fun validateLastName(): Boolean {
        val lastname = _uiStates.value.lastName
        return when {
            lastname.isNullOrBlank() -> {
                _uiStates.update { state ->
                    state.copy(lastErrorMessage = "Please enter your last name")
                }
                false
            }
            lastname.length < 3 -> {
                _uiStates.update { state ->
                    state.copy(lastErrorMessage = "At least 3 characters")
                }
                false
            }
            else -> true
        }
    }


    private fun validateUserName(): Boolean {
        val username = _uiStates.value.userName
        return when {
            username.isNullOrBlank() -> {
                _uiStates.update { state ->
                    state.copy(userErrorMessage = "Please enter your user name")
                }
                false
            }
            username.length < 4 -> {
                _uiStates.update { state ->
                    state.copy(userErrorMessage = "At least 4 characters")
                }
                false
            }
            else -> true
        }
    }


    private fun validateEmail(): Boolean {
        val email = _uiStates.value.email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return if (email.isNullOrBlank() || !email.matches(emailRegex)) {
            _uiStates.update { state -> state.copy(emailErrorMessage = "Please enter a valid email address") }
            false
        } else true
    }



//    fun formatDate(input: String): String {
//        val date = LocalDate.Companion.parse(input)
//        val day = date.dayOfMonth.toString().padStart(2, '0')
//        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
//        val year = date.year
//        return "$day $month $year"
//    }

    private fun validateDateOfBirth(): Boolean {
        val dob = _uiStates.value.dateOfBirth
        return if (dob.isNullOrBlank()) {
            _uiStates.update { state -> state.copy(dateOfBirthErrorMessage = "Please select your date of birth") }
            false
        } else true
    }

    private fun numberValidation(): Boolean {
        val currentNumber = uiStates.value.number.orEmpty()

        val mobileNumberError = when {
            currentNumber.isBlank() -> "Please enter mobile number"
            else -> ""
        }
        _uiStates.update { currentState ->
            currentState.copy(numberErrorMessage = mobileNumberError)
        }
        return mobileNumberError.isEmpty()
    }

    fun validateAllFields(): Boolean {
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isEmailValid = validateEmail()
        val isUserValid = validateUserName()
        val isNumberValid = numberValidation()
        val isDateOfBirthValid = validateDateOfBirth()
        val isImageValid = validateImage()

        return isFirstNameValid &&
                isLastNameValid  &&
                isEmailValid &&
                isUserValid &&
                isNumberValid &&
                isDateOfBirthValid &&
                isImageValid
    }



    init {
        getProfile()
    }

    private fun getProfile(){
        viewModelScope.launch {
            when(val result = commonUseCases.getProfile()){
                is Success -> {
                    _uiStates.update {
                        sharedPreferenceUseCase.putUserInfo(result.data.data)
                        val rawNumber = result.data.data.contact.number
                        val trimmedNumber = when {
                            rawNumber.startsWith("91") -> rawNumber.removePrefix("91")
                            rawNumber.startsWith("1") -> rawNumber.removePrefix("1")
                            else -> rawNumber
                        }
                        it.copy(
                            firstName = result.data.data.firstName,
                            lastName = result.data.data.lastName,
                            userName = result.data.data.username,
                            email = result.data.data.email,
                            number = trimmedNumber,
                            imageFromServer = result.data.data.profile
                        )
                    }
                }
                is Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }

            }
        }
    }

    fun updateProfile(
        firstName: String?,
        lastName: String?,
        email: String?,
        country: String?,
        number: String?,
        profile: String?,
        about: String?,
        username: String?,
        onSuccess:()->Unit
    ) {
        viewModelScope.launch {
            val result = commonUseCases.updateProfile(
                firstName = firstName,
                lastName = lastName,
                email = email,
                country = country,
                number = number,
                profile = profile,
                about = about,
                userName = username
            )
            when(result) {
                is Success -> {
                    val profileInCompleteProfile = result.data.data.flags.profileIncomplete
                    sharedPreferenceUseCase.putProfileInfo(profileInCompleteProfile)
                    called = true
                    sharedPreferenceUseCase.putUserInfo(result.data.data)
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta.message,
                        )
                    }
                    onSuccess()
                }
                is Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }



}