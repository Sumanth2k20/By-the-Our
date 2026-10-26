package com.bytheour.presentations.screens.tabs.reports.workers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.ChangeUserStatusRequest
import com.bytheour.useCases.AdminUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ContactWorkerCardUiState(
    val id: String,
    val isSelected: Boolean,
    val isStatusActive: Boolean,
    val title: String? = "",
    val number: String? = "",
    val emailId: String?,
    val documentStatus: String?,
    val registrationDate: String?,
    val accountType: String?,
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
    val serviceStatus:String?= ""
)

class WorkersScreenViewModel(
    private val adminUseCases: AdminUseCases
) : ViewModel() {

    data class AdminUserUiState(
        val deviceType: String? = "",
        val totalCount: Int? = 0,
        val contactList: List<ContactWorkerCardUiState>? = emptyList(),
        val message: String? = null,
        val selectAll: Boolean = false,
        val search: String? = null,
        val isLoading: Boolean = false,
        val hasMoreData: Boolean = true,
        val currentOffset: Int = 0,
        val currentFilter: String? = null
    )

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

            currentState.copy(
                contactList = updatedList,
            )
        }
    }

    fun updateSearch(search: String) {
        _uiState.update {
            it.copy(search = search)
        }
    }

    var isDropdownExpanded by mutableStateOf(false)
        private set

    fun toggleDropdown() {
        isDropdownExpanded = !isDropdownExpanded
    }

    fun onDropdownItemSelected(item: String?) {
        isDropdownExpanded = false
        _uiState.update {
            it.copy(currentFilter = item)
        }
        getFilteredList(item.toString())
    }

    fun dismissDropdown() {
        isDropdownExpanded = false
    }

    private fun getFilteredList(status: String) {
        viewModelScope.launch {
            fetchAdminWorkersList(
                limit = PAGINATION_LIMIT.toString(),
                offset = "0",
                documentStatus = status,
                searchKey = _uiState.value.search,
                isRefresh = true
            )
        }
    }

    fun loadMoreWorkers() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMoreData) {
            fetchAdminWorkersList(
                limit = PAGINATION_LIMIT.toString(),
                offset = currentState.currentOffset.toString(),
                documentStatus = currentState.currentFilter,
                searchKey = currentState.search,
                isRefresh = false
            )
        }
    }

    fun fetchAdminWorkersList(
        limit: String = PAGINATION_LIMIT.toString(),
        offset: String = "0",
        documentStatus: String? = null,
        searchKey: String? = null,
        isRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = adminUseCases.getAdminWorkersList(limit, offset, documentStatus, searchKey)
            when (result) {
                is Success -> {
                    val records = result.data.data?.records.orEmpty().filterNotNull()

                    val mappedContacts = records.map { record ->
                        ContactWorkerCardUiState(
                            id = record.id?.toString() ?: "",
                            number = record.contactNumber ?: "--",
                            isSelected = false,
                            isStatusActive =  result.data.data?.rejectionStatus?.toBoolean() ?: false,
                            title = record.workerName
                                ?: "${record.firstName.orEmpty()} ${record.lastName.orEmpty()}",
                            emailId = record.email ?: "--",
                            documentStatus = record.documentStatus ?: "--",
                            registrationDate = record.registeredDate ?: "--",
                            accountType = record.dType ?: "--",
                            firstName = record.firstName,
                            lastName = record.lastName,
                            profileImage = record.profile,
                            serviceStatus = record.isActive
                        )
                    }

                    _uiState.update { currentState ->
                        val newContactList = if (isRefresh) {
                            mappedContacts
                        } else {
                            (currentState.contactList.orEmpty() + mappedContacts).distinctBy { it.id }
                        }

                        val nextOffset = offset.toInt() + records.size

                        val hasMore =
                            records.size == limit.toInt() && nextOffset < (result.data.data?.total
                                ?: 0)

                        currentState.copy(
                            totalCount = result.data.data?.total,
                            contactList = newContactList,
                            message = null,
                            isLoading = false,
                            hasMoreData = hasMore,
                            currentOffset = nextOffset
                        )
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun adminDeleteSelectedWorkers(
        id: String
    ) {
        viewModelScope.launch {
            when (val result = adminUseCases.adminDeleteWorker(id)) {
                is Success -> {
                    _uiState.update { currentState ->
                        val updatedList = currentState.contactList?.filter { it.id != id }
                        currentState.copy(
                            contactList = updatedList,
                            totalCount = (currentState.totalCount ?: 0) - 1
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

    fun workerStatusChange(
        id: String,
        status: String
    ) {
        viewModelScope.launch {
            val request = ChangeUserStatusRequest(
                id = id,
                status = status
            )
            when (val result = adminUseCases.changeWorkerStatus(request)) {
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
}