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
    
    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getProductById(id: Long): Flow<Product?> {
        return dao.getProductById(id).map { entity ->
            entity?.toDomain()
        }
    }
    
    override fun getProductsByCategory(categoryId: Long): Flow<List<Product>> {
        return dao.getProductsByCategory(categoryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getProductByEan(ean: String): Flow<Product?> {
        return dao.getProductByEan(ean).map { entity ->
            entity?.toDomain()
        }
    }
    
    override fun searchProducts(query: String): Flow<List<Product>> {
        return dao.searchProducts(query).map { entities ->
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
    
    override suspend fun deleteProductById(id: Long) {
        dao.deleteProductById(id)
    }
    
    override fun getMostConsumedProducts(userId: String, limit: Int): Flow<List<Product>> {
        // Por enquanto ignora userId, retorna produtos ordenados por nome  
        return dao.getMostConsumedProducts(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
