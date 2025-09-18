package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.Store
import com.pantrymanager.domain.entity.StorePrice
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getNearbyStores(latitude: Double, longitude: Double, radiusKm: Double): Flow<List<Store>>
    fun getStoreById(id: Long): Flow<Store?>
    suspend fun insertStore(store: Store): Long
    suspend fun updateStore(store: Store)
    suspend fun deleteStore(store: Store)
    fun getAllStores(): Flow<List<Store>>
    fun searchStores(query: String): Flow<List<Store>>
    
    // Store prices
    fun getPricesForProduct(productId: Long): Flow<List<StorePrice>>
    fun getPricesForStore(storeId: Long): Flow<List<StorePrice>>
    suspend fun updateStorePrice(storePrice: StorePrice)
    fun getPromotions(): Flow<List<StorePrice>>
    fun getBestPricesForProducts(productIds: List<Long>): Flow<List<StorePrice>>
}
