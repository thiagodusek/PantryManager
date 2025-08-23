package com.prantymanager.domain.usecase.product

import com.prantymanager.domain.entity.Product
import com.prantymanager.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getAllProducts().first()
    }
}
