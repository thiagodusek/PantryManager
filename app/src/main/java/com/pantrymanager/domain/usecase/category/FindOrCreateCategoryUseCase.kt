package com.pantrymanager.domain.usecase.category

import com.pantrymanager.data.defaults.DefaultCategories
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class FindOrCreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val addCategoryUseCase: AddCategoryUseCase
) {
    suspend operator fun invoke(categoryName: String): Result<Category> {
        return try {
            if (categoryName.isBlank()) {
                return Result.failure(Exception("Nome da categoria não pode estar vazio"))
            }
            
            // Primeiro tenta buscar uma categoria existente
            val existingCategory = categoryRepository.findByName(categoryName)
            if (existingCategory != null) {
                return Result.success(existingCategory)
            }
            
            // Busca nos dados padrão primeiro
            val defaultCategory = DefaultCategories.findByName(categoryName)
            if (defaultCategory != null) {
                // Se existe nos dados padrão, adiciona ao banco
                val result = addCategoryUseCase(defaultCategory)
                if (result.isSuccess) {
                    val categoryId = result.getOrThrow()
                    val createdCategory = categoryRepository.getCategoryById(categoryId)
                    return if (createdCategory != null) {
                        Result.success(createdCategory)
                    } else {
                        Result.failure(Exception("Erro ao recuperar categoria padrão criada"))
                    }
                }
            }
            
            // Se não existe nos padrões, cria uma nova categoria personalizada
            val newCategory = Category(
                name = categoryName.trim(),
                description = "Categoria cadastrada automaticamente via pesquisa de produto",
                color = "#607D8B", // Cor padrão (cinza azulado)
                icon = "category", // Ícone padrão
                isActive = true
            )
            
            val result = addCategoryUseCase(newCategory)
            if (result.isSuccess) {
                val categoryId = result.getOrThrow()
                val createdCategory = categoryRepository.getCategoryById(categoryId)
                return if (createdCategory != null) {
                    Result.success(createdCategory)
                } else {
                    Result.failure(Exception("Erro ao recuperar categoria criada"))
                }
            } else {
                return Result.failure(result.exceptionOrNull() ?: Exception("Erro ao criar categoria"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
