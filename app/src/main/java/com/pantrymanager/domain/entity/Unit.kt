package com.pantrymanager.domain.entity

import java.time.LocalDateTime

/**
 * Entidade que representa uma unidade de medida
 */
data class MeasurementUnit(
    val id: Long = 0L,
    val name: String,
    val abbreviation: String,
    val description: String? = null,
    val multiplyQuantityByPrice: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
