package com.bytheour.presentations.screens.tabs.messages

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.GetChatMessageRequest
import com.bytheour.data.request.GetSupportHistoryRequest
import com.bytheour.data.request.MakeCharArchiveRequest
import com.bytheour.data.request.MessageReq
import com.bytheour.data.response.Message
import com.bytheour.utils.enums.MessageType
import com.bytheour.useCases.ChatUseCases
import com.bytheour.useCases.CommonChatSupportTicketUseCases
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.ChatSocket
import com.bytheour.utils.FileUpload
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.getCurrentDateTimeIn24hrs
import com.bytheour.utils.getCurrentDateTimeString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Suppress("ArrayInDataClass")
data class UiState(
    val id: String? = null,
    val username: String? = null,
    val status: String? = null,
    val roomId: String? = null,
    val message: String? = null,
    val type: String? = null,
    val isArchived: Boolean = false,
    val hasMoreData: Boolean = true,
    val currentPage: Int = 0,
    val isLoadingMore: Boolean = false,
    val totalCount: Int = 0,
    val isInitialChat: Boolean = false,
    val isChatSupport: Boolean = false,
    val profile: String? = null,
    val connected: Boolean = false,
    val chatSupportId: String? = null,
    val fromID: String? = null,
    val imageBitmap: ImageBitmap? = null,
    val imageErrorMessage: String? = null,
    val imagePathUrl:String? =  null,
    val byteArrayImage:ByteArray? = null,
    val filename:String? = null,
    val showDefaultQues: Boolean = false,
)

data class SupportHistoryMessage(
    val id: String,
    val text: String = "",
    val isUser: Boolean,
    val timestamp: String? = null
)


class OneOnOneChatScreenViewModel(
    private val chatUseCases: ChatUseCases,
    private val chatSupportScreenUseCase: CommonChatSupportTicketUseCases,
    private val chatSocket: ChatSocket,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase,
    commonUseCase: CommonUseCases
): ViewModel() {

    private val _supportMessages = MutableStateFlow<List<SupportHistoryMessage>>(emptyList())
//    val supportMessages: StateFlow<List<SupportHistoryMessage>> = _supportMessages.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _uiStates = MutableStateFlow(UiState())
    val uiStates = _uiStates.asStateFlow()


    init {
        chatSocket.connect()
    }

    val getAccountType = if (sharedPreferenceUseCase.getUser()== TYPE_CUSTOMER) "Provider" else "Customer"
    val getCurrentUserType = if (sharedPreferenceUseCase.getUser()== TYPE_CUSTOMER) "user" else "worker"



    fun sendMessage(text: String,msgType: String) {
        val userInfo = sharedPreferenceUseCase.getUserInfo()
        val sendData = Message(
            message = text,
            room =uiStates.value.roomId,
            sender = getCurrentUserType,
            from = userInfo?.id,
            type = msgType,
            user = sharedPreferenceUseCase.getUser() != TYPE_PROVIDER,
            time = getCurrentDateTimeIn24hrs(),
            profile = uiStates.value.profile,
            showTime = getCurrentDateTimeString(),
            support = uiStates.value.isChatSupport,
            senderType = if (uiStates.value.isChatSupport) sharedPreferenceUseCase.getUser() else null,
            default_msg = if (sharedPreferenceUseCase.getUser() == TYPE_PROVIDER && uiStates.value.isInitialChat) text else null
        )
        val messageReq = MessageReq(sendData, uiStates.value.roomId)
        println("messageReq ==================== ${Json.encodeToString(messageReq)}")
            chatSocket.sendChatMessage(payloadJson = Json.encodeToString(messageReq), toSupportRoom = uiStates.value.isChatSupport)
//        _messages.update { old ->
//            listOf(sendData) + old
//        }

     //   loadInitialMessages(uiStates.value.roomId ?: "", PAGINATION_LIMIT.toString())
    }

    private fun getChatMessages(
        roomId: String,
        isRefresh: Boolean = false
    ) {
        val currentState = _uiStates.value
        if (currentState.isLoadingMore && !isRefresh) return
        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage

            _uiStates.update {
                it.copy(isLoadingMore = true)
            }

            val request = GetChatMessageRequest(
                room = roomId,
                limit = "500",
                offset = "0"
            )

            when (val result = chatUseCases.getChatMessage(request)) {
                is JtApiResult.Success -> {
                    val messagesResponse = result.data?.data ?: emptyList()


                    val isFirstMessage = messagesResponse.firstOrNull{it.sender == getCurrentUserType}


                    _messages.value = messagesResponse

                    _uiStates.update {
                        it.copy(
                            roomId = roomId,
                            message = result.data?.meta?.message,
                            type = messagesResponse.firstOrNull()?.type,
                            currentPage = page + 1,
                            hasMoreData = false,
                            isLoadingMore = false,
                            totalCount = messagesResponse.size,
                            showDefaultQues = isFirstMessage == null && sharedPreferenceUseCase.getUser()== TYPE_PROVIDER
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun loadInitialMessages(roomId: String) {
        getChatMessages(
            roomId = roomId,
            isRefresh = true
        )
    }


    fun makeChatArchive(
        roomId: String,
        unarchive: Int
    ) {
        viewModelScope.launch {
            val request = MakeCharArchiveRequest(
                room_id = roomId,
                unarchive = unarchive
            )
            val result = chatUseCases.makeChatArchive(request)
            when(result) {
                is JtApiResult.Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data?.meta?.message,
                            isArchived = unarchive == 0
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }


    fun loadInitialSupportHistory(roomId: String, limit: String) {
        getSupportHistory(
            roomId = roomId,
            limit = limit,
            isRefresh = true
        )
    }



    fun getSupportHistory(
        roomId: String,
        limit: String = "10",
        isRefresh: Boolean = false
    ) {
        val currentState = _uiStates.value
        if (currentState.isLoadingMore && !isRefresh) return
        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage
            val calculatedOffset = (page * limit.toInt()).toString()

            _uiStates.update {
                it.copy(isLoadingMore = true)
            }

            val request = GetSupportHistoryRequest(
                supportId = roomId,
                limit = limit,
                offset = calculatedOffset
            )

            when (val result = chatSupportScreenUseCase.getSupportHistory(request)) {
                is JtApiResult.Success -> {
                    val messagesResponse = result.data?.data ?: emptyList()

                    val newMessageList = messagesResponse.map { chatItem ->
                        SupportHistoryMessage(
                            id = chatItem.room ?: "",
                            text = chatItem.message ?: "",
                            isUser = chatItem.sender == "user",
                            timestamp = chatItem.time
                        )
                    }

                    val currentMessages = if (isRefresh) emptyList() else _supportMessages.value
                    val updatedMessages = currentMessages + newMessageList
                    val hasMore = newMessageList.size >= limit.toInt()

                    _supportMessages.value = updatedMessages

                    _uiStates.update {
                        it.copy(
                            roomId = roomId,
                            message = result.data?.meta?.message,
                            currentPage = page + 1,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            totalCount = updatedMessages.size
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun start(roomId: String, fromChatSupportId: String, profile: String,fromID: String) {
        _uiStates.value = uiStates.value.copy(
            roomId = roomId,
            profile = profile,
            isChatSupport = fromChatSupportId.isNotBlank(),
            chatSupportId = fromChatSupportId,
            fromID = fromID
        )
        viewModelScope.launch {
            chatSocket.isConnected.collect { _uiStates.update { it.copy(connected = it.connected) } }
        }
        viewModelScope.launch {
            chatSocket.messages.collect { msg ->
//                val addHeader = _state.value.messages.none { it.header.contains("Today") }
//                val decorated = if (addHeader) msg.copy(header = "Today") else msg
                _messages.update { old ->
                    (listOf(msg) + old).distinct()
                }

            }
        }
        chatSocket.connect()
        chatSocket.join(
            roomId.ifEmpty { fromChatSupportId },
            fromChatSupportId.isNotBlank(),sharedPreferenceUseCase.getUser(),
            sharedPreferenceUseCase.getUserInfo()?.id ?: ""
        )
    }

    override fun onCleared() {
        super.onCleared()
        chatSocket.disconnect()
    }

    fun createChatRoom(fromChatSupportId: String, profile: String,fromID: String) {
        viewModelScope.launch {
            val result = if (sharedPreferenceUseCase.getUser() == TYPE_PROVIDER) {
                println("chatRoomId:===================== $fromID")
                chatUseCases.createChatRoom(
                    userId = fromID,
                    workerId = null
                )
            } else {
                println("chatRoomId:===================== $fromID")
                chatUseCases.createChatRoom(
                    userId = null,
                    workerId = fromID
                )
            }

            when(result) {
                is JtApiResult.Success -> {
                    _uiStates.update {
                        it.copy(
                            roomId = result.data?.data?.chatRoomId,
                            isInitialChat = true,
                            profile = profile,
                            isChatSupport = fromChatSupportId.isNotBlank(),
                            fromID = fromID
                        )
                    }
                    println("chatRoomId: ${result.data?.data?.chatRoomId}")
                    start(result.data?.data?.chatRoomId ?: "",fromChatSupportId,profile,fromID)
                    callChatHistory()
                }
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun callChatHistory(){
        if (uiStates.value.isChatSupport) {
            loadInitialSupportHistory(
                roomId = uiStates.value.chatSupportId ?: "",
                limit = PAGINATION_LIMIT.toString(),
            )
        } else {
            loadInitialMessages(
                roomId = uiStates.value.roomId ?: "",
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


    private val fileUploadUseCase = FileUpload(commonUseCase)

    fun sendChatImage(filePath:ByteArray) {
        viewModelScope.launch {
            fileUploadUseCase.uploadImage(
                useChatAttachment = true,
                file = filePath,
                filename = { uploadedFileName ->
                    _uiStates.update { currentState ->
                        currentState.copy(filename = uploadedFileName)
                    }
                },
                onMessage = { message ->
                    _uiStates.update { it.copy(message = message) }
                },
                onSuccess = {
                    sendMessage(uiStates.value.filename ?: "", MessageType.IMAGE.status)
                },
                showSuccessMessage = false
            )
        }
    }



}