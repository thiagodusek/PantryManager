package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.UnitDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.domain.repository.UnitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnitRepositoryImpl @Inject constructor(
    private val dao: UnitDao
) : UnitRepository {
    
    override fun getAllUnits(): Flow<List<UnitEntity>> {
        return dao.getAllUnits().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getUnitById(id: Long): Flow<UnitEntity?> {
        return dao.getUnitById(id).map { entity ->
            entity?.toDomain()
        }
    }
    
    override fun searchUnits(query: String): Flow<List<UnitEntity>> {
        return dao.searchUnits(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun insertUnit(unit: UnitEntity): Long {
        return dao.insertUnit(unit.toEntity())
    }
    
    override suspend fun updateUnit(unit: UnitEntity) {
        dao.updateUnit(unit.toEntity())
    }
    
    override suspend fun deleteUnit(unit: UnitEntity) {
        dao.deleteUnit(unit.toEntity())
    }
    
    override suspend fun deleteUnitById(id: Long) {
        dao.deleteUnitById(id)
    }
}
