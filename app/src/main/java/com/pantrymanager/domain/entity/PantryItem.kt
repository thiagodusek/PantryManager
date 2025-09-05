package com.pantrymanager.domain.entity

import java.time.LocalDate

data class PantryItem(
    val id: Long = 0,
    val productId: Long,
    val userId: String,
    val quantity: Double,
    val unit: String,
    val expiryDate: LocalDate?,
    val purchaseDate: LocalDate = LocalDate.now(),
    val purchasePrice: Double? = null,
    val store: String? = null,
    val isExpired: Boolean = false,
    val isConsumed: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)
