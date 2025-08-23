package com.prantymanager.domain.usecase.unit

import com.prantymanager.domain.entity.MeasurementUnit as UnitEntity
import com.prantymanager.domain.repository.UnitRepository
import javax.inject.Inject

class UpdateUnitUseCase @Inject constructor(
    private val unitRepository: UnitRepository
) {
    suspend operator fun invoke(unit: UnitEntity): Result<kotlin.Unit> {
        return try {
            // Validação dos campos obrigatórios
            if (unit.name.isBlank()) {
                return Result.failure(Exception("Nome da unidade é obrigatório"))
            }
            
            if (unit.abbreviation.isBlank()) {
                return Result.failure(Exception("Abreviação é obrigatória"))
            }
            
            unitRepository.updateUnit(unit)
            Result.success(kotlin.Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
