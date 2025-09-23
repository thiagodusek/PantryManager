package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: String? = null,
    val icon: String? = null,
    val description: String? = null,
    val parentCategoryId: Long? = null
)

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        color = color ?: "#1976D2",
        icon = icon ?: "category",
        parentCategoryId = parentCategoryId
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        color = color,
        icon = icon,
        description = description,
        parentCategoryId = parentCategoryId
    )
}
