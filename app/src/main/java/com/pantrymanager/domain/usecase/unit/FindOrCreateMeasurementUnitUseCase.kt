package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class FindOrCreateMeasurementUnitUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(unitName: String, abbreviation: String? = null): Result<MeasurementUnit> {
        return try {
            if (unitName.isBlank()) {
                return Result.failure(Exception("Nome da unidade não pode estar vazio"))
            }
            
            // Usa o método findOrCreateMeasurementUnit do repositório Firebase
            val unit = measurementUnitRepository.findOrCreateMeasurementUnit(
                name = unitName.trim(),
                abbreviation = abbreviation?.trim() ?: unitName.trim()
            )
            Result.success(unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
