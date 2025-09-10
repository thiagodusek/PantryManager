package com.pantrymanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pantrymanager.data.dto.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products WHERE userId = :userId ORDER BY name ASC")
    fun getAllProducts(userId: String): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE id = :id AND userId = :userId")
    fun getProductById(id: Long, userId: String): Flow<ProductEntity?>
    
    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND userId = :userId ORDER BY name ASC")
    fun getProductsByCategory(categoryId: Long, userId: String): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR ean LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%') ORDER BY name ASC")
    fun searchProducts(query: String, userId: String): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE ean = :ean AND userId = :userId LIMIT 1")
    fun getProductByEan(ean: String, userId: String): Flow<ProductEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long
    
    @Update
    suspend fun updateProduct(product: ProductEntity)
    
    @Delete
    suspend fun deleteProduct(product: ProductEntity)
    
    @Query("DELETE FROM products WHERE id = :id AND userId = :userId")
    suspend fun deleteProductById(id: Long, userId: String)
    
    @Query("SELECT * FROM products WHERE userId = :userId ORDER BY name ASC LIMIT :limit")
    fun getMostConsumedProducts(limit: Int, userId: String): Flow<List<ProductEntity>>
}
