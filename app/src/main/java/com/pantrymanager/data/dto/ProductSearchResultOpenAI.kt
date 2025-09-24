package com.pantrymanager.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Resultado da pesquisa de produto via OpenAI ChatGPT
 */
@JsonClass(generateAdapter = true)
data class ProductSearchResultOpenAI(
    @Json(name = "ean") val ean: String,
    @Json(name = "name") val name: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "brand") val brand: String = "",
    @Json(name = "category") val category: String = "",
    @Json(name = "unit") val unit: String = "",
    @Json(name = "unitAbbreviation") val unitAbbreviation: String = "",
    @Json(name = "averagePrice") val averagePrice: Double = 0.0,
    @Json(name = "weight") val weight: String = "",
    @Json(name = "nutritionalInfo") val nutritionalInfo: String? = null,
    @Json(name = "observations") val observations: String? = null,
    @Json(name = "found") val found: Boolean = false,
    @Json(name = "source") val source: String = "openai"
)
