package com.bytheour.presentations.screens.tabs.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.request.AddRatingRequest
import com.bytheour.data.request.BookingDetailsRequest
import com.bytheour.data.request.ManageQuotationRequest
import com.bytheour.data.request.PromoCodeListRequest
import com.bytheour.data.request.RaiseMediationRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.data.response.BookingDetailResponse
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.useCases.CommonBookingUseCase
import com.bytheour.useCases.CommonPaymentUseCase
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.useCases.UserBookingsUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.PaymentCallbacks
import com.bytheour.utils.SharedPaymentMethod
import com.bytheour.utils.Status
import com.bytheour.utils.StripeBridgeRegistry
import com.bytheour.utils.StripeUiBridgeRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class JTBookingTabViewmodel(
    private val userBookingsUseCases: UserBookingsUseCases,
    private val commonBookingsUseCases: CommonBookingUseCase,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase,
    private val commonPaymentUseCase: CommonPaymentUseCase,
) : ViewModel() {

    val tag = "SAI----"

    data class ServiceItem(
        val title: String? = null,
        val priceRange: Double? = null,
        val scheduleDate: String? = null,
        val scheduleTime: String? = null,
        val providerName: String? = null,
        val address: String? = "",
        val status: Status.OrderStatus?,
        val selectedTab: Int? = 0,
        val workingName:String?= null,
        val rating: Float?= null,
        val reviewCount: String?= null,
        val pricePerHour: Int?= null,
        val message:String?= null,
        val image:String?= null,
        val id:String?= null,
        val workerName :String? = null,
        val customerId :String?= null,
        val bookingId:String?= null,
        val workerId:String?= null,
        val userId:String?= null,
        val review:String?= null,
        val percentage : Double? = null,
        val amount : Double? = null,
        val serviceRating :String? = null,
        val ratings :Float? = null,
        val costPerHour :String? = null,
        val cancelBy :String? = null,
        val reason :String? = null,
        val mediationRaised:String? = null,
        val extraHours :String ? = null,
        val hours :Double? = null,
        val paymentType  :String? = null,
        val cancelAfterTime :String? = null,
        val isQuotationAccepted:Boolean? = null,
        val isPaymentConfirmed :Boolean? = null,
        val startedByUser :Boolean? = null,
        val workEndByProvider :Boolean? = null,
        val reviewIsGiven :String? = null,
        val serviceRatingText :String? = null,
        val givenRating :Int ? = null,
        val mediationAmount: String? = null,
        val isQuotationSent :Boolean? = null,
        val providerStartTime:String? = null,
        val providerEndTime:String? = null,
        val mediationStatus :String? = null,
        val mediationAcceptAmount :String? = null,
        val customerTimeSlot :String? = "",
        val serviceType:String?  = null,
        val flags:ModelProviderBookingList.Flags? = null,
        val mediation: ModelProviderBookingList.Mediation? = null,
        val perHourAmount :Float? = null,
        val customer: ModelProviderBookingList.Customer? = null,
        val reviewList: ModelGetRating.Data? =null,
        val reviewTaken :Int ? = null,
        val providerImage:String? = null,
        val providerVerified:String? = null,
        val workerDetails: ModelProviderBookingList.Worker? = null,
        val serviceId:String? = null,
        val amountProceed:String?= null,
        val workerExperience :Int? = null,
        val minHourTime :Int? = null,
        val distance :String? = "",
        val customerLat:String? = null,
        val customerLong:String? = null,
        val isInspection:Boolean? = null,
        val inspectionRate :Double? = null,
        val inspectionId :String? = null,
        val problem :String?= null,
        val problemDescription :String?= null,
        val problemSolution :String? = null,
        val estimationCost:String? = null,
        val availableTime:String? = null,
        val serviceDescription:String? = null

    )

    data class PromoItem(
        val code: String = "",
        val title: String = "",
        val description: String = "",
        val image: String = "",
    )

    data class StatusUiState(
        val services: List<ServiceItem> = emptyList(),
        val selectedTab: Int = 0,
        val message: String? = "",
        val statusCode :Int? = null,
        val reasonId :String? = null,
        val text :String? = null,
        val isServiceStarted: Boolean = false,
        val mediationReason :String? = null,
        val mediationAmount  :String? = null,
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val paginationOffset: Int = 0,
        val isNavigateBack:Boolean? = null,
        val promoList : List<PromoItem> = emptyList(),
        val bookingId: String = "",
        val isErrorMsg: Boolean = false,
        val clientSecret: String? = null,
        val cardNumber: String? = null,
        val navigateBack: Boolean = false,
        val reviewMessage:String?= null,


    )

    private val _uiStates = MutableStateFlow(StatusUiState())
    val uiStates = _uiStates.asStateFlow()

    private val _bookingDetails = MutableStateFlow(BookingData())
    val bookingData: StateFlow<BookingData> = _bookingDetails.asStateFlow()

    private fun updateBookingId(bookingId: String) {
        _uiStates.update {
            it.copy(bookingId = bookingId)
        }
    }

    fun onTabSelected(index: Int) {
        _uiStates.update { state ->
            state.copy(selectedTab = index)
        }

        when (index) {
            0 -> loadInitialUpComingList()
            1 -> fetchInitialOnGoingList()
            2 -> loadInitialClosedBookings()
        }
    }
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    fun refreshBookings(tab: Int) {
        viewModelScope.launch {
            _isRefreshing.value = true

            when (tab) {
                0 -> loadInitialUpComingList()
                1 -> fetchInitialOnGoingList()
                2 -> loadInitialClosedBookings()
            }

            delay(300) // optional: ensures UI gets a frame to show refresh
            _isRefreshing.value = false
        }
    }


    private fun fetchInitialOnGoingList() {
        _uiStates.update {
            it.copy(
                services = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        fetchOnGoingList(0, true)
    }

    private fun loadInitialUpComingList() {
        _uiStates.update {
            it.copy(
                services = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        upComingList(0, true)
    }

    private fun loadInitialClosedBookings() {
        _uiStates.update {
            it.copy(
                services = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        loadClosedBookings(0, true)
    }

    fun loadMoreOnGoingList() {
        val currentState = _uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            fetchOnGoingList(currentState.paginationOffset, false)
        }
    }

    fun loadMoreUpComingList() {
        val currentState = _uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            upComingList(currentState.paginationOffset, false)
        }
    }

    fun loadMoreClosedBookings() {
        val currentState = _uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            loadClosedBookings(currentState.paginationOffset, false)
        }
    }

    private fun fetchOnGoingList(offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }

            val ongoingDeferred = async { onGoingList(offset) }
            val pendingDeferred = async { onPendingList(offset) }

            val ongoing = ongoingDeferred.await()
            val pending = pendingDeferred.await()

            val combinedServices = ongoing + pending
            val limitInt = PAGINATION_LIMIT
            val hasMoreData = combinedServices.size >= limitInt

            _uiStates.update { currentState ->
                val updatedServices = if (isInitialLoad) combinedServices else currentState.services + combinedServices
                val newOffset = if (isInitialLoad) combinedServices.size else currentState.paginationOffset + combinedServices.size

                currentState.copy(
                    services = updatedServices,
                    message = null,
                    isLoadingMore = false,
                    paginationOffset = newOffset,
                    hasMoreData = hasMoreData,
                )
            }
        }
    }

    private fun upComingList(offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }

            val request = ServiceListRequest(
                limit = PAGINATION_LIMIT.toString(),
                offset = offset.toString(),
            )

            when(val result = commonBookingsUseCases.getUpcomingList(request)) {
                is Success -> {
                    val services = result.data?.data?.map {
                        ServiceItem(
                            title = it.service.title,
                            priceRange = it.bill.total,
                            scheduleDate = it.booking.date,
                            scheduleTime = it.booking.time,
                            providerName = it.worker.firstName + it.worker.lastName,
                            address = it.address?.address?.fullAdd,
                            rating = it.worker.portfolio.ratings,
                            reviewCount = it.worker.portfolio.complted,
                            pricePerHour = it.cost.minHour,
                            status = Status.OrderStatus.CONFIRMED,
                            image = it.service.image,
                            bookingId = it.booking.id,
                            workerId = it.worker.id,
                            userId = it.customer.id,
                            id = it.worker.id,
                            workerName = it.worker.fullName,
                            customerId = it.customer.id,
                            percentage = it.bill.commission.percentage,
                            amount = it.bill.amount,
                            serviceRating = it.worker.portfolio.complted,
                            ratings = it.worker.portfolio.ratings,
                            costPerHour = it.cost.formatMinHr,
                            reason = it.cancellation?.reason,
                            cancelBy = it.cancellation?.cancelledBy,
                            mediationRaised = it.mediation.mediationRaised,
                            extraHours = it.additional.hours?.formatMinHrs,
                            hours = it.booking.hours,
                            paymentType = it.bill.payment.mode,
                            cancelAfterTime = it.flags.cancelAfterStartTime,
                            isQuotationAccepted = it.flags.isQuotationAccepted,
                            isQuotationSent = it.flags.isQuotationSent,
                            isPaymentConfirmed = it.flags.isPaymentConfirmed,
                            startedByUser = it.flags.jobStartedByUser,
                            workEndByProvider = it.flags.jobEndedByWorker,
                            reviewIsGiven = if (it.review?.given != null) "true" else "false",
                            serviceRatingText = it.review?.given?.serviceRatingText,
                            givenRating = it.review?.given?.serviceRating,
                            mediationAmount = it.mediation.mediationAmount,
                            providerStartTime = it.times.firstOrNull()?.startTime,
                            providerEndTime = it.times.firstOrNull()?.endTime,
                            mediationStatus = it.mediation.mediationStatus,
                            mediationAcceptAmount = it.mediation.mediationAcceptAmount,
                            customerTimeSlot = it.customerTimeSlot,
                            perHourAmount = it.cost.rate,
                            workerDetails = it.worker,
                            serviceId = it.service.id,
                            providerImage = it.worker.profile,
                            providerVerified = it.worker.verificationStatus,
                            amountProceed = it.mediation.amountProcessed,
                            workerExperience = it.worker.experience,
                            minHourTime = it.cost.minHour,
                            distance = it.distance,
                            customerLong = it.address?.lng,
                            customerLat = it.address?.lat,
                            isInspection = it.flags.isInspection,
                            inspectionRate = it.service.inspectionRate,
                            inspectionId = it.inspectionReport?.id,
                            problem = it.inspectionReport?.problem,
                            problemSolution = it.inspectionReport?.problemSolution,
                            problemDescription = it.inspectionReport?.problemDescription,
                            estimationCost = it.inspectionReport?.inspectionEstimation,
                            availableTime = it.inspectionReport?.availableTime,
                            serviceDescription = it.service.description
                        )
                    } ?: emptyList()

                    val limitInt = PAGINATION_LIMIT
                    val hasMoreData = services.size >= limitInt

                    _uiStates.update { currentState ->
                        val updatedServices = if (isInitialLoad) services else currentState.services + services
                        val newOffset = if (isInitialLoad) services.size else currentState.paginationOffset + services.size

                        currentState.copy(
                            services = updatedServices,
                            message = null,
                            isLoadingMore = false,
                            paginationOffset = newOffset,
                            hasMoreData = hasMoreData,
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            services = if (isInitialLoad) emptyList() else it.services,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun onGoingList(offset: Int): List<ServiceItem> {
        val request = ServiceListRequest(
            limit = PAGINATION_LIMIT.toString(),
            offset = offset.toString(),
        )

        return when(val result = commonBookingsUseCases.getOngoingList(request)) {
            is Success -> {
                result.data?.data?.map {
                    ServiceItem(
                        title = it.service.title,
                        priceRange = it.bill.total,
                        scheduleDate = it.booking.date,
                        scheduleTime = it.booking.time,
                        providerName = it.worker.firstName + it.worker.lastName,
                        address = it.address?.address?.fullAdd,
                        rating = it.worker.portfolio.ratings,
                        reviewCount = it.worker.portfolio.complted,
                        pricePerHour = it.cost.minHour,
                        status = Status.OrderStatus.IN_PROGRESS,
                        image = it.service.image,
                        id = it.worker.id,
                        workerName = it.worker.fullName,
                        customerId = it.customer.id,
                        bookingId = it.booking.id,
                        workerId = it.worker.id,
                        userId = it.customer.id,
                        percentage = it.bill.commission.percentage,
                        amount = it.bill.amount,
                        serviceRating = it.worker.portfolio.complted,
                        ratings = it.worker.portfolio.ratings,
                        costPerHour = it.cost.formatMinHr,
                        reason = it.cancellation?.reason,
                        cancelBy = it.cancellation?.cancelledBy,
                        mediationRaised = it.mediation.mediationRaised,
                        extraHours = it.additional.hours?.formatMinHrs,
                        hours = it.booking.hours,
                        paymentType = it.bill.payment.mode,
                        cancelAfterTime = it.flags.cancelAfterStartTime,
                        isQuotationAccepted = it.flags.isQuotationAccepted,
                        isQuotationSent = it.flags.isQuotationSent,
                        isPaymentConfirmed = it.flags.isPaymentConfirmed,
                        startedByUser = it.flags.jobStartedByUser,
                        workEndByProvider = it.flags.jobEndedByWorker,
                        reviewIsGiven = if (it.review?.given != null) "true" else "false",
                        serviceRatingText = it.review?.given?.serviceRatingText,
                        givenRating = it.review?.given?.serviceRating,
                        mediationAmount = it.mediation.mediationAmount,
                        providerStartTime = it.times.firstOrNull()?.startTime,
                        providerEndTime = it.times.firstOrNull()?.endTime,
                        mediationStatus = it.mediation.mediationStatus,
                        mediationAcceptAmount = it.mediation.mediationAcceptAmount,
                        customerTimeSlot = it.customerTimeSlot,
                        perHourAmount = it.cost.rate,
                        providerImage = it.worker.profile,
                        providerVerified = it.worker.verificationStatus,
                        workerDetails = it.worker,
                        serviceId = it.service.id,
                        amountProceed = it.mediation.amountProcessed,
                        workerExperience = it.worker.experience,
                        minHourTime = it.cost.minHour,
                        distance = it.distance,
                        customerLong = it.address?.lng,
                        customerLat = it.address?.lat,
                        isInspection = it.flags.isInspection,
                        inspectionRate = it.service.inspectionRate,
                        inspectionId = it.inspectionReport?.id,
                        problem = it.inspectionReport?.problem,
                        problemSolution = it.inspectionReport?.problemSolution,
                        problemDescription = it.inspectionReport?.problemDescription,
                        estimationCost = it.inspectionReport?.inspectionEstimation,
                        availableTime = it.inspectionReport?.availableTime,
                        serviceDescription = it.service.description

                    )
                } ?: emptyList()
            }
            is JtApiResult.Failure -> {
                _uiStates.update {
                    it.copy(
                        message = result.error.message
                    )
                }
                emptyList()
            }
        }
    }

    private suspend fun onPendingList(offset: Int): List<ServiceItem> {
        val request = ServiceListRequest(
            limit = PAGINATION_LIMIT.toString(),
            offset = offset.toString(),
        )

        return when (val result = commonBookingsUseCases.getPendingList(request)){
            is Success -> {
                result.data.data.map {
                    ServiceItem(
                        title = it.service.title,
                        priceRange = it.bill.total,
                        scheduleDate = it.booking.date,
                        scheduleTime = it.booking.time,
            //                        rating = it.worker.portfolio.ratings,
                        address = it.address?.address?.fullAdd,
                        pricePerHour = it.cost.minHour,
                        status = Status.OrderStatus.SEARCHING,
                        image = it.service.image,
            //                        id = it.worker.id,
            //                        workerName = it.worker.fullName,
                        customerId = it.customer.id,
                        bookingId = it.booking.id,
            //                        workerId = it.worker.id,
                        userId = it.customer.id,
                        percentage = it.bill.commission.percentage,
                        amount = it.bill.amount,
            //                        ratings = it.worker.portfolio.ratings,
                        costPerHour = it.cost.formatMinHr,
                        reason = it.cancellation?.reason,
                        cancelBy = it.cancellation?.cancelledBy,
                        mediationRaised = it.mediation.mediationRaised,
                        extraHours = it.additional.hours?.formatMinHrs,
                        hours = it.booking.hours,
                        paymentType = it.bill.payment.mode,
                        cancelAfterTime = it.flags.cancelAfterStartTime,
                        isQuotationAccepted = it.flags.isQuotationAccepted,
                        isQuotationSent = it.flags.isQuotationSent,
                        isPaymentConfirmed = it.flags.isPaymentConfirmed,
                        startedByUser = it.flags.jobStartedByUser,
                        workEndByProvider = it.flags.jobEndedByWorker,
                        serviceRatingText = it.review?.given?.serviceRatingText,
                        givenRating = it.review?.given?.serviceRating,
                        mediationAmount = it.mediation.mediationAmount,
                        providerStartTime = it.times.firstOrNull()?.startTime,
                        providerEndTime = it.times.firstOrNull()?.endTime,
                        mediationStatus = it.mediation.mediationStatus,
                        mediationAcceptAmount = it.mediation.mediationAcceptAmount,
                        customerTimeSlot = it.customerTimeSlot,
                        perHourAmount = it.cost.rate,
            //                        providerImage = it.worker.profile,
            //                        providerVerified = it.worker.verificationStatus,
            //                        workerDetails = it.worker,
                        serviceId = it.service.id,
                        amountProceed = it.mediation.amountProcessed,
                        workerExperience = it.worker.experience,
                        minHourTime = it.cost.minHour,
                        distance = it.distance,
                        customerLong = it.address?.lng,
                        customerLat = it.address?.lat,
                        isInspection = it.flags.isInspection,
                        inspectionRate = it.service.inspectionRate,
                        inspectionId = it.inspectionReport?.id,
                        problem = it.inspectionReport?.problem,
                        problemSolution = it.inspectionReport?.problemSolution,
                        problemDescription = it.inspectionReport?.problemDescription,
                        estimationCost = it.inspectionReport?.inspectionEstimation,
                        availableTime = it.inspectionReport?.availableTime,
                        serviceDescription = it.service.description


                    )
                }
            }

            is JtApiResult.Failure -> {
                _uiStates.update {
                    it.copy(
                        message = result.error.message
                    )
                }
                emptyList()
            }
        }
    }

    private fun loadClosedBookings(offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }

            val request = ServiceListRequest(
                limit = PAGINATION_LIMIT.toString(),
                offset = offset.toString(),
            )

            val completedDeferred = async { commonBookingsUseCases.completedBookings(request)}
            val cancelledDeferred = async { commonBookingsUseCases.cancelledBookings(request) }

            val completedResult = completedDeferred.await()
            val cancelledResult = cancelledDeferred.await()

            val allServices = mutableListOf<ServiceItem>()
            var errorMessage: String? = null

            when (cancelledResult) {
                is Success -> {
                    val cancelledServices = cancelledResult.data.data.map {
                        ServiceItem(
                            title = it.service.title,
                            priceRange = it.bill.amount,
                            scheduleDate = it.booking.date,
                            bookingId = it.booking.id,
                            scheduleTime = it.booking.time,
                            providerName = it.worker.fullName,
                            address = it.address?.address?.line1 + it.address?.address?.state,
                            rating = it.worker.portfolio.ratings,
                            reviewCount = it.worker.portfolio.complted,
                            pricePerHour = it.cost.minHour,
                            status = Status.OrderStatus.CANCELLED,
                            workerName = it.worker.fullName,
                            customerId = it.customer.id,
                            percentage = it.bill.commission.percentage,
                            amount = it.bill.amount,
                            serviceRating = it.worker.portfolio.complted,
                            ratings = it.worker.portfolio.ratings,
                            costPerHour = it.cost.formatMinHr,
                            reason = it.cancellation?.reason,
                            cancelBy = it.cancellation?.cancelledBy,
                            mediationRaised = it.mediation.mediationRaised,
                            extraHours = it.additional.hours?.formatMinHrs,
                            paymentType = it.bill.payment.mode,
                            cancelAfterTime = it.flags.cancelAfterStartTime,
                            isQuotationAccepted = it.flags.isQuotationAccepted,
                            isQuotationSent = it.flags.isQuotationSent,
                            isPaymentConfirmed = it.flags.isPaymentConfirmed,
                            startedByUser = it.flags.jobStartedByUser,
                            workEndByProvider = it.flags.jobEndedByWorker,
                            reviewIsGiven = if (it.review?.given != null) "true" else "false",
                            serviceRatingText = it.review?.given?.serviceRatingText,
                            givenRating = it.review?.given?.serviceRating,
                            mediationAmount = it.mediation.mediationAmount,
                            providerStartTime = it.times.firstOrNull()?.startTime,
                            providerEndTime = it.times.firstOrNull()?.endTime,
                            mediationStatus = it.mediation.mediationStatus,
                            mediationAcceptAmount = it.mediation.mediationAcceptAmount,
                            customerTimeSlot = it.customerTimeSlot,
                            perHourAmount = it.cost.rate,
                            image = it.service.image,
                            reviewList = it.review,
                            providerImage = it.worker.profile,
                            providerVerified = it.worker.verificationStatus,
                            workerDetails = it.worker,
                            serviceId = it.service.id,
                            amountProceed = it.mediation.amountProcessed,
                            workerExperience = it.worker.experience,
                            minHourTime = it.cost.minHour,
                            distance = it.distance,
                            customerLong = it.address?.lng,
                            customerLat = it.address?.lat,
                            isInspection = it.flags.isInspection,
                            inspectionRate = it.service.inspectionRate,
                            inspectionId = it.inspectionReport?.id,
                            problem = it.inspectionReport?.problem,
                            problemSolution = it.inspectionReport?.problemSolution,
                            problemDescription = it.inspectionReport?.problemDescription,
                            estimationCost = it.inspectionReport?.inspectionEstimation,
                            availableTime = it.inspectionReport?.availableTime,
                            serviceDescription = it.service.description


                        )
                    }
                    allServices.addAll(cancelledServices)
                }
                is JtApiResult.Failure -> {
                    errorMessage = cancelledResult.error.message
                }
            }

            when (completedResult) {
                is Success -> {
                    val completedData = completedResult.data.data

                    val completedServices = completedData.map {
                        ServiceItem(
                            title = it.service.title,
                            priceRange = it.bill.total,
                            scheduleDate = it.booking.date,
                            scheduleTime = it.booking.time,
                            providerName = "${it.worker.firstName} ${it.worker.lastName}".trim(),
                            address = it.address?.address?.fullAdd,
                            rating = it.worker.portfolio.ratings,
                            reviewCount = it.worker.portfolio.complted,
                            pricePerHour = it.cost.minHour,
                            status = Status.OrderStatus.COMPLETED,
                            workerName = it.worker.fullName,
                            customerId = it.customer.id,
                            bookingId = it.booking.id,
                            workerId = it.worker.id,
                            userId = it.customer.id,
                            reason = it.cancellation?.reason,
                            cancelBy = it.cancellation?.cancelledBy,
                            percentage = it.bill.commission.percentage,
                            amount = it.bill.amount,
                            mediationRaised = it.mediation.mediationRaised,
                            extraHours = it.additional.hours?.formatMinHrs,
                            hours = it.booking.hours,
                            paymentType = it.bill.payment.mode,
                            cancelAfterTime = it.flags.cancelAfterStartTime,
                            isQuotationAccepted = it.flags.isQuotationAccepted,
                            isQuotationSent = it.flags.isQuotationSent,
                            isPaymentConfirmed = it.flags.isPaymentConfirmed,
                            startedByUser = it.flags.jobStartedByUser,
                            workEndByProvider = it.flags.jobEndedByWorker,
                            reviewIsGiven = if (it.review?.given != null) "true" else "false",
                            serviceRatingText = it.review?.given?.serviceRatingText,
                            givenRating = it.review?.given?.serviceRating,
                            mediationAmount = it.mediation.mediationAmount,
                            providerStartTime = it.times.firstOrNull()?.startTime,
                            providerEndTime = it.times.firstOrNull()?.endTime,
                            mediationStatus = it.mediation.mediationStatus,
                            mediationAcceptAmount = it.mediation.mediationAcceptAmount,
                            customerTimeSlot = it.customerTimeSlot,
                            costPerHour = it.cost.formatMinHr,
                            perHourAmount = it.cost.rate,
                            image = it.service.image,
                            reviewTaken = it.review?.taken?.serviceRating,
                            reviewList = it.review,
                            providerImage = it.worker.profile,
                            providerVerified = it.worker.verificationStatus,
                            workerDetails = it.worker,
                            serviceId = it.service.id,
                            amountProceed = it.mediation.amountProcessed,
                            workerExperience = it.worker.experience,
                            minHourTime = it.cost.minHour,
                            distance = it.distance,
                            customerLong = it.address?.lng,
                            customerLat = it.address?.lat,
                            isInspection = it.flags.isInspection,
                            inspectionRate = it.service.inspectionRate,
                            inspectionId = it.inspectionReport?.id,
                            problem = it.inspectionReport?.problem,
                            problemSolution = it.inspectionReport?.problemSolution,
                            problemDescription = it.inspectionReport?.problemDescription,
                            estimationCost = it.inspectionReport?.inspectionEstimation,
                            availableTime = it.inspectionReport?.availableTime,
                            serviceDescription = it.service.description

                        )
                    }

                    allServices.addAll(completedServices)
                }
                is JtApiResult.Failure -> {
                    errorMessage = completedResult.error.message
                }
            }


            val hasMoreData = allServices.size >= PAGINATION_LIMIT

            _uiStates.update { currentState ->
                val updatedServices = if (isInitialLoad) allServices else currentState.services + allServices
                val newOffset = if (isInitialLoad) PAGINATION_LIMIT else currentState.paginationOffset + PAGINATION_LIMIT

                currentState.copy(
                    services = updatedServices,
                    message = errorMessage,
                    isLoadingMore = false,
                    paginationOffset = newOffset,
                    hasMoreData = hasMoreData,
                )
            }
        }
    }


    fun startBooking(booking:String) {
        val request = StartBookingRequest(
            booking = booking
        )
        viewModelScope.launch {
            when (val result = userBookingsUseCases.startBooking(payload = request) ){
                is Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data?.meta?.message?:"Booking Start Conformation",
                            isServiceStarted = true,
                            isNavigateBack = true
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            isServiceStarted = false,
                            isNavigateBack = false
                        )
                    }
                }
            }
        }
    }

    fun completeRequestBooking(
        booking:String,
        onSuccess: () -> Unit = {}
    ) {
        val request = StartBookingRequest(
            booking = booking
        )
        viewModelScope.launch {
            when (val result = userBookingsUseCases.completeRequestBooking(payload = request)) {
                is Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data?.meta?.message?:"",
                            isNavigateBack = true
                        )
                    }
                    onSuccess()
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            isNavigateBack = false
                        )
                    }
                }
            }
        }
    }


    fun fetchBookingDetails(bookingId: String, isEphemeralRequired: Boolean = false) {
        viewModelScope.launch {

            val response = commonBookingsUseCases.getBookingDetails(BookingDetailsRequest(bookingId))

            when (response) {
                is Success -> {
                    updateBookingId(bookingId)
                    if (response.data != null){
                        _bookingDetails.value = mapToBookingDataProvider(response.data)
                    }else{
                        _bookingDetails.value = BookingData()
                    }
                    if (isEphemeralRequired) getEphemeralKey()
                }
                is JtApiResult.Failure -> {
                    _uiStates.value = _uiStates.value.copy(
                        message = response.error.message
                    )
                }
            }
        }
    }
    private fun mapToBookingDataProvider(apiData: BookingDetailResponse): BookingData {
        val data = apiData.data
        return BookingData(
            status = Status.OrderStatus.fromValue(data.flags.type)?.status ?: "",
            title = data.service.title,
            address = data.address?.address?.fullAdd.orEmpty(),
            priceRange = data.bill.total.toString(),
            image = data.service.image,
            date = data.booking.date,
            time = data.booking.time,
            customerId = data.customer.id,
            workerName = data.worker.fullName,
            serverId = data.worker.id.orEmpty(),
            percentage = data.bill.commission.percentage,
            amount = data.bill.amount,
            workerId = data.worker.id.orEmpty(),
            serviceRating = data.worker.portfolio.complted,
            ratings = data.worker.portfolio.ratings,
            costPerHour = data.cost.totalCost.toString(),
            cancelBy = data.cancellation?.cancelledBy,
            reason = data.cancellation?.reason,
            bookingId = data.booking.id,
            mediationRaise = data.mediation?.mediationRaised,
            extraHours = data.additional.hours?.formatMinHrs,
            hours = data.booking.hours,
            paymentType = data.bill.payment.mode,
            cancelAfterTime = data.flags.cancelAfterStartTime,
            isQuotationAccepted = data.flags.isQuotationAccepted,
            isPaymentConfirmed = data.flags.isPaymentConfirmed,
            startedByUser = data.flags.jobStartedByUser,
            workEndByProvider = data.flags.jobEndedByWorker,
            reviewIsGiven = if (data.review?.given != null) "true" else "false",
            serviceRatingText = data.review?.given?.serviceRatingText,
            givenRating = data.review?.given?.serviceRating,
            mediationAmount = data.mediation?.mediationAmount,
            isQuotationSent = data.flags.isQuotationSent,
            providerStartTime = data.times.firstOrNull()?.startTime,
            providerEndTime = data.times.firstOrNull()?.endTime,
            mediationStatus = data.mediation?.mediationStatus,
            mediationAcceptAmount = data.mediation?.mediationAcceptAmount,
            rating = data.worker.portfolio.ratings,
            perHourAmount = data.cost.rate,
            reviewTaken = data.review?.taken?.serviceRating,
            reviewList = data.review,
            providerImage = data.worker.profile,
            providerVerified = data.worker.verificationStatus,
            workerDetails = data.worker,
            serviceId = data.service.id,
            amountProceed = data.mediation?.amountProcessed,
            workerExperience = data.worker.experience,
            minHourTime = data.cost.minHour,
            customerLong = data.address?.lng,
            customerLat = data.address?.lat,
            isInspection = data.flags.isInspection,
            isInspectionRate = data.service.inspectionRate,
            inspectionId = data.inspectionReport?.id,
            problem = data.inspectionReport?.problem,
            problemSolution = data.inspectionReport?.problemSolution,
            problemDescription = data.inspectionReport?.problemDescription,
            estimationCost = data.inspectionReport?.inspectionEstimation,
            availableTime = data.inspectionReport?.availableTime,
            serviceDescription = data.service.description
        )
    }
    fun onReviewMessage(reason: String) {
        _uiStates.update {
            it.copy(reviewMessage = reason)
        }
    }

    fun addRating(
        bookingId: String,
        serviceRating: String,
        rating: String?,
        review: String? = null,
        onSuccess: () -> Unit = {},
    ) {
        val serviceRatingNumber = convertRatingToNumber(serviceRating)
        val honestyRatingNumber = convertRatingToNumber(rating)

        val request = AddRatingRequest(
            booking = bookingId,
            rating = honestyRatingNumber,
            serviceRating = serviceRatingNumber,
            review = review
        )

        viewModelScope.launch {
            when (val result = commonBookingsUseCases.addRating(payload = request)) {
                is Success -> {
                    val message = result.data?.meta?.message ?: ""
                    val isSuccess = message.contains("successfully", ignoreCase = true)

                    if (isSuccess){
                        onSuccess()
                        _uiStates.update {
                            it.copy(
                                message = message,
                                isNavigateBack = isSuccess
                            )
                        }
                    }else{
                        _uiStates.update {
                            it.copy(
                                message = message,
                                isNavigateBack = isSuccess
                            )
                        }
                    }

                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            isNavigateBack = false
                        )
                    }
                }
            }
        }
    }

    fun updateRating(
        bookingId: String,
        serviceRating: String,
        rating: String?,
        review: String? = null,
        onSuccess: () -> Unit = {},
    ) {
        val serviceRatingNumber = convertRatingToNumber(serviceRating)
        val honestyRatingNumber = convertRatingToNumber(rating)

        val request = AddRatingRequest(
            booking = bookingId,
            rating = honestyRatingNumber,
            serviceRating = serviceRatingNumber,
            review = review
        )

        viewModelScope.launch {
            when (val result = commonBookingsUseCases.updateRating(payload = request)) {
                is Success -> {
                    val message = result.data?.meta?.message ?: ""
                    val isSuccess = message.contains("successfully", ignoreCase = true)

                    if (isSuccess){
                        onSuccess()
                        _uiStates.update {
                            it.copy(
                                message = message,
                                isNavigateBack = isSuccess
                            )
                        }
                    }else{
                        _uiStates.update {
                            it.copy(
                                message = message,
                                isNavigateBack = isSuccess
                            )
                        }
                    }

                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            isNavigateBack = false
                        )
                    }
                }
            }
        }
    }

    private fun convertRatingToNumber(rating: String?): String? {
        return when (rating) {
            "Bad" -> "1"
            "Poor" -> "2"
            "Average" -> "3"
            "Great" -> "4"
            "Excellent" -> "5"
            else -> null
        }
    }

    fun onMediationReasonChange(reason: String) {
        _uiStates.update {
            it.copy(mediationReason = reason)
        }
    }

    fun onMediationAmountChange(amount: String) {
        _uiStates.update {
            it.copy(mediationAmount = amount)
        }
    }

    fun raiseMediation(
        bookingId: String,
        mediationReason: String,
        mediationAmount: String
    ){
        viewModelScope.launch {
            val request = RaiseMediationRequest(
                booking = bookingId,
                mediationReason = mediationReason,
                mediationAmount = mediationAmount
            )
            when(val result = userBookingsUseCases.raiseMediation(request)){
                is Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta.message,
                            isNavigateBack = true
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    val errorMessage = result.error.message
                    _uiStates.update {
                        it.copy(message = errorMessage,
                            isNavigateBack = false)
                    }
                }
            }
        }
    }

    fun manageQuotation(
        bookingId: String,
        bookingTime:String,
        status:String,
        userTime:String
    ){
        viewModelScope.launch {
            val request = ManageQuotationRequest(
                bookingId = bookingId,
                bookingTime = bookingTime ,
                status = status,
                userTime = userTime
            )
            when (val result = commonBookingsUseCases.manageQuotation(request)) {

                is Success -> {
                    _uiStates.update {
                        it.copy(
                            isNavigateBack = true,
                            message = result.data.meta?.message
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    val errorMessage = result.error.message
                    _uiStates.update {
                        it.copy(message = errorMessage, isNavigateBack = false)
                    }
                }
            }
        }
    }

    fun clearMessage(){
        _uiStates.update {
            it.copy(message = null)
        }
    }

    fun getPromoCodesList(booking:String){
        viewModelScope.launch {
            val request = PromoCodeListRequest(
                bookingId = booking
            )
            when (val result = userBookingsUseCases.getPromoCodesList(request)) {
                is Success -> {
                    val promoItems = result.data.data.map { data ->
                        PromoItem(
                            code = data.code,
                            title = data.title,
                            description = data.description,
                            image = data.image,
                        )
                    }

                    _uiStates.update {
                        it.copy(
                            promoList = promoItems,
                            message = null
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    val errorMessage = result.error.message
                    _uiStates.update {
                        it.copy(message = errorMessage)
                    }
                }
            }
        }
    }

    // Stripe Apis
    private fun getEphemeralKey() {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.getEphemeralKey()) {
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message, isErrorMsg = true)
                    }
                }

                is Success -> {
                    val ephemeralModel = result.data.data

                    StripeUiBridgeRegistry.bridge?.showStripeBillingScreen(ephemeralModel)

//                    StripeBridgeRegistry.bridge?.initCustomerSession(ephemeralModel)
                    attachStripePlatformListeners()
                }

            }
        }
    }

    private fun attachStripePlatformListeners(){
// Register a default handler to receive events back from platform.
        StripeBridgeRegistry.registerCallbacks(object : PaymentCallbacks {
            override fun onRequestInitialize(amountInMinorUnits: Long, currency: String) {
                println("$tag onRequestInitialize--$amountInMinorUnits-$currency")
            }
            override fun onRequestPresentPaymentMethods() {
                println("$tag onRequestPresentPaymentMethods--")

            }
            override fun onRequestAddPaymentMethod() {
                println("$tag onRequestAddPaymentMethod---")

            }
            override fun onRequestProcessPayment() {
                println("$tag onRequestProcessPayment--")

            }

            override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                println("$tag onCommunicatingStateChanged--$isCommunicating")
                //  _ui.value = _ui.value.copy(isCommunicating = isCommunicating)
            }
            override fun onPaymentMethodSelected(pm: SharedPaymentMethod?) {
                println("$tag onPaymentMethodSelected--$pm")
                StripeBridgeRegistry.bridge?.selectPaymentMethod()
                //  _ui.value = _ui.value.copy(selectedMethod = pm)
            }
            override fun onPaymentSuccess(intentId: String?) {
                println("$tag onPaymentSuccess--$intentId")
                println("$tag STRIPE PAYMENT SUCCESS")
                println("$tag STRIPE INTENT ID --- -------> $intentId")
                println("$tag CURRENT BOOKING ID ---=======> ${uiStates.value.bookingId}")
                //  intentId?.let {  completePayment(bookingId = bookingId, stripeId = it) }
                // _ui.value = _ui.value.copy(paymentSucceeded = true)
            }
            override fun onPaymentFailure(errorMessage: String) {
                println("$tag onPaymentFailure---------->$errorMessage")
                // _ui.value = _ui.value.copy(lastError = errorMessage)
            }

            override fun callPaymentIntentAPI(paymentMethod: String) {
                println("$tag STRIPE SELECTED PAYMENT METHOD JSON ----------> $paymentMethod")
                sharedPreferenceUseCase.putPaymentMethod(paymentMethod)
                makePayment()

            }
            override fun callCompletePaymentAPI(stripeId: String,mode: String) {
                completePayment(stripeId,mode)
            }

            override fun savePaymentMethod(paymentMethod: String) {
                val json = Json.parseToJsonElement(paymentMethod)
                val cardJson = json.jsonObject["card"]
//                cardJson?.jsonObject?.get("expiryYear")
//                cardJson?.jsonObject?.get("expiryMonth")
                val last4 = cardJson?.jsonObject?.get("last4").toString().replace("\"","")
                val brand = cardJson?.jsonObject?.get("brand").toString().replace("\"","")
                _uiStates.update {
                    it.copy(cardNumber = "$brand-$last4")
                }
                sharedPreferenceUseCase.putPaymentMethod(paymentMethod)

            }

            override fun savePaymentMethodIOS(paymentMethod: String) {
                _uiStates.update {
                    it.copy(cardNumber = paymentMethod.replace(" ","-"))
                }
               // sharedPreferenceUseCase.putPaymentMethod(paymentMethod)

            }
        })
    }

    fun makePayment() {
        viewModelScope.launch {

            println("$tag BOOKING ID --------> ${uiStates.value.bookingId}")
            println("$tag PAYMENT METHOD ----------> ${sharedPreferenceUseCase.getPaymentMethod()}")

            when (val result = commonPaymentUseCase.makePayment(uiStates.value.bookingId,"n",null)) {
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message, isErrorMsg = true)
                    }
                }

                is Success -> {
                    val paymentResult = result.data.data


                    println("$tag PAYMENT INTENT FULL RESPONSE ---> ${result.data}")
                    println("$tag PAYMENT INTENT ID ---> ${paymentResult.id}")
                    println("$tag BOOKING COMPLETED? ---> ${result.data.meta.bookingCompleted}")


                    println("$tag $paymentResult")

                    if (result.data.meta.bookingCompleted) {
                        _uiStates.update {
                            it.copy(message = result.data.meta.message, isErrorMsg = false)
                        }
                    } else {
                        _uiStates.update {
                            it.copy(clientSecret = result.data.data.id)
                        }
                        onRetrievePaymentIntent(paymentResult.id)
                    }
                }

            }
        }
    }

    fun completePayment(stripeId: String,mode: String) {
        viewModelScope.launch {
            println("$tag COMPLETE PAYMENT CALLED")
            println("$tag STRIPE ID --------------> $stripeId")
            println("$tag MODE --------> $mode")
            println("$tag BOOKING ID ------> ${uiStates.value.bookingId}")
            when (val result = commonPaymentUseCase.completePayment(uiStates.value.bookingId, payment = stripeId, mode = mode)) {
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message, isErrorMsg = true)
                    }
                }

                is Success -> {

                    _uiStates.update {
                        it.copy(message = result.data.meta.message, isErrorMsg = false, navigateBack = true)
                    }
                    println("$tag ${result.data}")
                }

            }
        }
    }

    private fun onRetrievePaymentIntent(clientSecret: String) = StripeBridgeRegistry.bridge?.retrievePaymentIntnt(clientSecret)
    fun onPayNow() = StripeBridgeRegistry.bridge?.processPayment(sharedPreferenceUseCase.getPaymentMethod())
    fun onSelectCard() = StripeBridgeRegistry.bridge?.selectPaymentMethod()

    fun onScreenExit() {
        StripeBridgeRegistry.unregisterCallbacks()
    }

    fun clearError(){
        _uiStates.update {
            it.copy(
                message = null
            )
        }
    }


    fun onClearNavigation() {
        _uiStates.update {
            it.copy(navigateBack = false)
        }
    }

}