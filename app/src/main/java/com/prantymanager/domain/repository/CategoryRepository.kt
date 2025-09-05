package com.prantymanager.domain.repository

import com.prantymanager.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoryById(id: Long): Flow<Category?>
    suspend fun insertCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun deleteCategoryById(id: Long)
}
