package com.pantrymanager.domain.entity

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Entidade que representa um lote/estoque específico de um produto
 */
data class ProductBatch(
    val id: Long = 0L,
    val productId: Long,
    val userId: String,
    val batchNumber: String,
    val quantity: Double,
    val expiryDate: LocalDate,
    val purchaseDate: LocalDate? = null,
    val purchasePrice: Double? = null,
    val purchaseLocation: String? = null,
    val isConsumed: Boolean = false,
    val consumedAt: LocalDateTime? = null,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
