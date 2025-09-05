package com.prantymanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prantymanager.domain.entity.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: String = "#1976D2",
    val icon: String = "category"
)

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        color = color,
        icon = icon
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        color = color,
        icon = icon
    )
}
