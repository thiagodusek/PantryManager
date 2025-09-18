package com.pantrymanager.domain.usecase.category

import com.pantrymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class RemoveDuplicateCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            val allCategories = categoryRepository.getAllCategories()
            val categoriesToRemove = mutableListOf<Long>()
            val seenNames = mutableSetOf<String>()
            
            // Identifica duplicatas (mantém a primeira ocorrência)
            allCategories.forEach { category ->
                val normalizedName = category.name.trim().lowercase()
                if (seenNames.contains(normalizedName)) {
                    categoriesToRemove.add(category.id)
                } else {
                    seenNames.add(normalizedName)
                }
            }
            
            // Remove as duplicatas
            if (categoriesToRemove.isNotEmpty()) {
                categoryRepository.deleteCategories(categoriesToRemove)
            }
            
            Result.success(categoriesToRemove.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
