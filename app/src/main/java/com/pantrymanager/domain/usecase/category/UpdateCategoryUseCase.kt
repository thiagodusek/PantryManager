package com.pantrymanager.domain.usecase.category

import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Unit> {
        return try {
            // Validação do campo obrigatório
            if (category.name.isBlank()) {
                return Result.failure(Exception("Nome da categoria é obrigatório"))
            }
            
            categoryRepository.updateCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
