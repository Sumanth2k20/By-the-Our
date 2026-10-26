package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.request.AcceptAndDeclineRequest
import com.bytheour.data.request.BookingDetailsRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.data.response.BookingDetailResponse
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.useCases.CommonBookingUseCase
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone


data class ProviderServiceItem(
    val title: String? = null,
    val priceRange: Double? = null,
    val scheduleDate: String? = null,
    val scheduleTime: String? = null,
    val providerName: String? = null,
    val address: String? = "",
    val status:String? = null ,
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
    val costPerHour :Double? = null,
    val serviceType:String?  = null,
    val flags:ModelProviderBookingList.Flags? = null,
    val mediation: ModelProviderBookingList.Mediation? = null,
    val customer: ModelProviderBookingList.Customer? = null,
    val reviewIsGiven :String? = null,
    val serviceRatingText :String? = null,
    val givenRating :Int ? = null,
    val bill: ModelProviderBookingList.Bill? = null,
    val cancellation:ModelProviderBookingList.Cancellation? = null,
    val reviewList: ModelGetRating.Data? =null,
    val addressList:ModelProviderBookingList.Address? = null,
    val serviceRatingValue:String? = null,
    val providerImage :String? = null,
    val extraHours :String ? = null,
    val hours :Double? = null,
    val mediationAmount: String? = null,
    val distance :String? = "",
    val customerLat:String? = null,
    val customerLong:String? = null,
    val workerDetails: ModelProviderBookingList.Worker? = null,
    val isInspection:Boolean? = null,
    val inspectionRate :Double? = null,
    val inspectionCalculatedAmount:Double? = null
)

class JTProviderBookingTabScreenViewmodel(
    private val providerBookingUseCases: ProviderBookingUseCases,
    private val commonBookingsUseCases: CommonBookingUseCase
) : ViewModel() {


    data class UIState(
        val message: String? = null,
        val bookingId: String? = null,
        val estimatedHours: String? = null,
        val priceType: String? = null,
        val startTimes: List<String> = emptyList(),
        val endTimes: List<String> = emptyList(),
        val fixedPrice: String? = null,
        val services: List<ProviderServiceItem> = emptyList(),
        val selectedTab: Int = 0,
        val statusCode: Int? = null,
        val reasonId: String? = null,
        val text: String? = null,
        val flags: List<ModelProviderBookingList.Flags> = emptyList(),
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val paginationOffset: Int = 0,
        val upcomingType:String? = null,
        val ongoingType:String? = null,
        val completedType:String? = null,
        val cancelType:String? = null,
        val isSuccess:Boolean = false,
        val isRefreshing: Boolean = false,
    )

    private val _uiStates = MutableStateFlow(UIState())
    val uiStates = _uiStates.asStateFlow()

    private val _bookingDetails = MutableStateFlow(BookingDataProvider())
    val bookingData: StateFlow<BookingDataProvider> = _bookingDetails.asStateFlow()


    fun onTabSelected(index: Int) {
        _uiStates.update { state ->
            state.copy(selectedTab = index)
        }

        when (index) {
            0 -> loadInitialUpcomingBookings(PAGINATION_LIMIT.toString())
            1 -> loadInitialOngoingBookings(PAGINATION_LIMIT.toString())
            2 -> loadInitialClosedBookings(PAGINATION_LIMIT.toString())
        }
    }

    fun clearMessage() {
        _uiStates.update { it.copy(message = null) }
    }

    fun loadInitialOngoingBookings(limit: String) {
        _uiStates.update {
            it.copy(
                services = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        loadOngoingBookings(limit, 0, isInitialLoad = true)
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshBookings(tab: Int) {
        viewModelScope.launch {
            _isRefreshing.value = true

            when (tab) {
                0 -> loadInitialUpcomingBookings(PAGINATION_LIMIT.toString())
                1 -> loadInitialOngoingBookings(PAGINATION_LIMIT.toString())
                2 -> loadInitialClosedBookings(PAGINATION_LIMIT.toString())
            }

            delay(300)
            _isRefreshing.value = false
        }
    }

    fun loadMoreOngoingBookings(limit: String) {
        val currentState = _uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            loadOngoingBookings(limit, currentState.paginationOffset, isInitialLoad = false)
        }
    }

    private fun loadOngoingBookings(limit: String, offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }

            val request = ServiceListRequest(
                limit = limit,
                offset = offset.toString()
            )

            when (val result = commonBookingsUseCases.getOngoingList(request)) {
                is Success -> {
                    val newServices = result.data?.data?.map {
                        ProviderServiceItem(
                            title = it.service.title,
                            priceRange = it.bill.total,
                            scheduleDate = it.booking.date,
                            scheduleTime = it.booking.time,
                            providerName = it.worker.firstName + it.worker.lastName,
                            address = it.address?.address?.fullAdd,
                            rating = it.worker.portfolio.ratings,
                            reviewCount = it.worker.portfolio.complted,
                            pricePerHour = it.cost.minHour,
                            status = it.flags.type,
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
                            costPerHour = it.cost.totalCost,
                            flags = it.flags,
                            customer = it.customer,
                            bill = it.bill,
                            addressList = it.address,
                            serviceRatingValue = (it.review?.taken?.rating ?: 0).toString(),
                            providerImage = it.worker.profile,
                            extraHours = it.additional.hours?.formatMinHrs,
                            hours = it.booking.hours,
                            mediationAmount = it.mediation.mediationAmount,
                            distance = it.distance,
                            customerLat = it.address?.lat,
                            customerLong = it.address?.lng,
                            workerDetails = it.worker,
                            isInspection = it.flags.isInspection,
                            inspectionRate = it.service.inspectionRate,
                            inspectionCalculatedAmount = it.bill.inspectionAmount
                        )
                    } ?: emptyList()

                    val limitInt = limit.toIntOrNull() ?: 0
                    val hasMoreData = newServices.size >= limitInt

                    _uiStates.update { state ->
                        val newList = if (isInitialLoad) newServices else state.services + newServices
                        val newOffset = if (isInitialLoad) newServices.size else state.paginationOffset + newServices.size

                        state.copy(
                            services = newList,
                            bookingId = result.data?.data?.firstOrNull()?.booking?.id,
                            ongoingType = result.data?.data?.firstOrNull()?.flags?.type,
                            paginationOffset = newOffset,
                            hasMoreData = hasMoreData,
                            isLoadingMore = false,
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message, isLoadingMore = false)
                    }
                }
            }
        }
    }

    fun loadInitialUpcomingBookings(limit: String) {
        _uiStates.update {
            it.copy(
                services = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        loadUpcomingBookings(limit, 0, isInitialLoad = true)
    }

    fun loadMoreUpcomingBookings(limit: String) {
        val currentState = _uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            loadUpcomingBookings(limit, currentState.paginationOffset, isInitialLoad = false)
        }
    }

    private fun loadUpcomingBookings(limit: String, offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }

            val request = ServiceListRequest(
                limit = limit,
                offset = offset.toString()
            )

            when (val result = commonBookingsUseCases.getUpcomingList(request)) {
                is Success -> {
                    val newServices = result.data?.data?.map {
                        ProviderServiceItem(
                            title = it.service.title,
                            priceRange = it.bill.total,
                            scheduleDate = it.booking.date,
                            scheduleTime = it.booking.time,
                            providerName = it.worker.firstName + it.worker.lastName,
                            address = it.address?.address?.fullAdd,
                            rating = it.worker.portfolio.ratings,
                            reviewCount = it.worker.portfolio.complted,
                            pricePerHour = it.cost.minHour,
                            status = it.flags.type,
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
                            costPerHour = it.cost.rate.toDouble(),
                            flags = it.flags,
                            customer = it.customer,
                            bill = it.bill,
                            addressList = it.address,
                            serviceRatingValue = (it.review?.taken?.rating ?: 0).toString(),
                            providerImage = it.worker.profile,
                            extraHours = it.additional.hours?.formatMinHrs,
                            hours = it.booking.hours,
                            distance = it.distance,
                            customerLat = it.address?.lat,
                            customerLong = it.address?.lng,
                            workerDetails = it.worker,
                            isInspection = it.flags.isInspection,
                            inspectionRate = it.service.inspectionRate,
                            inspectionCalculatedAmount = it.bill.inspectionAmount
                        )
                    } ?: emptyList()

                    val limitInt = limit.toIntOrNull() ?: 0
                    val hasMoreData = newServices.size >= limitInt

                    _uiStates.update { state ->
                        val newList = if (isInitialLoad) newServices else state.services + newServices
                        val newOffset = if (isInitialLoad) newServices.size else state.paginationOffset + newServices.size

                        state.copy(
                            services = newList,
                            bookingId = result.data?.data?.firstOrNull()?.booking?.id,
                            upcomingType = result.data?.data?.firstOrNull()?.flags?.type,
                            paginationOffset = newOffset,
                            hasMoreData = hasMoreData,
                            isLoadingMore = false,
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message, isLoadingMore = false)
                    }
                }
            }
        }
    }



    fun loadInitialClosedBookings(limit: String) {
        _uiStates.update {
            it.copy(
                services = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        loadClosedBookings(limit, 0, isInitialLoad = true)
    }

    fun loadMoreClosedBookings(limit: String) {
        val currentState = _uiStates.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            loadClosedBookings(limit, currentState.paginationOffset, isInitialLoad = false)
        }
    }

    fun loadClosedBookings(limit: String, offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isLoadingMore = true) }

            val request = ServiceListRequest(
                limit = limit,
                offset = offset.toString(),
            )

            val completedDeferred = async { commonBookingsUseCases.completedBookings(request) }
            val cancelledDeferred = async { commonBookingsUseCases.cancelledBookings(request) }

            val completedResult = completedDeferred.await()
            val cancelledResult = cancelledDeferred.await()

            val combinedList = mutableListOf<ProviderServiceItem>()

            // Cancelled Bookings
            if (cancelledResult is Success) {
                val cancelledServices = cancelledResult.data.data.map {
                    ProviderServiceItem(
                        title = it.service.title,
                        priceRange = it.bill.amount,
                        scheduleDate = it.booking.date,
                        scheduleTime = it.booking.time,
                        providerName = it.worker.fullName,
                        address = it.address?.address?.fullAdd,
                        rating = it.worker.portfolio.ratings,
                        reviewCount = it.worker.portfolio.complted,
                        pricePerHour = it.cost.minHour,
                        status = it.flags.type,
                        workerName = it.worker.fullName,
                        bookingId = it.booking.id,
                        customerId = it.customer.id,
                        percentage = it.bill.commission.percentage,
                        amount = it.bill.amount,
                        serviceRating = it.worker.portfolio.complted,
                        ratings = it.worker.portfolio.ratings,
                        costPerHour = it.cost.totalCost,
                        flags = it.flags,
                        customer = it.customer,
                        bill = it.bill,
                        cancellation = it.cancellation,
                        addressList = it.address,
                        image = it.service.image,
                        serviceRatingValue = (it.review?.taken?.rating?:0).toString(),
                        extraHours = it.additional.hours?.formatMinHrs,
                        hours = it.booking.hours,
                        providerImage = it.worker.profile,
                        distance = it.distance,
                        customerLat = it.address?.lat,
                        customerLong = it.address?.lng,
                        workerDetails = it.worker,
                        isInspection = it.flags.isInspection,
                        inspectionRate = it.service.inspectionRate,
                        inspectionCalculatedAmount = it.bill.inspectionAmount
                    )
                }

                combinedList.addAll(cancelledServices)
                _uiStates.update { currentState ->
                    currentState.copy(
                        cancelType = cancelledResult.data.data.map { it.flags.type }.firstOrNull(),
                        bookingId = cancelledResult.data.data.firstOrNull()?.booking?.id,
                    )
                }
            } else if (cancelledResult is JtApiResult.Failure) {
                _uiStates.update {
                    it.copy(
                        message = cancelledResult.error.message
                    )
                }
            }

            // Completed Bookings
            if (completedResult is Success) {
                val completedServices = completedResult.data.data.map {
                    ProviderServiceItem(
                        title = it.service.title,
                        priceRange = it.bill.total,
                        scheduleDate = it.booking.date,
                        scheduleTime = it.booking.time,
                        providerName = it.worker.firstName + it.worker.lastName,
                        address = it.address?.address?.fullAdd,
                        rating = it.worker.portfolio.ratings,
                        reviewCount = it.worker.portfolio.complted,
                        pricePerHour = it.cost.minHour,
                        status = it.flags.type,
                        workerName = it.worker.fullName,
                        customerId = it.customer.id,
                        bookingId = it.booking.id,
                        workerId = it.worker.id,
                        userId = it.customer.id,
                        flags = it.flags,
                        mediation = it.mediation,
                        customer = it.customer,
                        reviewIsGiven = if (it.review?.given != null) "true" else "false",
                        serviceRatingText = it.review?.given?.serviceRatingText,
                        givenRating = it.review?.given?.serviceRating,
                        bill = it.bill,
                        reviewList = it.review,
                        addressList = it.address,
                        image = it.service.image,
                        serviceRatingValue = (it.review?.taken?.rating?:0).toString(),
                        extraHours = it.additional.hours?.formatMinHrs,
                        hours = it.booking.hours,
                        providerImage = it.worker.profile,
                        distance = it.distance,
                        customerLat = it.address?.lat,
                        customerLong = it.address?.lng,
                        workerDetails = it.worker,
                        isInspection = it.flags.isInspection,
                        inspectionRate = it.service.inspectionRate,
                        inspectionCalculatedAmount = it.bill.inspectionAmount


                    )
                }

                combinedList.addAll(completedServices)
                _uiStates.update { currentState ->
                    currentState.copy(
                        completedType = completedResult.data.data.map { it.flags.type }.firstOrNull(),
                        bookingId = completedResult.data.data.firstOrNull()?.booking?.id,
                    )
                }
            } else if (completedResult is JtApiResult.Failure) {
                _uiStates.update {
                    it.copy(
                        message = completedResult.error.message
                    )
                }
            }

            val limitInt = limit.toIntOrNull() ?: 0
            val hasMoreData = combinedList.size >= limitInt

            _uiStates.update { state ->
                val newList = if (isInitialLoad) combinedList else state.services + combinedList
                val newOffset = if (isInitialLoad) combinedList.size else state.paginationOffset + combinedList.size

                state.copy(
                    services = newList,
                    paginationOffset = newOffset,
                    hasMoreData = hasMoreData,
                    isLoadingMore = false,
                )
            }
        }
    }

    fun startJob(
        bookingId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val request = StartBookingRequest(
                booking = bookingId,
            )

            when (val result = providerBookingUseCases.startJob(request = request)) {
                is Success -> {
                    val message = result.data.meta.message.orEmpty()
                    _uiStates.update {
                        it.copy(message = message)
                    }
                    if (message.contains("successfully", ignoreCase = true)) {
                        _uiStates.update { it.copy(isSuccess = true) }
                        onSuccess()
                    }else{
                        _uiStates.update { it.copy(message = result.data.meta.message) }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun endJob(
        bookingId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val request = StartBookingRequest(
                booking = bookingId,
            )

            when (val result = providerBookingUseCases.endJob(request = request)) {
                is Success -> {
                    val message = result.data.meta.message.orEmpty()
                    _uiStates.update {
                        it.copy(message = message)
                    }
                    if (message.contains("successfully", ignoreCase = true)) {
                        _uiStates.update { it.copy(isSuccess = true,message = message) }
                        onSuccess()
                    }else{
                        _uiStates.update { it.copy(message = result.data.meta.message) }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun fetchBookingDetails(bookingId: String) {
        viewModelScope.launch {

            val response = commonBookingsUseCases.getBookingDetails(BookingDetailsRequest(bookingId))

            when (response) {
                is Success -> {
                    if (response.data != null){
                        _bookingDetails.value = mapToBookingDataProvider(response.data)
                    }else{
                        _bookingDetails.value = BookingDataProvider()
                    }

                }

                is JtApiResult.Failure -> {
                    _uiStates.value.copy(message = response.error.message)
                }
            }
        }
    }
    private fun mapToBookingDataProvider(apiData: BookingDetailResponse): BookingDataProvider {
        val data = apiData.data

        return BookingDataProvider(
            status = data.flags.type,
            title = data.service.title,
            address = data.address?.address?.fullAdd.orEmpty(),
            priceRange = data.bill.total.toString(),
            image = data.service.image,
            date = data.booking.date,
            time = data.booking.time,
            customerId = data.customer.id,
            workerName = data.worker.fullName,
            serverId = data.worker.id?:"",
            percentage = data.bill.commission.percentage,
            amount = data.bill.amount,
            workerId = data.worker.id,
            serviceRating = data.worker.portfolio.complted,
            ratings = data.worker.portfolio.ratings,
            costPerHour = data.cost.rate.toDouble(),
            bookingId = data.booking.id,
            flags = data.flags,
            mediation = data.mediation,
            customer = data.customer,
            reviewIsGiven = if (data.review?.given != null) "true" else "false",
            serviceRatingText = data.review?.given?.serviceRatingText,
            givenRating = data.review?.given?.serviceRating,
            bill = data.bill,
            cancellation = data.cancellation,
            reviewList = data.review,
            addressList = data.address,
            extraHours = data.additional.hours?.formatMinHrs,
            hours = data.booking.hours,
            providerImage = data.worker.profile,
            paymentType =  data.bill.payment.mode,
            distance = data.distance,
            customerLat = data.address?.lat,
            customerLong = data.address?.lng,
            workerDetails = data.worker,
            isInspection = data.flags.isInspection,
            inspectionRate = data.service.inspectionRate,
            inspectionCalculatedAmount = data.bill.inspectionAmount
        )
    }



    fun acceptBooking(
        bookingId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val request = AcceptAndDeclineRequest(
                booking = bookingId,
                timezone = TimeZone.currentSystemDefault().id
            )

            when (val result = providerBookingUseCases.acceptBooking(request = request)) {
                is Success -> {
                    val message = result.data.meta.message.orEmpty()
                    _uiStates.update {
                        it.copy(message = message)
                    }
                    if (message.contains("successfully", ignoreCase = true)) {
                        _uiStates.update { it.copy(isSuccess = true) }
                        onSuccess()
                    }else{
                        _uiStates.update { it.copy(message = result.data.meta.message) }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun declineBooking(
        bookingId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val request = AcceptAndDeclineRequest(
                booking = bookingId,
                timezone = TimeZone.currentSystemDefault().id
            )

            when (val result = providerBookingUseCases.declineBooking(request = request)) {
                is Success -> {
                    val message = result.data.meta.message.orEmpty()
                    _uiStates.update {
                        it.copy(message = message)
                    }
                    if (message.contains("successfully", ignoreCase = true)) {
                        onSuccess()
                    }else{
                        _uiStates.update { it.copy(message = result.data.meta.message) }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }







}