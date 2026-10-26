package com.bytheour.presentations.screens.tabs.moreTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.FaqRequest
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.TYPE_CUSTOMER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FaqViewModel(
    private val generalUseCases: GeneralUseCases
) : ViewModel() {

    data class FaqUIState(
        val faqList: List<FaqItem> = emptyList(),
        val totalCount: Int? = 0,
        val message: String = "",
        val hasMoreData: Boolean = true,
        val currentPage: Int = 0,
        val isLoadingMore: Boolean = false
    )

    data class FaqItem(
        val question: String,
        val answer: String,
    )

    private val _uiState = MutableStateFlow(FaqUIState())
    val uiState: StateFlow<FaqUIState> = _uiState.asStateFlow()

    @Suppress("UNUSED_PARAMETER")
    private fun getFaq(
        limit: Int,
        offset: String,
        type: String,
        isRefresh: Boolean = false
    ) {
        val currentState = _uiState.value
        if (currentState.isLoadingMore) return

        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage
            val calculatedOffset = (page * limit).toString()

            _uiState.update {
                it.copy(isLoadingMore = true)
            }

            val request = FaqRequest(
                limit = limit,
                offset = calculatedOffset,
                type = type
            )

            when (val result = generalUseCases.getFaqUseCase(request)) {
                is Success -> {
                    val faqResponse = result.data?.data

                    val newFaqList = faqResponse?.mapNotNull { item ->
                        val question = item.question
                        val answer = item.answer
                        if (question.isNotBlank() && answer.isNotBlank()) {
                            FaqItem(question = question, answer = answer)
                        } else null
                    } ?: emptyList()

                    val totalCount = result.data?.data?.size
                    val currentItems = if (isRefresh) emptyList() else currentState.faqList
                    val updatedItems = currentItems + newFaqList
                    val hasMore = updatedItems.size < (totalCount ?: 0)

                    _uiState.update {
                        it.copy(
                            faqList = updatedItems,
                            totalCount = updatedItems.size,
                            currentPage = page + 1,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            message = ""
                        )
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun loadFaq(
        limit: Int = PAGINATION_LIMIT,
        type: String = TYPE_CUSTOMER
    ) {
        getFaq(
            limit = limit,
            offset = "0",
            type = type,
            isRefresh = true
        )
    }

    fun loadMoreData(
        limit: Int = PAGINATION_LIMIT,
        type: String = TYPE_CUSTOMER
    ) {
        getFaq(
            limit = limit,
            offset = "0", // This will be recalculated in the private method
            type = type,
            isRefresh = false
        )
    }
}