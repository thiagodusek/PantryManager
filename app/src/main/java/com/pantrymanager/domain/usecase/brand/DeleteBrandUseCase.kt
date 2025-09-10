package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

class DeleteBrandUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(brand: Brand): Result<Unit> {
        return try {
            brandRepository.deleteBrand(brand)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend operator fun invoke(brandId: Long): Result<Unit> {
        return try {
            brandRepository.deleteBrandById(brandId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteMultiple(brandIds: List<Long>): Result<Unit> {
        return try {
            brandIds.forEach { brandId ->
                brandRepository.deleteBrandById(brandId)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
