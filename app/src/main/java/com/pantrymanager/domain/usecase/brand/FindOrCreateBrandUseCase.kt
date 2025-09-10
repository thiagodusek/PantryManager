package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

class FindOrCreateBrandUseCase @Inject constructor(
    private val brandRepository: BrandRepository,
    private val addBrandUseCase: AddBrandUseCase
) {
    suspend operator fun invoke(brandName: String): Result<Brand> {
        return try {
            if (brandName.isBlank()) {
                return Result.failure(Exception("Nome da marca não pode estar vazio"))
            }
            
            // Primeiro tenta buscar uma marca existente
            val existingBrand = brandRepository.findByName(brandName)
            if (existingBrand != null) {
                return Result.success(existingBrand)
            }
            
            // Se não existe, cria uma nova marca
            val newBrand = Brand(
                name = brandName.trim(),
                description = "Marca cadastrada automaticamente",
                isActive = true
            )
            
            val result = addBrandUseCase(newBrand)
            if (result.isSuccess) {
                val brandId = result.getOrThrow()
                val createdBrand = brandRepository.getBrandById(brandId)
                if (createdBrand != null) {
                    Result.success(createdBrand)
                } else {
                    Result.failure(Exception("Erro ao recuperar marca criada"))
                }
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Erro ao criar marca"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
