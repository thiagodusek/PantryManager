package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getShoppingListsByUser(userId: String): Flow<List<ShoppingList>>
    fun getShoppingListById(id: Long): Flow<ShoppingList?>
    suspend fun insertShoppingList(shoppingList: ShoppingList): Long
    suspend fun updateShoppingList(shoppingList: ShoppingList)
    suspend fun deleteShoppingList(shoppingList: ShoppingList)
    suspend fun deleteShoppingListById(id: Long)
    suspend fun generateAutomaticShoppingList(userId: String): Result<ShoppingList>
    fun getActiveShoppingLists(userId: String): Flow<List<ShoppingList>>
}
