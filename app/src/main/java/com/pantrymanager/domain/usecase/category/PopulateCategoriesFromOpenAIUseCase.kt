package com.pantrymanager.domain.usecase.category

import com.pantrymanager.data.service.OpenAICategorySearchService
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import javax.inject.Inject

class PopulateCategoriesFromOpenAIUseCase @Inject constructor(
    private val openAICategorySearchService: OpenAICategorySearchService,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            // Buscar todas as categorias de supermercado no ChatGPT
            val categoriesFromOpenAI = openAICategorySearchService.getAllSupermarketCategories()
            
            if (categoriesFromOpenAI.isEmpty()) {
                return Result.failure(Exception("Nenhuma categoria encontrada no ChatGPT"))
            }
            
            var addedCount = 0
            
            // Para cada categoria encontrada, verificar se já existe e criar se não
            categoriesFromOpenAI.forEach { categoryName ->
                try {
                    val existingCategory = categoryRepository.findByName(categoryName)
                    if (existingCategory == null) {
                        // Categoria não existe, criar
                        val newCategory = Category(
                            name = categoryName,
                            description = "Categoria encontrada automaticamente via ChatGPT",
                            color = generateRandomColor(),
                            icon = "category"
                        )
                        categoryRepository.insertCategory(newCategory)
                        addedCount++
                    }
                } catch (e: Exception) {
                    // Continua com as próximas categorias se uma falhar
                }
            }
            
            Result.success(addedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateRandomColor(): String {
        val colors = listOf(
            "#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5",
            "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50",
            "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800",
            "#FF5722", "#795548", "#9E9E9E", "#607D8B"
        )
        return colors.random()
    }
}
