package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class UpdateMeasurementUnitUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(unit: MeasurementUnit) {
        measurementUnitRepository.updateMeasurementUnit(unit)
    }
}