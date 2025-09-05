package com.pantrymanager.domain.usecase.category

import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Unit> {
        return try {
            categoryRepository.deleteCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend operator fun invoke(categoryId: Long): Result<Unit> {
        return try {
            categoryRepository.deleteCategoryById(categoryId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
