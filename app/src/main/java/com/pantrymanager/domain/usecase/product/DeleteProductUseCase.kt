package com.pantrymanager.domain.usecase.product

import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Unit> {
        return try {
            productRepository.deleteProduct(product)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend operator fun invoke(productId: Long, userId: String): Result<Unit> {
        return try {
            productRepository.deleteProductById(productId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
