package com.prantymanager.domain.usecase.category

import com.prantymanager.domain.entity.Category
import com.prantymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Long> {
        return try {
            if (category.name.isBlank()) {
                return Result.failure(Exception("Nome da categoria é obrigatório"))
            }
            
            val categoryId = categoryRepository.insertCategory(category)
            Result.success(categoryId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
