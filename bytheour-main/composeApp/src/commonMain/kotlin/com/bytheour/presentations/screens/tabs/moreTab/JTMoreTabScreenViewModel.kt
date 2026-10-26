package com.bytheour.presentations.screens.tabs.moreTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.SwitchAccountRequest
import com.bytheour.data.response.LoginNewResponse
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.EntryModuleUseCases
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.TYPE_CUSTOMER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

var called :Boolean =false

class JTMoreTabScreenViewModel(
    private val commonUseCases: CommonUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase,
    private val generalUseCases: GeneralUseCases,
    private val entryModuleUseCase: EntryModuleUseCases<Any>
) : ViewModel() {

    enum class SwitchDestination {
        MAIN_SCREEN
    }

    data class MoreUIState(
        val userName: String? = null,
        val mail: String? = null,
        val isCheckedNotification: Boolean? = true,
        val isCheckedAvailability: Boolean? = false,
        val message: String? = null,
        val image: String? = null,
        val linked :Boolean? = null,
        val data : LoginNewResponse.Data? = null,
        val destination: SwitchDestination? = null,
        val showCreateProfile: Boolean = false,
        val switchAccountClicked: String = "",
        val switchFromSignUp:String = "",
        val inviteCode: String? = null,
        val inviteLink: String? = null,
        val inviteText: String? = null
    )
    private val _uiState = MutableStateFlow(MoreUIState())
    val uiState: StateFlow<MoreUIState> = _uiState.asStateFlow()
    val userType = sharedPreferenceUseCase.getUser()
    var isSwitchToUserClicked = false
    var isFromSwitchAccount = false

    private var switchAccountSuccess = false
    private var deviceTokenSuccess = false

    init {
        val notify = sharedPreferenceUseCase.getAvilabilityStatus()
        _uiState.update {
            it.copy(isCheckedAvailability = notify)
        }
    }
    init {
        val notifyStatus = sharedPreferenceUseCase.getNotificationStatus()
        _uiState.update {
            it.copy(isCheckedNotification = notifyStatus)
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(message = null)
        }
    }
    fun updateShowProfile(){
        _uiState.update {
            it.copy(showCreateProfile = false)
        }
    }
    fun switchAccountClick(switch:String){
        _uiState.update {
            it.copy(switchAccountClicked = switch)
        }
    }
    fun switchFromSignUp(switch:String){
        _uiState.update {
            it.copy(switchFromSignUp = switch)
        }
    }

    fun logOut(navigateToUserTypeScreen: () -> Unit) {
        viewModelScope.launch {
            when(val result = commonUseCases.logOut()){
                is Success ->{
                    _uiState.update {
                        it.copy(
                            message = result.data.meta.message
                        )
                    }
                    navigateToUserTypeScreen()
                    sharedPreferenceUseCase.clearSharedPrefs()

                }
                is Failure ->{
                    _uiState.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun getProfile(
        onSuccess: () -> Unit = {}
    ){
        viewModelScope.launch {
            when(val result = commonUseCases.getProfile()){
                is Success -> {
                    sharedPreferenceUseCase.putUserInfo(result.data.data)
                    onSuccess()
                    val name = "${result.data.data.firstName} " + result.data.data.lastName
                    _uiState.update {
                        it.copy(
                            userName = name,
                            mail = result.data.data.email,
//                                message = result.data.meta.message,
                            image = result.data.data.profile
                        )
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

    fun notificationStatusUpdate(allowNotifications: Boolean) {
        viewModelScope.launch {
            val result =
                commonUseCases.updateNotificationsStatus(if (allowNotifications) "y" else "n")

            when (result) {
                is Success -> {
                    _uiState.update {
                        it.copy(
                            isCheckedNotification = allowNotifications,
                            message = result.data.meta.message
                        )
                    }
                    sharedPreferenceUseCase.putUserInfo(result.data.data)
                    sharedPreferenceUseCase.putNotificationStatus(allowNotifications)
                }

                is Failure -> {
                    _uiState.update { it.copy(message = result.error.message) }
                }
            }
        }
    }

    fun updateVisibility(availability: Boolean) {
        viewModelScope.launch {
            val result =
                commonUseCases.updateVisibility(if (availability) "y" else "n")

            when (result) {
                is Success -> {
                    if (result.data.data != null) {
                        _uiState.update {
                            it.copy(
                                isCheckedAvailability = availability,
                                message = result.data.meta.message
                            )
                        }
                        sharedPreferenceUseCase.putAvilabilityStatus(availability)
                    } else {
                        _uiState.update { it.copy(message = result.data.meta.message) }
                    }
                }

                is Failure -> {
                    _uiState.update { it.copy(message = result.error.message) }
                }
            }
        }
    }

    fun checkSwitchAccountStatus(
        navigateToSignUpScreen: () -> Unit = {},
    ) {
        viewModelScope.launch {
            val result = commonUseCases.switchAccountStatus()
            val sharedPreference = sharedPreferenceUseCase.getUserInfo()
            val user = sharedPreferenceUseCase.getUser()

            when (result) {
                is Success -> {
                    val data = result.data.data.verification
                    val account = result.data.data.account

                    _uiState.update { it.copy(message = result.data.meta.message) }

                    if (sharedPreference?.flags?.profileIncomplete == true && user == TYPE_CUSTOMER) {
                        if (account.exists) {
                            _uiState.update { it.copy(showCreateProfile = true) }
                        } else {
                            navigateToSignUpScreen()
                        }
                    } else if (called) {
                        if (user == TYPE_CUSTOMER) {
                            when {
                                (data.email && data.number) || data.number -> {
                                    isFromSwitchAccount = true
                                    isSwitchToUserClicked = true
                                    switchAccount()
                                }
                                data.email -> {
                                    isSwitchToUserClicked = true
                                    isFromSwitchAccount = true
                                    switchAccount(
                                        securityToken = "test-email-link",
                                        type = "email",
                                        email = sharedPreference?.email
                                    )
                                }
                                else -> {
                                    isFromSwitchAccount = true
                                    isSwitchToUserClicked = true
                                    switchAccount()
                                }
                            }
                        } else {
                            if (data.number) {
                                isFromSwitchAccount = true
                                isSwitchToUserClicked = true
                                switchAccount()
                            } else {
                                isFromSwitchAccount = true
                                isSwitchToUserClicked = true
                                switchAccount()
                            }
                        }
                    } else {
                        if (user == TYPE_CUSTOMER) {
                            when {
                                (data.email && data.number) || data.number -> {
                                    isSwitchToUserClicked = true
                                    isFromSwitchAccount = true
                                    switchAccount()
                                }
                                data.email -> {
                                    isFromSwitchAccount = true
                                    isSwitchToUserClicked = true
                                    switchAccount(
                                        securityToken = "test-email-link",
                                        type = "email",
                                        email = sharedPreference?.email
                                    )
                                }
                                else -> {
                                    isFromSwitchAccount = true
                                    isSwitchToUserClicked = true
                                    switchAccount()
                                }
                            }
                        } else {
                            if (data.number) {
                                isFromSwitchAccount = true
                                isSwitchToUserClicked = true
                                switchAccount()
                            } else {
                                isSwitchToUserClicked = true
                                isFromSwitchAccount = true
                                switchAccount()
                            }
                        }
                    }
                }

                is Failure -> {
                    _uiState.update { it.copy(message = result.error.message) }
                }
            }
        }
    }


    fun switchAccount(
        contact: String? = null,
        securityToken: String? = null,
        type: String? = null,
        email: String? = null,
    ) {
        viewModelScope.launch {
            val request = SwitchAccountRequest(
                contact = contact,
                securityToken = securityToken,
                type = type,
                email = email
            )
            when (val result = commonUseCases.switchAccount(request)) {
                is Success -> {
                    switchAccountSuccess = true
                    _uiState.update {
                        it.copy(
                            message = result.data.meta.message,
                            data = result.data.data
                        )
                    }
                    checkNavigationCondition()
                }
                is Failure -> {
                    switchAccountSuccess = false
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun saveProfile(
        onSuccess:()->Unit,
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        type: String
    ) {
        viewModelScope.launch {
            val result = entryModuleUseCase.saveProfile(
                firstName = firstname,
                lastName = lastname,
                userName = username,
                email = email,
                companyType = type
            )

            when (result) {
                is Success -> {
                    checkSwitchAccountStatus(onSuccess)
                    onSuccess()
                    _uiState.update {
                        it.copy(
                            message = result.data.meta.message
                        )
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

    fun deviceToken(
        device: String,
        token: String,
        type: String,
        version: Int,
        os: String,
        appVersion: String
    ) {
        viewModelScope.launch {
            val result = generalUseCases.deviceToken(
                device = device,
                token = token,
                type = type,
                version = version,
                os = os,
                appVersion = appVersion,
            )
            when (result) {
                is Success -> {
                    deviceTokenSuccess = true
                    _uiState.update {
                        it.copy(
                            message = result.data.meta.message ?: ""
                        )
                    }
                    checkNavigationCondition()
                }

                is Failure -> {
                    deviceTokenSuccess = false
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

     private fun checkNavigationCondition() {
        if (switchAccountSuccess && deviceTokenSuccess) {
            _uiState.update {
                it.copy(destination = SwitchDestination.MAIN_SCREEN)
            }
            // Reset flags for future use
            switchAccountSuccess = false
            deviceTokenSuccess = false
        }
    }

    fun getInviteCode() {
        viewModelScope.launch {
            when (val result = commonUseCases.getInviteCode()) {
                is Success -> {
                    _uiState.update {
                        it.copy(
                            inviteCode = result.data.data.code,
                            inviteLink = result.data.data.link,
                            inviteText = result.data.data.text,
                            message = result.data.meta.message
                        )
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


}