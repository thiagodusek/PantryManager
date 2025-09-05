package com.prantymanager.domain.usecase.unit

import com.prantymanager.domain.entity.MeasurementUnit as UnitEntity
import com.prantymanager.domain.repository.UnitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAllUnitsUseCase @Inject constructor(
    private val unitRepository: UnitRepository
) {
    suspend operator fun invoke(): List<UnitEntity> {
        return unitRepository.getAllUnits().first()
    }
}
