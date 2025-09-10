package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.Brand

@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val logoUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: String, // LocalDateTime as String
    val updatedAt: String  // LocalDateTime as String
)

fun BrandEntity.toDomain(): Brand {
    return Brand(
        id = id,
        name = name,
        description = description,
        logoUrl = logoUrl,
        isActive = isActive
    )
}

fun Brand.toEntity(): BrandEntity {
    return BrandEntity(
        id = id,
        name = name,
        description = description,
        logoUrl = logoUrl,
        isActive = isActive,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
