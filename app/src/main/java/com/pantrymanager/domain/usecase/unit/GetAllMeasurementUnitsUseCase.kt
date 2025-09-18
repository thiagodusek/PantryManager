package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import javax.inject.Inject

class GetAllMeasurementUnitsUseCase @Inject constructor(
    private val measurementUnitRepository: MeasurementUnitRepository
) {
    suspend operator fun invoke(): List<MeasurementUnit> {
        return measurementUnitRepository.getAllMeasurementUnits()
    }
}
