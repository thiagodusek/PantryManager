package com.pantrymanager.domain.entity

data class Store(
    val id: Long = 0,
    val name: String,
    val address: Address,
    val phone: String? = null,
    val email: String? = null,
    val website: String? = null,
    val rating: Double? = null,
    val distance: Double? = null, // km
    val isOpen: Boolean = true,
    val openingHours: List<OpeningHour> = emptyList(),
    val imageUrl: String? = null
)

data class OpeningHour(
    val dayOfWeek: Int, // 1-7 (Sunday-Saturday)
    val openTime: String, // "08:00"
    val closeTime: String // "22:00"
)

data class StorePrice(
    val id: Long = 0,
    val storeId: Long,
    val productId: Long,
    val price: Double,
    val unit: String,
    val isOnPromotion: Boolean = false,
    val promotionPrice: Double? = null,
    val promotionEndDate: Long? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)
