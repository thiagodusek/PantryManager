package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.Product
import java.time.LocalDate

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
            entity = UnitDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["unitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["unitId"]),
        Index(value = ["ean"])
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ean: String? = null,
    val name: String,
    val description: String? = null,
    val categoryId: Long,
    val unitId: Long,
    val observation: String? = null,
    val imageUrl: String? = null,
    val brand: String? = null,
    val averagePrice: Double = 0.0,
    val createdAt: String,
    val updatedAt: String
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        ean = ean,
        name = name,
        description = description,
        categoryId = categoryId,
        unitId = unitId,
        observation = observation,
        imageUrl = imageUrl,
        brand = brand,
        averagePrice = averagePrice,
        createdAt = LocalDate.parse(createdAt),
        updatedAt = LocalDate.parse(updatedAt)
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        ean = ean,
        name = name,
        description = description,
        categoryId = categoryId,
        unitId = unitId,
        observation = observation,
        imageUrl = imageUrl,
        brand = brand,
        averagePrice = averagePrice,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
