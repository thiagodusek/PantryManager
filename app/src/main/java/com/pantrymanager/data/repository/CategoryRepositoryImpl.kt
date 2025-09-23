package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.CategoryDao
import com.pantrymanager.data.mapper.CategoryMapper
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {

    override suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            val categoryEntities = categoryDao.getAllCategories()
            categoryEntities.map { categoryMapper.entityToDomain(it) }
        } catch (e: Exception) {
            // Se houver erro na base, retorna lista vazia
            emptyList()
        }
    }

    override suspend fun getCategoryById(id: Long): Category? = withContext(Dispatchers.IO) {
        try {
            val categoryEntity = categoryDao.getCategoryById(id)
            categoryEntity?.let { categoryMapper.entityToDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertCategory(category: Category): Long = withContext(Dispatchers.IO) {
        val categoryEntity = categoryMapper.domainToEntity(category)
        categoryDao.insertCategory(categoryEntity)
    }

    override suspend fun updateCategory(category: Category) = withContext(Dispatchers.IO) {
        val categoryEntity = categoryMapper.domainToEntity(category)
        categoryDao.updateCategory(categoryEntity)
    }

    override suspend fun deleteCategory(category: Category) = withContext(Dispatchers.IO) {
        val categoryEntity = categoryMapper.domainToEntity(category)
        categoryDao.deleteCategory(categoryEntity)
    }

    override suspend fun deleteCategoryById(id: Long) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategoryById(id)
    }

    override suspend fun findByName(name: String): Category? = withContext(Dispatchers.IO) {
        try {
            val categoryEntity = categoryDao.findByName(name)
            categoryEntity?.let { categoryMapper.entityToDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchCategories(query: String): List<Category> = withContext(Dispatchers.IO) {
        try {
            val categoryEntities = categoryDao.searchCategories("%$query%")
            categoryEntities.map { categoryMapper.entityToDomain(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCategoriesByParent(parentCategoryId: Long?): List<Category> = withContext(Dispatchers.IO) {
        try {
            // Por enquanto, retorna todas as categorias
            val categoryEntities = categoryDao.getAllCategories()
            categoryEntities.map { categoryMapper.entityToDomain(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteCategories(ids: List<Long>) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategories(ids)
    }

    override suspend fun findOrCreateCategory(name: String): Category = withContext(Dispatchers.IO) {
        // Primeiro tenta encontrar
        val existing = findByName(name)
        if (existing != null) {
            return@withContext existing
        }
        
        // Se não encontrar, cria uma nova
        val newCategory = Category(
            id = 0, // Será gerado automaticamente
            name = name.trim(),
            description = null,
            color = "#1976D2",
            icon = "category",
            parentCategoryId = null
        )
        
        val newId = insertCategory(newCategory)
        newCategory.copy(id = newId)
    }
}