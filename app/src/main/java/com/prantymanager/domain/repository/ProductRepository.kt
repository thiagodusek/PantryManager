package com.prantymanager.domain.repository

import com.prantymanager.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(id: Long): Flow<Product?>
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>>
    fun getProductByEan(ean: String): Flow<Product?>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun deleteProductById(id: Long)
    fun getMostConsumedProducts(userId: String, limit: Int = 10): Flow<List<Product>>
}
