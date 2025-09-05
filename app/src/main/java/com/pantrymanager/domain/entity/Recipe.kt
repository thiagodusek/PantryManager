package com.pantrymanager.domain.entity

import java.time.LocalDate

data class Recipe(
    val id: Long = 0,
    val userId: String,
    val name: String,
    val description: String? = null,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<String>,
    val prepTime: Int? = null, // minutes
    val cookTime: Int? = null, // minutes
    val servings: Int = 1,
    val difficulty: RecipeDifficulty = RecipeDifficulty.MEDIUM,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val nutritionalInfo: NutritionalInfo? = null,
    val rating: Double? = null,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)

data class RecipeIngredient(
    val productId: Long,
    val quantity: Double,
    val unit: String,
    val notes: String? = null
)

enum class RecipeDifficulty {
    EASY, MEDIUM, HARD
}
