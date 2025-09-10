package com.pantrymanager.domain.entity

import java.time.LocalDateTime

/**
 * Entidade que representa uma marca de produto
 */
data class Brand(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val logoUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
