package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.MeasurementUnitDao
import com.pantrymanager.data.mapper.MeasurementUnitMapper
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementUnitRepositoryImpl @Inject constructor(
    private val measurementUnitDao: MeasurementUnitDao,
    private val measurementUnitMapper: MeasurementUnitMapper
) : MeasurementUnitRepository {

    override suspend fun getAllMeasurementUnits(): List<MeasurementUnit> = withContext(Dispatchers.IO) {
        try {
            val entities = measurementUnitDao.getAllMeasurementUnits()
            entities.map { measurementUnitMapper.entityToDomain(it) }
        } catch (e: Exception) {
            // Se houver erro na base, retorna lista vazia
            emptyList()
        }
    }

    override suspend fun getMeasurementUnitById(id: Long): MeasurementUnit? = withContext(Dispatchers.IO) {
        try {
            val entity = measurementUnitDao.getMeasurementUnitById(id)
            entity?.let { measurementUnitMapper.entityToDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertMeasurementUnit(measurementUnit: MeasurementUnit): Long = withContext(Dispatchers.IO) {
        val entity = measurementUnitMapper.domainToEntity(measurementUnit)
        measurementUnitDao.insertMeasurementUnit(entity)
    }

    override suspend fun updateMeasurementUnit(measurementUnit: MeasurementUnit) = withContext(Dispatchers.IO) {
        val entity = measurementUnitMapper.domainToEntity(measurementUnit)
        measurementUnitDao.updateMeasurementUnit(entity)
    }

    override suspend fun deleteMeasurementUnit(unitId: Long) = withContext(Dispatchers.IO) {
        measurementUnitDao.deleteMeasurementUnitById(unitId)
    }

    override suspend fun findByName(name: String): MeasurementUnit? = withContext(Dispatchers.IO) {
        try {
            val entity = measurementUnitDao.findByName(name)
            entity?.let { measurementUnitMapper.entityToDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun findByAbbreviation(abbreviation: String): MeasurementUnit? = withContext(Dispatchers.IO) {
        try {
            val entity = measurementUnitDao.findByAbbreviation(abbreviation)
            entity?.let { measurementUnitMapper.entityToDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchMeasurementUnits(query: String): List<MeasurementUnit> = withContext(Dispatchers.IO) {
        try {
            val entities = measurementUnitDao.searchMeasurementUnits("%$query%")
            entities.map { measurementUnitMapper.entityToDomain(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteMeasurementUnits(ids: List<Long>) = withContext(Dispatchers.IO) {
        measurementUnitDao.deleteMeasurementUnits(ids)
    }

    override suspend fun findOrCreateMeasurementUnit(name: String, abbreviation: String): MeasurementUnit = withContext(Dispatchers.IO) {
        // Primeiro tenta encontrar por nome
        val existingByName = findByName(name)
        if (existingByName != null) {
            return@withContext existingByName
        }
        
        // Tenta encontrar por abreviação
        val existingByAbbr = findByAbbreviation(abbreviation)
        if (existingByAbbr != null) {
            return@withContext existingByAbbr
        }
        
        // Se não encontrar, cria uma nova
        val newUnit = MeasurementUnit(
            id = 0, // Será gerado automaticamente
            name = name.trim(),
            abbreviation = abbreviation.trim(),
            description = null,
            multiplyQuantityByPrice = false
        )
        
        val newId = insertMeasurementUnit(newUnit)
        newUnit.copy(id = newId)
    }
}