package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

class GetAllBrandsUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(): List<Brand> {
        return brandRepository.getAllBrands()
    }
}
