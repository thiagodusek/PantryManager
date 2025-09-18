package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class AddMeasurementUnitUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(unit: MeasurementUnit): Result<Long> {
        return try {
            if (unit.name.isBlank()) {
                return Result.failure(Exception("Nome da unidade de medida é obrigatório"))
            }
            
            if (unit.abbreviation.isBlank()) {
                return Result.failure(Exception("Abreviação da unidade de medida é obrigatória"))
            }
            
            val unitId = measurementUnitRepository.insertMeasurementUnit(unit)
            Result.success(unitId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
