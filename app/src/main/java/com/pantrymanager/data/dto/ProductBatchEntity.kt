package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.ProductBatch
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "product_batches",
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
        Index(value = ["userId"]),
        Index(value = ["batchNumber", "userId"]),
        Index(value = ["expiryDate"]),
        Index(value = ["isConsumed"])
    ]
)
data class ProductBatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long, // FK para produto
    val userId: String, // ID do usuário dono do lote
    val batchNumber: String, // Número do lote (obrigatório)
    val quantity: Double, // Quantidade no lote
    val expiryDate: String, // Data de validade (LocalDate as String)
    val purchaseDate: String? = null, // Data de compra opcional (LocalDate as String)
    val purchasePrice: Double? = null, // Preço de compra opcional
    val purchaseLocation: String? = null, // Local de compra opcional
    val isConsumed: Boolean = false, // Se o lote foi totalmente consumido
    val consumedAt: String? = null, // Data que foi consumido (LocalDateTime as String)
    val notes: String? = null, // Observações adicionais
    val createdAt: String, // LocalDateTime as String
    val updatedAt: String  // LocalDateTime as String
)

fun ProductBatchEntity.toDomain(): ProductBatch {
    return ProductBatch(
        id = id,
        productId = productId,
        userId = userId,
        batchNumber = batchNumber,
        quantity = quantity,
        expiryDate = LocalDate.parse(expiryDate),
        purchaseDate = purchaseDate?.let { LocalDate.parse(it) },
        purchasePrice = purchasePrice,
        purchaseLocation = purchaseLocation,
        isConsumed = isConsumed,
        consumedAt = consumedAt?.let { LocalDateTime.parse(it) },
        notes = notes
    )
}

fun ProductBatch.toEntity(): ProductBatchEntity {
    return ProductBatchEntity(
        id = id,
        productId = productId,
        userId = userId,
        batchNumber = batchNumber,
        quantity = quantity,
        expiryDate = expiryDate.toString(),
        purchaseDate = purchaseDate?.toString(),
        purchasePrice = purchasePrice,
        purchaseLocation = purchaseLocation,
        isConsumed = isConsumed,
        consumedAt = consumedAt?.toString(),
        notes = notes,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
