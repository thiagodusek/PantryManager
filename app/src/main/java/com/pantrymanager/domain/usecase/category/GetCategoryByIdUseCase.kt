package com.pantrymanager.domain.usecase.category

import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Long): Category? {
        return categoryRepository.getCategoryById(categoryId).first()
    }
}
