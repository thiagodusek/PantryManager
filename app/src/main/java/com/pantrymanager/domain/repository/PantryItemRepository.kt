package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.PantryItem
import kotlinx.coroutines.flow.Flow

interface PantryItemRepository {
    fun getAllItems(): Flow<List<PantryItem>>
    fun getItemById(id: Long): Flow<PantryItem?>
    fun getItemsByUserId(userId: String): Flow<List<PantryItem>>
    fun getExpiredItems(): Flow<List<PantryItem>>
    fun getItemsExpiringWithin(days: Int): Flow<List<PantryItem>>
    suspend fun insertItem(item: PantryItem): Long
    suspend fun updateItem(item: PantryItem)
    suspend fun deleteItem(item: PantryItem)
    suspend fun deleteItemById(id: Long)
    fun searchItems(query: String): Flow<List<PantryItem>>
}
