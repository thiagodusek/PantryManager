package com.pantrymanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pantrymanager.data.dto.PantryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PantryItemDao {
    
    @Query("SELECT * FROM pantry_items ORDER BY updatedAt DESC")
    fun getAllItems(): Flow<List<PantryItemEntity>>
    
    @Query("SELECT * FROM pantry_items WHERE id = :id")
    fun getItemById(id: Long): Flow<PantryItemEntity?>
    
    @Query("SELECT * FROM pantry_items WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getItemsByUser(userId: String): Flow<List<PantryItemEntity>>
    
    @Query("SELECT * FROM pantry_items WHERE productId = :productId ORDER BY updatedAt DESC")
    fun getItemsByProduct(productId: Long): Flow<List<PantryItemEntity>>
    
    @Query("SELECT * FROM pantry_items WHERE isExpired = 1 ORDER BY updatedAt DESC")
    fun getExpiredItems(): Flow<List<PantryItemEntity>>
    
    @Query("SELECT * FROM pantry_items WHERE expiryDate <= date('now', '+' || :days || ' days') AND expiryDate IS NOT NULL ORDER BY expiryDate ASC")
    fun getItemsExpiringWithin(days: Int): Flow<List<PantryItemEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: PantryItemEntity): Long
    
    @Update
    suspend fun updateItem(item: PantryItemEntity)
    
    @Delete
    suspend fun deleteItem(item: PantryItemEntity)
    
    @Query("DELETE FROM pantry_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)
    
    @Query("SELECT pi.* FROM pantry_items pi INNER JOIN products p ON pi.productId = p.id WHERE p.name LIKE '%' || :query || '%' OR p.description LIKE '%' || :query || '%' ORDER BY pi.updatedAt DESC")
    fun searchItems(query: String): Flow<List<PantryItemEntity>>
}
