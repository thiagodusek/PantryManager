package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.BrandDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrandRepositoryImpl @Inject constructor(
    private val dao: BrandDao
) : BrandRepository {
    
    override suspend fun getAllBrands(): List<Brand> {
        return dao.getAllBrands().map { it.toDomain() }
    }
    
    override suspend fun getBrandById(brandId: Long): Brand? {
        return dao.getBrandById(brandId)?.toDomain()
    }
    
    override suspend fun findByName(name: String): Brand? {
        return dao.findByName(name)?.toDomain()
    }
    
    override suspend fun searchBrands(query: String): List<Brand> {
        return dao.searchBrands("%$query%").map { it.toDomain() }
    }
    
    override suspend fun insertBrand(brand: Brand): Long {
        return dao.insertBrand(brand.toEntity())
    }
    
    override suspend fun updateBrand(brand: Brand) {
        dao.updateBrand(brand.toEntity())
    }
    
    override suspend fun deleteBrand(brandId: Long) {
        dao.deleteBrandById(brandId)
    }
}
