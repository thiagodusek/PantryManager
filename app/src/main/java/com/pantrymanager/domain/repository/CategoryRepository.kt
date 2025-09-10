package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.Category

interface CategoryRepository {
    suspend fun getAllCategories(): List<Category>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun insertCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun deleteCategoryById(id: Long)
    
    // Métodos adicionais para busca e criação automática
    suspend fun findByName(name: String): Category?
    suspend fun searchCategories(query: String): List<Category>
    suspend fun getCategoriesByParent(parentCategoryId: Long?): List<Category>
}
