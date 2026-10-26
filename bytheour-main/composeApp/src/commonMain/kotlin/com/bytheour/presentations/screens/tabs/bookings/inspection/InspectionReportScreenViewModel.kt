package com.bytheour.presentations.screens.tabs.bookings.inspection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.InspectionReportRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.useCases.UserBookingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InspectionReportScreenViewModel(
    private val userBookingsUseCases: UserBookingsUseCases,
    ) : ViewModel() {

    data class InspectionReportScreenUIState(
        val problem: String? = null,
        val problemDescription: String? = null,
        val solution: String? = null,
        val estimatedCost: String? = null,
        val time :String? = null,
        val bookingId :String? = null,
        val newBookingId:String? = null,
        // Error messages
        val problemError: String? = null,
        val descriptionError: String? = null,
        val solutionError: String? = null,
        val estimatedCostError: String? = null,
        val timeError: String? = null,
        val message :String? = null,
        val onSuccess:Boolean? = null
    )
    private val _uiStates = MutableStateFlow(InspectionReportScreenUIState())
    val uiStates = _uiStates.asStateFlow()

    fun onProblemChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(problem = value, problemError = null)
    }
    fun onDescriptionChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(problemDescription = value, descriptionError = null)
    }
    fun onSolutionChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(solution = value, solutionError = null)
    }
    fun onEstimatedCostChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(estimatedCost = value, estimatedCostError = null)
    }
    fun onTimeChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(time = value, timeError = null)
    }
    fun isValid():Boolean {
        val current = _uiStates.value
        var isValid = true

        var problemError: String? = null
        var descriptionError: String? = null
        var solutionError: String? = null
        var estimatedCostError: String? = null
//        var timeError: String? = null


        if (current.problem.isNullOrBlank()) {
            problemError = "Problem is required"
            isValid = false
        }

        if (current.problemDescription.isNullOrBlank()) {
            descriptionError = "Description is required"
            isValid = false
        }

        if (current.solution.isNullOrBlank()) {
            solutionError = "Solution is required"
            isValid = false
        }

        if (current.estimatedCost.isNullOrBlank()) {
            estimatedCostError = "Estimated cost is required"
            isValid = false
        } else if (current.estimatedCost.toDoubleOrNull() == null) {
            estimatedCostError = "Enter a valid number"
            isValid = false
        }

//        if (current.time.isNullOrBlank()) {
//            timeError = "Time is required"
//            isValid = false
//        }

        _uiStates.value = current.copy(
            problemError = problemError,
            descriptionError = descriptionError,
            solutionError = solutionError,
            estimatedCostError = estimatedCostError,
//            timeError = timeError
        )

        return isValid

    }


    fun makeSubmitInspectionReport(
        bookingId :String,
        problem:String,
        problemDescription:String,
        solution:String,
        estimatedCost:String,
        time:String,
        onSuccess:()->Unit
    ){
        if (!isValid()) return
        viewModelScope.launch {
            val request = InspectionReportRequest(
                booking = bookingId,
                problem = problem,
                problemSolution = solution,
                problemDescription = problemDescription,
                estimation = estimatedCost,
                availableDateTime = time
            )
            when (val result = userBookingsUseCases.submitInspectionReport(request)) {
                is Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta?.message,
                            onSuccess = true
                        )
                    }
                    onSuccess()
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }

            }
        }

    }


    fun acceptInspectionReport(
        bookingId: String,
        onSuccess: (String)-> Unit
    ){
        viewModelScope.launch {
            val request = StartBookingRequest(
                booking = bookingId
            )
            when (val result = userBookingsUseCases.acceptInspectionReport(request)) {
                is Success -> {
                    val response = result.data.data
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta?.message,
                            newBookingId = response?.booking?.id,
                            onSuccess = true,
                        )
                    }
                    onSuccess(response?.booking?.id?:"")
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message,
                            onSuccess = false)
                    }
                }
            }
        }
    }

}
