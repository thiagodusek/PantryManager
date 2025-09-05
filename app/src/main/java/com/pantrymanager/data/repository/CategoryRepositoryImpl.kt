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
    
    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getCategoryById(id: Long): Flow<Category?> {
        return dao.getCategoryById(id).map { entity ->
            entity?.toDomain()
        }
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
}
