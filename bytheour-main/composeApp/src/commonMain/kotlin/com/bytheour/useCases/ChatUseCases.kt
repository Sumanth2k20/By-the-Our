package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.GetChatMessageRequest
import com.bytheour.data.request.MakeCharArchiveRequest
import com.bytheour.data.request.UsersListRequest
import com.bytheour.data.response.ChatListRoomResponse
import com.bytheour.data.response.ChatRoomResponse
import com.bytheour.data.response.GetChatMessageHistoryResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.repository.ChatRepository
import com.bytheour.showProgress

class ChatUseCases(
    private val chatRepository: ChatRepository
) {

    suspend fun getChatList(): JtApiResult<ChatListRoomResponse?> {
        showProgress.value = true
        val result = chatRepository.getChatListRoom()
        showProgress.value = false
        return result
    }

    suspend fun getChatMessage(request: GetChatMessageRequest): JtApiResult<GetChatMessageHistoryResponse?> {
        showProgress.value = true
        val result = chatRepository.getChatMessageHistory(request)
        showProgress.value = false
        return result
    }

    suspend fun getArchivedChat(request: UsersListRequest): JtApiResult<ChatListRoomResponse?> {
        showProgress.value = true
        val result = chatRepository.getArchivedChat(request)
        showProgress.value = false
        return result
    }

    suspend fun makeChatArchive(request: MakeCharArchiveRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = chatRepository.makeChatArchive(request)
        showProgress.value = false
        return result
    }

    suspend fun createChatRoom(userId: String?, workerId: String?): JtApiResult<ChatRoomResponse?> {
        showProgress.value = true
        val result = chatRepository.createChatRoom(userId,workerId)
        showProgress.value = false
        return result
    }
}
