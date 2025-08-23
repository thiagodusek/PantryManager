package com.prantymanager.domain.repository

import com.prantymanager.domain.entity.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipesByUser(userId: String): Flow<List<Recipe>>
    fun getRecipeById(id: Long): Flow<Recipe?>
    fun searchRecipes(query: String): Flow<List<Recipe>>
    suspend fun insertRecipe(recipe: Recipe): Long
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun deleteRecipeById(id: Long)
    fun getRecipesByIngredients(productIds: List<Long>): Flow<List<Recipe>>
    fun getFavoriteRecipes(userId: String): Flow<List<Recipe>>
}
