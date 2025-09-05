package com.prantymanager.domain.usecase.category

import com.prantymanager.domain.entity.Category
import com.prantymanager.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getAllCategories().first()
    }
}
