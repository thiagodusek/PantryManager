package com.pantrymanager.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTOs para comunicação com a OpenAI API
 */
@JsonClass(generateAdapter = true)
data class OpenAIRequest(
    @Json(name = "model")
    val model: String = "gpt-3.5-turbo",
    @Json(name = "messages")
    val messages: List<OpenAIMessage>,
    @Json(name = "max_tokens")
    val maxTokens: Int = 500,
    @Json(name = "temperature")
    val temperature: Double = 0.7
)

@JsonClass(generateAdapter = true)
data class OpenAIMessage(
    @Json(name = "role")
    val role: String, // "system", "user", "assistant"
    @Json(name = "content")
    val content: String
)

@JsonClass(generateAdapter = true)
data class OpenAIResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "object")
    val objectType: String,
    @Json(name = "created")
    val created: Long,
    @Json(name = "model")
    val model: String,
    @Json(name = "choices")
    val choices: List<OpenAIChoice>,
    @Json(name = "usage")
    val usage: OpenAIUsage? = null
)

@JsonClass(generateAdapter = true)
data class OpenAIChoice(
    @Json(name = "index")
    val index: Int,
    @Json(name = "message")
    val message: OpenAIMessage,
    @Json(name = "finish_reason")
    val finishReason: String
)

@JsonClass(generateAdapter = true)
data class OpenAIUsage(
    @Json(name = "prompt_tokens")
    val promptTokens: Int,
    @Json(name = "completion_tokens")
    val completionTokens: Int,
    @Json(name = "total_tokens")
    val totalTokens: Int
)

/**
 * Resultado estruturado da pesquisa de produto via OpenAI
 */
@JsonClass(generateAdapter = true)
data class ProductSearchResultOpenAI(
    val ean: String,
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val brand: String? = null,
    val category: String? = null,
    val unit: String? = null,
    val unitAbbreviation: String? = null,
    val averagePrice: Double? = null,
    val found: Boolean = false,
    val source: String = "openai"
)
