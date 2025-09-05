package com.pantrymanager.domain.usecase.unit

import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.domain.repository.UnitRepository
import javax.inject.Inject

class AddUnitUseCase @Inject constructor(
    private val unitRepository: UnitRepository
) {
    suspend operator fun invoke(unit: UnitEntity): Result<Long> {
        return try {
            if (unit.name.isBlank()) {
                return Result.failure(Exception("Nome da unidade é obrigatório"))
            }
            
            val unitId = unitRepository.insertUnit(unit)
            Result.success(unitId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
