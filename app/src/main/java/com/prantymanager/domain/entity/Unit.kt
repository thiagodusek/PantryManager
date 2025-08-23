package com.prantymanager.domain.entity

data class MeasurementUnit(
    val id: Long = 0,
    val name: String, // Nome obrigatório (ex: kg, litros, unidades, etc.)
    val abbreviation: String, // Abreviação (ex: kg, l, un)
    val multiplyQuantityByPrice: Boolean = false // Se deve multiplicar quantidade pelo preço
)
