package com.pantrymanager.domain.usecase.product

import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Long): Product? {
        return productRepository.getProductById(productId).first()
    }
}
