package com.pantrymanager.domain.usecase.shoppinglist

import com.pantrymanager.domain.entity.ShoppingList
import com.pantrymanager.domain.repository.ShoppingListRepository
import javax.inject.Inject

class GenerateAutomaticShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(userId: String): Result<ShoppingList> {
        return try {
            shoppingListRepository.generateAutomaticShoppingList(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
