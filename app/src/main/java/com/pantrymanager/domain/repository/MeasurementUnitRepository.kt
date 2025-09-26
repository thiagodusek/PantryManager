package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.MeasurementUnit

interface MeasurementUnitRepository {
    suspend fun insertMeasurementUnit(unit: MeasurementUnit): Long
    suspend fun updateMeasurementUnit(unit: MeasurementUnit)
    suspend fun deleteMeasurementUnit(unit: MeasurementUnit)
    suspend fun deleteMeasurementUnit(unitId: Long)
    suspend fun deleteMeasurementUnitById(unitId: Long)
    suspend fun getMeasurementUnitById(unitId: Long): MeasurementUnit?
    suspend fun getAllMeasurementUnits(): List<MeasurementUnit>
    suspend fun findByName(name: String): MeasurementUnit?
    suspend fun findByAbbreviation(abbreviation: String): MeasurementUnit?
    suspend fun searchMeasurementUnits(query: String): List<MeasurementUnit>
    
    // Métodos para exclusão múltipla
    suspend fun deleteMeasurementUnits(ids: List<Long>)
    
    // Método para busca ou criação automática
    suspend fun findOrCreateMeasurementUnit(name: String, abbreviation: String): MeasurementUnit
}
