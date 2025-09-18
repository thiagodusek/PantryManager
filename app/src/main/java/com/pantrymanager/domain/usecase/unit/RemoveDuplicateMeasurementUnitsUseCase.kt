package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class RemoveDuplicateMeasurementUnitsUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            val allUnits = measurementUnitRepository.getAllMeasurementUnits()
            val unitsToRemove = mutableListOf<Long>()
            val seenNames = mutableSetOf<String>()
            val seenAbbreviations = mutableSetOf<String>()
            
            // Identifica duplicatas por nome ou abreviação (mantém a primeira ocorrência)
            allUnits.forEach { unit ->
                val normalizedName = unit.name.trim().lowercase()
                val normalizedAbbreviation = unit.abbreviation.trim().lowercase()
                
                if (seenNames.contains(normalizedName) || seenAbbreviations.contains(normalizedAbbreviation)) {
                    unitsToRemove.add(unit.id)
                } else {
                    seenNames.add(normalizedName)
                    seenAbbreviations.add(normalizedAbbreviation)
                }
            }
            
            // Remove as duplicatas
            if (unitsToRemove.isNotEmpty()) {
                measurementUnitRepository.deleteMeasurementUnits(unitsToRemove)
            }
            
            Result.success(unitsToRemove.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
