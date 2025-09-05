package com.prantymanager.domain.entity

import java.time.LocalDate

data class Product(
    val id: Long = 0,
    val ean: String? = null, // Código de barras opcional
    val name: String, // Nome obrigatório
    val description: String? = null, // Descrição opcional
    val categoryId: Long, // ID da categoria (obrigatório)
    val unitId: Long, // ID da unidade de medida (obrigatório)
    val observation: String? = null, // Observação opcional
    val imageUrl: String? = null, // Foto opcional
    val brand: String? = null,
    val averagePrice: Double = 0.0,
    val nutritionalInfo: NutritionalInfo? = null,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)

data class NutritionalInfo(
    val calories: Double? = null,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val fiber: Double? = null,
    val sodium: Double? = null
)
