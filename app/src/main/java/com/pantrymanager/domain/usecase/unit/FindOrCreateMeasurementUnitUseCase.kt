package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.data.defaults.DefaultMeasurementUnits
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class FindOrCreateMeasurementUnitUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val addMeasurementUnitUseCase: AddMeasurementUnitUseCase
) {
    suspend operator fun invoke(unitName: String, abbreviation: String? = null): Result<MeasurementUnit> {
        return try {
            if (unitName.isBlank()) {
                return Result.failure(Exception("Nome da unidade não pode estar vazio"))
            }
            
            // Primeiro tenta buscar uma unidade existente pelo nome
            val existingUnit = measurementUnitRepository.findByName(unitName)
            if (existingUnit != null) {
                return Result.success(existingUnit)
            }
            
            // Tenta buscar pela abreviação se fornecida
            if (!abbreviation.isNullOrBlank()) {
                val unitByAbbreviation = measurementUnitRepository.findByAbbreviation(abbreviation)
                if (unitByAbbreviation != null) {
                    return Result.success(unitByAbbreviation)
                }
            }
            
            // Busca nos dados padrão primeiro
            val defaultUnit = DefaultMeasurementUnits.getByAbbreviation(abbreviation ?: unitName)
                ?: DefaultMeasurementUnits.defaultUnits.find { 
                    it.name.equals(unitName, ignoreCase = true) ||
                    unitName.contains(it.name, ignoreCase = true) ||
                    it.name.contains(unitName, ignoreCase = true)
                }
            
            if (defaultUnit != null) {
                // Se existe nos dados padrão, adiciona ao banco
                val result = addMeasurementUnitUseCase(defaultUnit)
                if (result.isSuccess) {
                    val unitId = result.getOrThrow()
                    val createdUnit = measurementUnitRepository.getMeasurementUnitById(unitId)
                    if (createdUnit != null) {
                        return Result.success(createdUnit)
                    }
                }
            }
            
            // Se não existe nos padrões, cria uma nova unidade personalizada
            val newUnit = MeasurementUnit(
                name = unitName.trim(),
                abbreviation = abbreviation?.trim() ?: unitName.take(3).uppercase(),
                description = "Unidade cadastrada automaticamente via pesquisa de produto",
                multiplyQuantityByPrice = false, // Por padrão, não multiplica
                isActive = true
            )
            
            val result = addMeasurementUnitUseCase(newUnit)
            if (result.isSuccess) {
                val unitId = result.getOrThrow()
                val createdUnit = measurementUnitRepository.getMeasurementUnitById(unitId)
                if (createdUnit != null) {
                    Result.success(createdUnit)
                } else {
                    Result.failure(Exception("Erro ao recuperar unidade criada"))
                }
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Erro ao criar unidade"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
