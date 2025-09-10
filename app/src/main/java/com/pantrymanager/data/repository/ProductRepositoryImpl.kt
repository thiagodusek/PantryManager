package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.ProductDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
) : ProductRepository {
    
    override fun getAllProducts(userId: String): Flow<List<Product>> {
        return dao.getAllProducts(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getProductById(id: Long, userId: String): Flow<Product?> {
        return dao.getProductById(id, userId).map { entity ->
            entity?.toDomain()
        }
    }
    
    override fun getProductsByCategory(categoryId: Long, userId: String): Flow<List<Product>> {
        return dao.getProductsByCategory(categoryId, userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getProductByEan(ean: String, userId: String): Flow<Product?> {
        return dao.getProductByEan(ean, userId).map { entity ->
            entity?.toDomain()
        }
    }
    
    override fun searchProducts(query: String, userId: String): Flow<List<Product>> {
        return dao.searchProducts(query, userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun insertProduct(product: Product): Long {
        return dao.insertProduct(product.toEntity())
    }
    
    override suspend fun updateProduct(product: Product) {
        dao.updateProduct(product.toEntity())
    }
    
    override suspend fun deleteProduct(product: Product) {
        dao.deleteProduct(product.toEntity())
    }
    
    override suspend fun deleteProductById(id: Long, userId: String) {
        dao.deleteProductById(id, userId)
    }
    
    override fun getMostConsumedProducts(userId: String, limit: Int): Flow<List<Product>> {
        return dao.getMostConsumedProducts(limit, userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
