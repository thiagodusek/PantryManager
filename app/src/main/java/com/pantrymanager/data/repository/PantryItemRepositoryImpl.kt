package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.PantryItemDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.PantryItem
import com.pantrymanager.domain.repository.PantryItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PantryItemRepositoryImpl @Inject constructor(
    private val dao: PantryItemDao
) : PantryItemRepository {
    
    override fun getAllItems(): Flow<List<PantryItem>> {
        return dao.getAllItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getItemById(id: Long): Flow<PantryItem?> {
        return dao.getItemById(id).map { entity ->
            entity?.toDomain()
        }
    }
    
    override fun getItemsByUserId(userId: String): Flow<List<PantryItem>> {
        return dao.getItemsByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getExpiredItems(): Flow<List<PantryItem>> {
        return dao.getExpiredItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getItemsExpiringWithin(days: Int): Flow<List<PantryItem>> {
        return dao.getItemsExpiringWithin(days).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun insertItem(item: PantryItem): Long {
        return dao.insertItem(item.toEntity())
    }
    
    override suspend fun updateItem(item: PantryItem) {
        dao.updateItem(item.toEntity())
    }
    
    override suspend fun deleteItem(item: PantryItem) {
        dao.deleteItem(item.toEntity())
    }
    
    override suspend fun deleteItemById(id: Long) {
        dao.deleteItemById(id)
    }
    
    override fun searchItems(query: String): Flow<List<PantryItem>> {
        return dao.searchItems(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
