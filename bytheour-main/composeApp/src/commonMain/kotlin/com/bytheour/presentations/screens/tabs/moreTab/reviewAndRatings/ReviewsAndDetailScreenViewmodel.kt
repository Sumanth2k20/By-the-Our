package com.bytheour.presentations.screens.tabs.moreTab.reviewAndRatings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewsAndDetailScreenViewmodel(
    private val commonUseCases: CommonUseCases,
) : ViewModel() {

    enum class ReviewTab { GIVEN, TAKEN }

    data class ReviewItem(
        val id: String,
        val serviceRating: Int,
        val honestRating: Int,
        val review: String?,
        val type: String,
        val date: String,
        val serviceTitle: String,
        val bookingDate: String,
        val bookingTime: String,
        val bookingHours: Int,
        val userName: String,
        val userProfile: String,
        val workerName: String,
        val workerProfile: String,
        val isGiven: Boolean,
        val showEditIcon:Boolean,
        val bookingId:String,
        val giveServiceRatingText :String? = null,
        val giveServiceRating :String ? = null
    )

    data class UiState(
        val isLoading: Boolean = false,
        val reviewsGiven: List<ReviewItem> = emptyList(),
        val reviewsTaken: List<ReviewItem> = emptyList(),
        val error: String? = null,
        val selectedTab: ReviewTab = ReviewTab.TAKEN,
        val hasFetchedGiven: Boolean = true,
        val hasFetchedTaken: Boolean = true,
        val givenOffset: Int = 0,
        val takenOffset: Int = 0,
        val currentFilter: String? = null
    )


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadMoreServices() {
        val currentState = _uiState.value
        when (currentState.selectedTab) {
            ReviewTab.GIVEN -> {
                if (!currentState.hasFetchedGiven) return
                val newOffset = currentState.givenOffset + PAGINATION_LIMIT
                fetchReviews(
                    ratingType = "given",
                    offset = newOffset.toString(),
                    isInitialLoad = false,
                    honestRatings = currentState.currentFilter,
                )
            }
            ReviewTab.TAKEN -> {
                if (!currentState.hasFetchedTaken) return
                val newOffset = currentState.takenOffset + PAGINATION_LIMIT
                fetchReviews(
                    ratingType = "taken",
                    offset = newOffset.toString(),
                    isInitialLoad = false,
                    honestRatings = currentState.currentFilter,
                )
            }
        }
    }
    fun fetchReviews(
        ratingType: String? = null,
        honestRatings: String? = null,
        offset: String,
        isInitialLoad: Boolean
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = commonUseCases.getRating(limit = PAGINATION_LIMIT.toString(),
                offset = offset, ratingType, ratings = honestRatings, null)
            ) {
                is JtApiResult.Success -> {
                    val allReviews = result.data.data.map { rating ->
                        ReviewItem(
                            id = if(rating.given != null) rating.given.id else rating.taken?.id?:"",
                            serviceRating = if(rating.given != null) rating.given.serviceRating ?:0 else rating.taken?.serviceRating?:0,
                            honestRating = if(rating.taken != null) rating.taken.rating else rating.given?.rating?:0,
                            review =  if(rating.given != null) rating.given.review else rating.taken?.review?:"",
                            type = if(rating.given != null) rating.given.type else rating.taken?.type?:"",
                            date = if(rating.given != null) rating.given.date else rating.taken?.date?:"",
                            showEditIcon = if(rating.given != null) rating.given.allowToModity else false,
                            serviceTitle = rating.service.title,
                            bookingDate = rating.booking.date,
                            bookingTime = rating.booking.time,
                            bookingHours = rating.booking.hours,
                            userName = rating.user.firstName + " " + rating.user.lastName,
                            userProfile = rating.user.profile?:"",
                            workerName = rating.worker.firstName + " " + rating.worker.lastName,
                            workerProfile = rating.worker.profile,
                            isGiven = rating.given != null,
                            bookingId = rating.booking.id,
                            giveServiceRatingText = rating.taken?.serviceRatingText,
                        )
                    }
                    val hasMore = allReviews.size >= PAGINATION_LIMIT

                    _uiState.update { currentState ->
                        val newState = when (ratingType) {
                            "given" -> {
                                val newPageItems = allReviews.filter { it.isGiven }
                                val updatedGiven = if (isInitialLoad) {
                                    newPageItems
                                } else {
                                    currentState.reviewsGiven + newPageItems
                                }
                                currentState.copy(
                                    reviewsGiven = updatedGiven,
                                    givenOffset = if (newPageItems.isNotEmpty()) offset.toInt()
                                    else currentState.givenOffset,
                                    hasFetchedGiven = hasMore
                                )
                            }

                            "taken" -> {
                                val newPageItems = allReviews.filter { !it.isGiven }
                                val updatedTaken = if (isInitialLoad) {
                                    newPageItems
                                } else {
                                    currentState.reviewsTaken + newPageItems
                                }
                                currentState.copy(
                                    reviewsTaken = updatedTaken,
                                    takenOffset = if (newPageItems.isNotEmpty()) offset.toInt()
                                    else currentState.takenOffset,
                                    hasFetchedTaken = hasMore

                                )
                            }

                            else -> currentState
                        }
                        newState.copy(currentFilter = honestRatings)
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update { it.copy(error = result.error.message) }
                }
            }
        }
    }


    fun selectTab(tab: ReviewTab) {
        _uiState.update { it.copy(selectedTab = tab) }

        val ratingType = when (tab) {
            ReviewTab.TAKEN -> "taken"
            ReviewTab.GIVEN -> "given"
        }

        fetchReviews(ratingType = ratingType,
            offset = "0",
            isInitialLoad = true
        )
    }




    fun clearFilters() {
        val type = if (_uiState.value.selectedTab == ReviewTab.TAKEN) "taken" else "given"
        fetchReviews(ratingType = type,
            offset = "0",
            isInitialLoad = true,
            honestRatings = null
        )
    }


}