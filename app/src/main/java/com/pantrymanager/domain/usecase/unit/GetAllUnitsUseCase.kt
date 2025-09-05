package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.domain.repository.UnitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAllUnitsUseCase @Inject constructor(
    private val unitRepository: UnitRepository
) {
    suspend operator fun invoke(): List<UnitEntity> {
        return unitRepository.getAllUnits().first()
    }
}
