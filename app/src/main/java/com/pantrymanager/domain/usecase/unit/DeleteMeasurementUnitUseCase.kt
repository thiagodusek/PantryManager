package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class DeleteMeasurementUnitUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(measurementUnit: MeasurementUnit): Result<Unit> {
        return try {
            measurementUnitRepository.deleteMeasurementUnit(measurementUnit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend operator fun invoke(unitId: Long): Result<Unit> {
        return try {
            measurementUnitRepository.deleteMeasurementUnitById(unitId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}