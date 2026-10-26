package com.bytheour.presentations.screens.tabs.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.AdminDeleteUserRequest
import com.bytheour.data.request.AdminGetUserRequest
import com.bytheour.data.request.ChangeUserStatusRequest
import com.bytheour.data.request.UsersListRequest
import com.bytheour.useCases.AdminUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.PAGINATION_LIMIT_15
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUserUiState(
    val deviceType: String?= "--",
    val totalCount :Int?= 57,
    val contactList: List<ContactCardUiState>? = emptyList(),
    val message:String?= null,
    val selectAll: Boolean = false,
    val search: String? = "",
    val completed :Int? = 0,
    val upComing :Int? = 0,
    val onGoing :Int? = 0,
    val hasMoreData: Boolean = true,
    val currentPage: Int = 0,
    val isLoadingMore: Boolean = false
)

data class ContactCardUiState(
    val id: String,
    val firstName: String? = "Details Not Available",
    val lastName: String? = "Details Not Available",
    val phoneNumber: String,
    val isSelected: Boolean,
    val isStatusActive: Boolean,
    val deviceType: String? = "",
    val email :String? = "",
    val image :String? = "",
    val serviceStatus:String?= ""
)

class AdminUsersViewModel(
    private val adminUseCases: AdminUseCases
): ViewModel() {

    private val _uiState = MutableStateFlow(AdminUserUiState())
    val uiStates = _uiState.asStateFlow()

    fun onSelectAllUsers(isSelected: Boolean) {
        _uiState.update { currentState ->
            val updatedList = currentState.contactList?.map {
                it.copy(isSelected = isSelected)
            }
            currentState.copy(
                contactList = updatedList,
                selectAll = isSelected
            )
        }
    }

    fun onSelectCheckBox(userId: String, isSelected: Boolean) {
        _uiState.update { currentState ->
            val updatedList = currentState.contactList?.map {
                if (it.id == userId) it.copy(isSelected = isSelected) else it
            }

            val allSelected = updatedList?.all { it.isSelected } ?: false

            currentState.copy(
                contactList = updatedList,
                selectAll = allSelected
            )
        }
    }

    private var searchJob: Job? = null

    fun updateSearch(search: String) {
        _uiState.update {
            it.copy(search = search)
        }
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            performSearch(search)
        }
    }

    private fun performSearch(searchQuery: String) {
        _uiState.update {
            it.copy(
                contactList = emptyList(),
                currentPage = 0,
                hasMoreData = true
            )
        }
        getUserList(isRefresh = false, searchQuery = searchQuery)
    }

    private fun getUserList(
        isRefresh: Boolean = false,
        searchQuery: String? = null
    ) {
        val currentState = _uiState.value

        if (currentState.isLoadingMore) return

        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage
            val offset = (page * PAGINATION_LIMIT_15).toString()

            val keyword = when {
                searchQuery != null -> {
                    val trimmed = searchQuery.trim()
                    if (trimmed.length >= 3) trimmed else null
                }
                currentState.search?.isNotEmpty() == true -> {
                    val trimmed = currentState.search.trim()
                    if (trimmed.length >= 3) trimmed else null
                }
                else -> null
            }

            _uiState.update { it.copy(isLoadingMore = true) }

            val request = UsersListRequest(
                limit = PAGINATION_LIMIT.toString(),
                offset = offset,
                searchKey = keyword
            )

            when (val result = adminUseCases.getUsersList(request)) {
                is Success -> {
                    val records = result.data.data?.records.orEmpty()
                    val newItems = records.map { record ->
                        ContactCardUiState(
                            id = record?.id?.toString() ?: "",
                            firstName =  record?.firstName,
                            lastName = record?.lastName,
                            deviceType = record?.dType,
                            email = record?.email,
                            image = record?.profile,
                            phoneNumber = record?.contactNumber.orEmpty(),
                            isSelected = record?.isSelected == true,
                            isStatusActive = result.data.data?.rejectionStatus?.toBoolean() ?: false,
                            serviceStatus = record?.isActive
                        )
                    }

                    val totalCount = result.data.data?.total ?: 0
                    val currentItems = if (isRefresh) emptyList() else currentState.contactList.orEmpty()
                    val updatedItems = currentItems + newItems
                    val hasMore = updatedItems.size < totalCount

                    _uiState.update { state ->
                        state.copy(
                            contactList = updatedItems,
                            totalCount = totalCount,
                            currentPage = page + 1,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            message = null
                        )
                    }
                }

                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun loadMoreData() {
        getUserList(isRefresh = false)
    }

    fun refreshData() {
        _uiState.update { it.copy(search = "") }
        getUserList(isRefresh = true)
    }

    fun userStatusChange(
        id: String,
        status: String
    ) {
        viewModelScope.launch {
            val request = ChangeUserStatusRequest(
                id = id,
                status = status
            )
            when (val result = adminUseCases.changeUserStatus(request)) {
                is Success -> {
                    _uiState.update { currentState ->
                        val updatedList = currentState.contactList?.map {
                            if (it.id == id) it.copy(isStatusActive = status == "y")
                            else it
                        }
                        currentState.copy(contactList = updatedList)
                    }
                }

                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun adminDeleteUser(
        id: String
    ){
        viewModelScope.launch {
            val request = AdminDeleteUserRequest(
                id = id
            )
            when(val result = adminUseCases.adminDeleteUser(request)) {
                is Success -> {
                    _uiState.update { currentState ->
                        val updatedList = currentState.contactList?.filter { it.id != id }
                        currentState.copy(contactList = updatedList,
                            totalCount = (currentState.totalCount ?: 0) - 1)
                    }
                }

                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun adminGetUserDate(
        id: String
    ){
        viewModelScope.launch {
            val request = AdminGetUserRequest(
                id = id
            )
            when(val result = adminUseCases.adminGetUserData(request)) {
                is Success -> {
                    _uiState.update {
                        it.copy(
                            completed = result.data.data?.bookings?.completed,
                            onGoing = result.data.data?.bookings?.ongoing,
                            upComing = result.data.data?.bookings?.upcoming
                        )
                    }
                }
                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }
}

