package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.Brand

interface BrandRepository {
    suspend fun insertBrand(brand: Brand): Long
    suspend fun updateBrand(brand: Brand)
    suspend fun deleteBrand(brandId: Long)
    suspend fun getBrandById(brandId: Long): Brand?
    suspend fun getAllBrands(): List<Brand>
    suspend fun findByName(name: String): Brand?
    suspend fun searchBrands(query: String): List<Brand>
    
    // Métodos para exclusão múltipla
    suspend fun deleteBrands(ids: List<Long>)
    
    // Método para busca ou criação automática
    suspend fun findOrCreateBrand(name: String): Brand
}
