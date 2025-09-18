package com.pantrymanager.data.dto

import com.pantrymanager.domain.entity.MeasurementUnit

data class MeasurementUnitFirebaseDto(
    val id: String = "",
    val name: String = "",
    val abbreviation: String = "",
    val multiplyQuantityByPrice: Boolean = false,
    val userId: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

fun MeasurementUnitFirebaseDto.toDomain(): MeasurementUnit {
    return MeasurementUnit(
        id = id.hashCode().toLong(),
        name = name,
        abbreviation = abbreviation,
        multiplyQuantityByPrice = multiplyQuantityByPrice
    )
}

fun MeasurementUnit.toFirebaseDto(userId: String): MeasurementUnitFirebaseDto {
    return MeasurementUnitFirebaseDto(
        id = id.toString(),
        name = name,
        abbreviation = abbreviation,
        multiplyQuantityByPrice = multiplyQuantityByPrice,
        userId = userId
    )
}
