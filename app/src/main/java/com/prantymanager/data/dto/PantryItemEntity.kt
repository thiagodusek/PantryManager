package com.prantymanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.prantymanager.domain.entity.PantryItem
import java.time.LocalDate

@Entity(
    tableName = "pantry_items",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["productId"]),
        Index(value = ["userId"])
    ]
)
data class PantryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val userId: String,
    val quantity: Double,
    val unit: String,
    val expiryDate: String?, // Stored as ISO string
    val purchaseDate: String, // Stored as ISO string
    val purchasePrice: Double? = null,
    val store: String? = null,
    val isExpired: Boolean = false,
    val isConsumed: Boolean = false,
    val createdAt: String, // Stored as ISO string
    val updatedAt: String // Stored as ISO string
)

fun PantryItemEntity.toDomain(): PantryItem {
    return PantryItem(
        id = id,
        productId = productId,
        userId = userId,
        quantity = quantity,
        unit = unit,
        expiryDate = expiryDate?.let { LocalDate.parse(it) },
        purchaseDate = LocalDate.parse(purchaseDate),
        purchasePrice = purchasePrice,
        store = store,
        isExpired = isExpired,
        isConsumed = isConsumed,
        createdAt = LocalDate.parse(createdAt),
        updatedAt = LocalDate.parse(updatedAt)
    )
}

fun PantryItem.toEntity(): PantryItemEntity {
    return PantryItemEntity(
        id = id,
        productId = productId,
        userId = userId,
        quantity = quantity,
        unit = unit,
        expiryDate = expiryDate?.toString(),
        purchaseDate = purchaseDate.toString(),
        purchasePrice = purchasePrice,
        store = store,
        isExpired = isExpired,
        isConsumed = isConsumed,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
