package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.CategoryDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {
    
    override suspend fun getAllCategories(): List<Category> {
        return dao.getAllCategoriesSuspend().map { it.toDomain() }
    }
    
    override suspend fun getCategoryById(id: Long): Category? {
        return dao.getCategoryById(id)?.toDomain()
    }
    
    override suspend fun insertCategory(category: Category): Long {
        return dao.insertCategory(category.toEntity())
    }
    
    override suspend fun updateCategory(category: Category) {
        dao.updateCategory(category.toEntity())
    }
    
    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category.toEntity())
    }
    
    override suspend fun deleteCategoryById(id: Long) {
        dao.deleteCategoryById(id)
    }
    
    override suspend fun findByName(name: String): Category? {
        return dao.findByName(name)?.toDomain()
    }
    
    override suspend fun searchCategories(query: String): List<Category> {
        return dao.searchCategories("%$query%").map { it.toDomain() }
    }
    
    override suspend fun getCategoriesByParent(parentCategoryId: Long?): List<Category> {
        // Para implementação futura se houver hierarquia de categorias
        return emptyList()
    }
}
