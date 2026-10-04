package id.codemockup.ramu.core.network.services

import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.chat.ChatHistoryResponse
import id.codemockup.ramu.core.data.remote.response.chat.ChatResponse
import id.codemockup.ramu.core.data.remote.response.chat.ChatSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HermesServices {
    @POST("api/sessions")
    suspend fun createSession(@Body request: CreateChatSessionRequest): Response<ChatSessionResponse>

    @GET("api/sessions/{id}/messages")
    suspend fun history(
        @Path("id") sessionId: String,
        @Query("inline_images") inlineImages: Boolean = false,
    ): Response<ChatHistoryResponse>

    @POST("api/sessions/{id}/chat")
    suspend fun chat(@Path("id") sessionId: String, @Body request: ChatRequest): Response<ChatResponse>
}
