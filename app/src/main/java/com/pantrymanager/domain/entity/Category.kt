package com.pantrymanager.domain.entity

import java.time.LocalDateTime

/**
 * Entidade que representa uma categoria de produto
 */
data class Category(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val color: String = "#1976D2",
    val icon: String = "category",
    val parentCategoryId: Long? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
