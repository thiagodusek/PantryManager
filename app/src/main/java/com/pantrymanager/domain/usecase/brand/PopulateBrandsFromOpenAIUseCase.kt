package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.data.service.OpenAIBrandSearchService
import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

/**
 * Use Case para buscar marcas automaticamente via ChatGPT e cadastrar as novas
 */
class PopulateBrandsFromOpenAIUseCase @Inject constructor(
    private val openAIBrandSearchService: OpenAIBrandSearchService,
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            // 1. Buscar marcas via ChatGPT
            val brandsResult = openAIBrandSearchService.searchPopularBrands()
            
            if (brandsResult.isFailure) {
                return Result.failure(brandsResult.exceptionOrNull() ?: Exception("Erro ao buscar marcas no ChatGPT"))
            }
            
            val brandNames = brandsResult.getOrThrow()
            
            // 2. Buscar marcas já existentes no banco
            val existingBrands = brandRepository.getAllBrands()
            val existingBrandNames = existingBrands.map { it.name.lowercase() }.toSet()
            
            // 3. Filtrar apenas marcas novas
            val newBrandNames = brandNames.filter { brandName ->
                !existingBrandNames.contains(brandName.lowercase())
            }
            
            // 4. Cadastrar marcas novas
            var addedCount = 0
            newBrandNames.forEach { brandName ->
                try {
                    val brand = Brand(
                        id = 0, // Será gerado automaticamente
                        name = brandName.trim()
                    )
                    brandRepository.insertBrand(brand)
                    addedCount++
                } catch (e: Exception) {
                    // Log error but continue with next brand
                    android.util.Log.w("PopulateBrands", "Erro ao cadastrar marca '$brandName': ${e.message}")
                }
            }
            
            Result.success(addedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
