package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

class UpdateBrandUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(brand: Brand): Result<Unit> {
        return try {
            // Validação do campo obrigatório
            if (brand.name.isBlank()) {
                return Result.failure(Exception("Nome da marca é obrigatório"))
            }
            
            brandRepository.updateBrand(brand)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
