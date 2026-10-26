package com.bytheour.presentations.screens.tabs.providerHomeTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.useCases.ProvidersUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProviderHomeViewModel(
    private val providersUseCases: ProvidersUseCases,
    private val providerBookingUseCases: ProviderBookingUseCases,
    private val commonUseCases: CommonUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
):ViewModel() {

    data class ProviderUiState(
        val totalEarnings: String?="",
        val monthEarnings: String?="",
        val withdrawAmount: String?="",
        val currentBalance: String?="",
        val totalLead:String?="",
        val completedLead:String?="",
        val currentAssignedLeads:String?="",
        val newAssignedLeads:String?="",
        val errorMessage: String? = null,
        val message: String? = null,
        val id :String? = null,
        val serviceName:String? = null,
        val date:String? = null,
        val time:String? = null,
        val notAssigned:String? = null,
        val accepted:Boolean? = null,
        val profile:String? = null,
        val address:String? = null,
        val estimation:String? = null,
        val description:String? = null,
        val userName:String? = null,
        val userProfile:String? = null,
        val verificationStatus: String? = null,
        val webUrl: String? = null,
        val showAddServiceDialog:Boolean = false,
        val customerLat:String? = null,
        val customerLong:String? = null,
        val notificationPermissionRequested: Boolean = false,
        val isInspection:Boolean? = null,
        val isInspectionRate :String? = null,
        val problem: String? = null,
        val problemDescription: String? = null,
        val solution: String? = null,
        val estimatedCost: Double? = null,
        val availableTime :String? = null,
        val reason :String? = null,
        val hasInspectionReport : Boolean = false,
        val pinCode :String?= null,
        val inspectionCalculatedAmount:Double? = null
    )

    private val _uiState = MutableStateFlow(ProviderUiState())
    val uiStates: StateFlow<ProviderUiState> = _uiState.asStateFlow()


    fun clearState(){
        _uiState.update { it.copy(
            notAssigned = null,
            accepted = null,
        ) }
    }
    fun setNotificationPermissionRequested() {
        _uiState.value = _uiState.value.copy(notificationPermissionRequested = true)
    }

    fun getProviderDashBoardDetails(){
        viewModelScope.launch {
            when(val result = providersUseCases.getProviderDashBoardDetails()){
                is Success -> {
                    _uiState.update {
                        it.copy(
                            totalEarnings = result.data?.data?.earnings?.absTotal.toString(),
                            monthEarnings = result.data?.data?.earnings?.absWeek.toString(),
                            withdrawAmount = result.data?.data?.amount?.strWithdrawal,
                            currentBalance = result.data?.data?.amount?.strWallet,
                            totalLead = result.data?.data?.bookings?.totalLeads.toString(),
                            completedLead = result.data?.data?.bookings?.completedLeads.toString(),
                            currentAssignedLeads = result.data?.data?.bookings?.assignedLeads.toString(),
                            newAssignedLeads = result.data?.data?.bookings?.newLeads.toString(),
                            verificationStatus = result.data?.data?.verificationStatus,

                        )
                    }
                }
                is Failure ->{
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error.message
                        )
                    }
                }
            }

        }
    }


    fun acceptBookingRequest(
        booking:String,
        onSuccess:() ->Unit
    ){
        viewModelScope.launch {
            val request = StartBookingRequest(
                booking = booking
            )
            when(val result = providerBookingUseCases.updateBookingRequest(request)){
                is Success -> {
                    if (result.data.meta?.message?.contains("successfully", ignoreCase = true) == true){
                        onSuccess()
                    }
                    _uiState.update {
                            it.copy(
                                message = result.data.meta?.message,
                            )
                        }
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun getProfile(){
        viewModelScope.launch {
            when(val result = commonUseCases.getProfile()){
                is Success -> {
                    sharedPreferenceUseCase.putUserInfo(result.data.data)
                    val name = "${result.data.data.lastName} " + result.data.data.firstName
                    _uiState.update {
                        it.copy(
                            userName = name,
                            userProfile = result.data.data.profile,
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

    fun rejectBookingRequest(
        booking:String,
        onSuccess:() ->Unit
    ){
        viewModelScope.launch {
            val request = StartBookingRequest(
                booking = booking
            )
            when(val result = providerBookingUseCases.rejectBookingRequest(request)){
                is Success -> {
                    if (result.data.meta?.message?.contains("successfully", ignoreCase = true) == true){
                        onSuccess()
                    }
                    _uiState.update {
                        it.copy(
                            message = result.data.meta?.message
                        )
                    }
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error.message
                        )
                    }
                }
            }
        }
    }



    fun getProviderBookingRequest() {
        viewModelScope.launch {
            when (val result = providerBookingUseCases.getBookingRequest()) {
                is Success -> {
                    val response = result.data.data
                    _uiState.update {
                        it.copy(
                            id = response?.booking?.id?:"",
                            serviceName = response?.service?.title?:"",
                            date = response?.booking?.date?:"",
                            time = response?.booking?.time?:"",
                            notAssigned = response?.flags?.type,
                            profile = response?.service?.image,
                            estimation = (response?.booking?.hours?:"").toString(),
                            address = response?.address?.address?.line1,
                            description = response?.service?.description,
                            accepted = response?.flags?.accepted,
                            customerLong = response?.address?.lng,
                            customerLat = response?.address?.lat,
                            pinCode = response?.address?.address?.zip,
                            isInspection = response?.flags?.isInspection,
                            isInspectionRate = response?.service?.inspectionRate?:"",
                            problem = response?.inspectionReport?.problem,
                            problemDescription = response?.inspectionReport?.problemDescription,
                            solution = response?.inspectionReport?.problemSolution,
                            estimatedCost = response?.inspectionReport?.estimation,
                            reason = response?.inspectionReport?.rejectReason,
                            availableTime = response?.inspectionReport?.availableDateTime,
                            hasInspectionReport = response?.inspectionReport != null,
                            inspectionCalculatedAmount = response?.booking?.inspectionAmount
                        )
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun verifyAccountWithStripe(){
        viewModelScope.launch {
            when(val result = commonUseCases.verifyAccountWithStripe()) {
                is Success -> {
                    val response = result.data.data
                    if (response.status=="verified") {
                        _uiState.update {
                            it.copy(
                                message = "Your account has been Verified Successfully"
                            )
                        }
                    }else if (response.url != null){
                        _uiState.update {
                            it.copy(
                                webUrl = response.url
                            )
                        }
                    }else{
                        _uiState.update {
                            it.copy(
                                errorMessage = "Account couldn't be verified. Please try again"
                            )
                        }
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Account couldn't be verified. Please try again"
                        )
                    }
                }
            }
        }
    }

    fun updateWebUrl(){
        _uiState.update {
            it.copy(
                webUrl = null
            )
        }
    }

    fun getWorkerServices() {
        viewModelScope.launch {

            val request = ServiceListRequest(
                limit  = PAGINATION_LIMIT.toString(),
                offset = 0.toString()
            )

            when (val result = providerBookingUseCases.getService(request)) {
                is Success -> {
                    val show = result.data.data
                    _uiState.update {
                        it.copy(
                            showAddServiceDialog = show.isEmpty()

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