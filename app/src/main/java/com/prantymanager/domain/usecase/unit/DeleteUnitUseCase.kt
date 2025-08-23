package com.prantymanager.domain.usecase.unit

import com.prantymanager.domain.entity.MeasurementUnit as UnitEntity
import com.prantymanager.domain.repository.UnitRepository
import javax.inject.Inject

class DeleteUnitUseCase @Inject constructor(
    private val unitRepository: UnitRepository
) {
    suspend operator fun invoke(unit: UnitEntity): Result<kotlin.Unit> {
        return try {
            unitRepository.deleteUnit(unit)
            Result.success(kotlin.Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend operator fun invoke(unitId: Long): Result<kotlin.Unit> {
        return try {
            unitRepository.deleteUnitById(unitId)
            Result.success(kotlin.Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
