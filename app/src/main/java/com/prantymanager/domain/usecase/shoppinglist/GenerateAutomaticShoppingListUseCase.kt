package com.prantymanager.domain.usecase.shoppinglist

import com.prantymanager.domain.entity.ShoppingList
import com.prantymanager.domain.repository.ShoppingListRepository
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
