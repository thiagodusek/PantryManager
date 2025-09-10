package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

class AddBrandUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(brand: Brand): Result<Long> {
        return try {
            if (brand.name.isBlank()) {
                return Result.failure(Exception("Nome da marca é obrigatório"))
            }
            
            val brandId = brandRepository.insertBrand(brand)
            Result.success(brandId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
