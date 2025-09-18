package com.pantrymanager.data.dto

import com.pantrymanager.domain.entity.Category

data class CategoryFirebaseDto(
    val id: String = "",
    val name: String = "",
    val color: String = "#2196F3",
    val icon: String = "category",
    val userId: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

fun CategoryFirebaseDto.toDomain(): Category {
    return Category(
        id = id.hashCode().toLong(),
        name = name,
        color = color,
        icon = icon
    )
}

fun Category.toFirebaseDto(userId: String): CategoryFirebaseDto {
    return CategoryFirebaseDto(
        id = id.toString(),
        name = name,
        color = color,
        icon = icon,
        userId = userId
    )
}
