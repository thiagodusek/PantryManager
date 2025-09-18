package com.pantrymanager.domain.usecase.brand

import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject

/**
 * Use Case para remover marcas duplicadas
 */
class RemoveDuplicateBrandsUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            // 1. Buscar todas as marcas
            val allBrands = brandRepository.getAllBrands()
            
            // 2. Agrupar por nome (case-insensitive)
            val brandGroups = allBrands.groupBy { it.name.lowercase().trim() }
            
            // 3. Identificar marcas duplicadas (grupos com mais de 1 item)
            val duplicateGroups = brandGroups.filter { it.value.size > 1 }
            
            var removedCount = 0
            
            // 4. Para cada grupo de duplicatas, manter apenas uma (a mais antiga)
            duplicateGroups.forEach { (_, brands) ->
                // Ordenar por ID (assumindo que IDs menores = mais antigos)
                val sortedBrands = brands.sortedBy { it.id }
                val brandToKeep = sortedBrands.first()
                val brandsToRemove = sortedBrands.drop(1) // Remove o primeiro, mantém o resto
                
                // Remover as duplicatas
                brandsToRemove.forEach { brand ->
                    try {
                        brandRepository.deleteBrand(brand.id)
                        removedCount++
                    } catch (e: Exception) {
                        // Log error but continue
                        android.util.Log.w("RemoveDuplicateBrands", "Erro ao remover marca duplicada '${brand.name}': ${e.message}")
                    }
                }
            }
            
            Result.success(removedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
