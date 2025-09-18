package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.data.service.OpenAIMeasurementUnitSearchService
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class PopulateMeasurementUnitsFromOpenAIUseCase @Inject constructor(
    private val openAIMeasurementUnitSearchService: OpenAIMeasurementUnitSearchService,
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            // Buscar todas as unidades de medida no ChatGPT
            val unitsFromOpenAI = openAIMeasurementUnitSearchService.getAllMeasurementUnits()
            
            if (unitsFromOpenAI.isEmpty()) {
                return Result.failure(Exception("Nenhuma unidade de medida encontrada no ChatGPT"))
            }
            
            var addedCount = 0
            
            // Para cada unidade encontrada, verificar se já existe e criar se não
            unitsFromOpenAI.forEach { unitData ->
                try {
                    val existingUnit = measurementUnitRepository.findByName(unitData.name) 
                        ?: measurementUnitRepository.findByAbbreviation(unitData.abbreviation)
                    
                    if (existingUnit == null) {
                        // Unidade não existe, criar
                        val newUnit = MeasurementUnit(
                            name = unitData.name,
                            abbreviation = unitData.abbreviation,
                            description = unitData.description ?: "Unidade encontrada automaticamente via ChatGPT",
                            multiplyQuantityByPrice = unitData.multiplyQuantityByPrice
                        )
                        measurementUnitRepository.insertMeasurementUnit(newUnit)
                        addedCount++
                    }
                } catch (e: Exception) {
                    // Continua com as próximas unidades se uma falhar
                }
            }
            
            Result.success(addedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
