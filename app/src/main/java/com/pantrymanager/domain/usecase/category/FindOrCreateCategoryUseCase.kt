package com.pantrymanager.domain.usecase.category

import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class FindOrCreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryName: String): Result<Category> {
        return try {
            if (categoryName.isBlank()) {
                return Result.failure(Exception("Nome da categoria não pode estar vazio"))
            }
            
            // Usa o método findOrCreateCategory do repositório Firebase
            val category = categoryRepository.findOrCreateCategory(categoryName.trim())
            Result.success(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
