package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

class FindOrCreateBrandUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(brandName: String): Result<Brand> {
        return try {
            if (brandName.isBlank()) {
                return Result.failure(Exception("Nome da marca não pode estar vazio"))
            }
            
            // Usa o método findOrCreateBrand do repositório Firebase
            val brand = brandRepository.findOrCreateBrand(brandName.trim())
            Result.success(brand)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
