package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.Product
import java.time.LocalDateTime

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MeasurementUnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["unitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["unitId"]),
        Index(value = ["ean"]),
        Index(value = ["userId"])
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String, // ID do usuário dono do produto
    val ean: String? = null,
    val name: String,
    val description: String? = null,
    val categoryId: Long,
    val unitId: Long,
    val brandId: Long? = null,
    val observation: String? = null,
    val imageUrl: String? = null,
    val averagePrice: Double = 0.0,
    val createdAt: String, // LocalDateTime as String
    val updatedAt: String  // LocalDateTime as String
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        userId = userId,
        ean = ean,
        name = name,
        description = description,
        categoryId = categoryId,
        unitId = unitId,
        brandId = brandId,
        observation = observation,
        imageUrl = imageUrl,
        averagePrice = averagePrice,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        userId = userId,
        ean = ean,
        name = name,
        description = description,
        categoryId = categoryId,
        unitId = unitId,
        brandId = brandId,
        observation = observation,
        imageUrl = imageUrl,
        averagePrice = averagePrice,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
