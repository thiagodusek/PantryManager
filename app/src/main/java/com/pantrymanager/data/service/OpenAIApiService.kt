package com.pantrymanager.data.service

import com.pantrymanager.data.dto.OpenAIRequest
import com.pantrymanager.data.dto.OpenAIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface Retrofit para comunicação com a OpenAI API
 */
interface OpenAIApiService {
    
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: OpenAIRequest
    ): Response<OpenAIResponse>
}
