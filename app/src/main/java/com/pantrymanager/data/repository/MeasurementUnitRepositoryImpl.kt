package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.MeasurementUnitDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementUnitRepositoryImpl @Inject constructor(
    private val dao: MeasurementUnitDao
) : MeasurementUnitRepository {
    
    override suspend fun insertMeasurementUnit(unit: MeasurementUnit): Long {
        return dao.insertUnit(unit.toEntity())
    }
    
    override suspend fun updateMeasurementUnit(unit: MeasurementUnit) {
        dao.updateUnit(unit.toEntity())
    }
    
    override suspend fun deleteMeasurementUnit(unitId: Long) {
        dao.deleteUnitById(unitId)
    }
    
    override suspend fun getMeasurementUnitById(unitId: Long): MeasurementUnit? {
        return dao.getUnitById(unitId)?.toDomain()
    }
    
    override suspend fun getAllMeasurementUnits(): List<MeasurementUnit> {
        return dao.getAllUnits().map { it.toDomain() }
    }
    
    override suspend fun findByName(name: String): MeasurementUnit? {
        return dao.findByName(name)?.toDomain()
    }
    
    override suspend fun findByAbbreviation(abbreviation: String): MeasurementUnit? {
        return dao.findByAbbreviation(abbreviation)?.toDomain()
    }
    
    override suspend fun searchMeasurementUnits(query: String): List<MeasurementUnit> {
        return dao.searchUnits("%$query%").map { it.toDomain() }
    }
}
