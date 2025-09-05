package com.pantrymanager.domain.entity

import java.time.LocalDate

data class ShoppingList(
    val id: Long = 0,
    val userId: String,
    val name: String,
    val items: List<ShoppingListItem> = emptyList(),
    val isCompleted: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)

data class ShoppingListItem(
    val id: Long = 0,
    val productId: Long,
    val quantity: Double,
    val unit: String,
    val isPurchased: Boolean = false,
    val estimatedPrice: Double? = null,
    val actualPrice: Double? = null,
    val store: String? = null
)
