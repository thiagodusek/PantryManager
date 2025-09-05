package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import kotlinx.coroutines.flow.Flow

interface UnitRepository {
    fun getAllUnits(): Flow<List<UnitEntity>>
    fun getUnitById(id: Long): Flow<UnitEntity?>
    fun searchUnits(query: String): Flow<List<UnitEntity>>
    suspend fun insertUnit(unit: UnitEntity): Long
    suspend fun updateUnit(unit: UnitEntity)
    suspend fun deleteUnit(unit: UnitEntity)
    suspend fun deleteUnitById(id: Long)
}
