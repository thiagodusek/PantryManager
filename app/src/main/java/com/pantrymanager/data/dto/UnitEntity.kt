package com.pantrymanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pantrymanager.domain.entity.MeasurementUnit

@Entity(tableName = "units")
data class UnitDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val abbreviation: String,
    val multiplyQuantityByPrice: Boolean = false
)

fun UnitDbEntity.toDomain(): MeasurementUnit {
    return MeasurementUnit(
        id = id,
        name = name,
        abbreviation = abbreviation,
        multiplyQuantityByPrice = multiplyQuantityByPrice
    )
}

fun MeasurementUnit.toEntity(): UnitDbEntity {
    return UnitDbEntity(
        id = id,
        name = name,
        abbreviation = abbreviation,
        multiplyQuantityByPrice = multiplyQuantityByPrice
    )
}
