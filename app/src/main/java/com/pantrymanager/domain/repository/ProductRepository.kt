package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(userId: String): Flow<List<Product>>
    fun getProductById(id: Long, userId: String): Flow<Product?>
    fun getProductsByCategory(categoryId: Long, userId: String): Flow<List<Product>>
    fun getProductByEan(ean: String, userId: String): Flow<Product?>
    fun searchProducts(query: String, userId: String): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun deleteProductById(id: Long, userId: String)
    fun getMostConsumedProducts(userId: String, limit: Int = 10): Flow<List<Product>>
    suspend fun deleteProducts(ids: List<Long>, userId: String)
    suspend fun findOrCreateProductByEan(ean: String): Product?
}
