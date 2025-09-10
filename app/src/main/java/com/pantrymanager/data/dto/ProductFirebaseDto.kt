package com.pantrymanager.data.dto

import com.pantrymanager.domain.entity.Product

data class ProductFirebaseDto(
    val id: String = "",
    val ean: String? = null,
    val name: String = "",
    val description: String? = null,
    val categoryId: Long = 0,
    val unitId: Long = 0,
    val observation: String? = null,
    val imageUrl: String? = null,
    val userId: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

fun ProductFirebaseDto.toDomain(): Product {
    return Product(
        id = id.hashCode().toLong(), // Convert Firebase string ID to Long
        ean = ean,
        name = name,
        description = description,
        categoryId = categoryId,
        unitId = unitId,
        observation = observation,
        imageUrl = imageUrl,
        userId = userId
    )
}

fun Product.toFirebaseDto(): ProductFirebaseDto {
    return ProductFirebaseDto(
        id = id.toString(),
        ean = ean,
        name = name,
        description = description,
        categoryId = categoryId,
        unitId = unitId,
        observation = observation,
        imageUrl = imageUrl,
        userId = userId
    )
}
