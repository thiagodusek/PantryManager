package com.prantymanager.domain.usecase.product

import com.prantymanager.domain.entity.Product
import com.prantymanager.domain.repository.ProductRepository
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
    
    suspend operator fun invoke(productId: Long): Result<Unit> {
        return try {
            productRepository.deleteProductById(productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
