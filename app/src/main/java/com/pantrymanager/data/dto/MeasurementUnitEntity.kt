package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.MeasurementUnit

@Entity(tableName = "measurement_units")
data class MeasurementUnitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String, // Nome da unidade (ex: "Quilograma")
    val abbreviation: String, // Abreviação (ex: "kg")
    val description: String? = null, // Descrição opcional
    val multiplyQuantityByPrice: Boolean = false, // Se deve multiplicar quantidade por preço
    val isActive: Boolean = true,
    val createdAt: String, // LocalDateTime as String
    val updatedAt: String  // LocalDateTime as String
)

fun MeasurementUnitEntity.toDomain(): MeasurementUnit {
    return MeasurementUnit(
        id = id,
        name = name,
        abbreviation = abbreviation,
        description = description,
        multiplyQuantityByPrice = multiplyQuantityByPrice,
        isActive = isActive
    )
}

fun MeasurementUnit.toEntity(): MeasurementUnitEntity {
    return MeasurementUnitEntity(
        id = id,
        name = name,
        abbreviation = abbreviation,
        description = description,
        multiplyQuantityByPrice = multiplyQuantityByPrice,
        isActive = isActive,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
