package com.pantrymanager.data.dto

import com.pantrymanager.domain.entity.Brand

data class BrandFirebaseDto(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

fun BrandFirebaseDto.toDomain(): Brand {
    return Brand(
        id = id.hashCode().toLong(),
        name = name
    )
}

fun Brand.toFirebaseDto(userId: String): BrandFirebaseDto {
    return BrandFirebaseDto(
        id = id.toString(),
        name = name,
        userId = userId
    )
}
