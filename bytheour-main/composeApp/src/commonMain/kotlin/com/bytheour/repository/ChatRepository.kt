package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.request.GetChatMessageRequest
import com.bytheour.data.request.MakeCharArchiveRequest
import com.bytheour.data.request.UsersListRequest
import com.bytheour.data.response.ChatListRoomResponse
import com.bytheour.data.response.ChatRoomResponse
import com.bytheour.data.response.GetChatMessageHistoryResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI

class ChatRepository (
    private val httpClient: HttpClient
) {


    suspend fun getChatListRoom():JtApiResult<ChatListRoomResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_CHAT_LIST())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun getChatMessageHistory(request: GetChatMessageRequest): JtApiResult<GetChatMessageHistoryResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_CHAT_MESSAGE())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.room?.let { append("room", it) }
                        request.offset?.let { append("offset", it) }
                        request.limit?.let { append("limit", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }



    @OptIn(InternalAPI::class)
    suspend fun getArchivedChat(request: UsersListRequest):JtApiResult<ChatListRoomResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_ARCHIVE_CHAT())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.offset?.let { append("offset", it) }
                        request.limit?.let { append("limit", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }



    suspend fun makeChatArchive(request:MakeCharArchiveRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CHAT_ARCHIVE())
                setBody(FormDataContent(Parameters.build {
                    request.room_id?.let { append("room_id", it) }
                    request.unarchive?.let { append("unarchive", it.toString()) }
                }))
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun createChatRoom(userId: String?, workerId: String?): JtApiResult<ChatRoomResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CREATE_CHAT_ROOM())
                formData {
                    userId?.let { parameter("user_id", userId) }
                    workerId?.let { parameter("worker_id", workerId) }
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


}

