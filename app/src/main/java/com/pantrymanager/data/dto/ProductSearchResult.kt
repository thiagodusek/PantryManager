package com.pantrymanager.data.dto

/**
 * Resultado da pesquisa de produto por código de barras
 */
data class ProductSearchResult(
    val ean: String,
    val name: String?,
    val description: String?,
    val brand: String?,
    val imageUrl: String?,
    val averagePrice: Double = 0.0,
    val category: String? = null,
    val unit: String? = null, // Nome da unidade de medida sugerida
    val unitAbbreviation: String? = null, // Abreviação da unidade
    val nutritionalInfo: NutritionalInfoDto? = null,
    val found: Boolean = true,
    val source: String = "unknown" // fonte da informação (api, local, etc)
)

data class NutritionalInfoDto(
    val calories: Double? = null,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val fiber: Double? = null,
    val sodium: Double? = null
)
