package com.pantrymanager.domain.entity

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Entidade principal que representa um produto do usuário
 * 1 usuário pode ter vários produtos
 * 1 produto tem 1 categoria, 1 unidade de medida, 1 marca
 * 1 produto pode ter vários lotes (ProductBatch)
 */
data class Product(
    val id: Long = 0L,
    val userId: String, // ID do usuário dono do produto (obrigatório)
    val ean: String? = null, // Código de barras opcional
    val name: String, // Nome obrigatório
    val description: String? = null, // Descrição opcional
    
    // Referencias para outras entidades
    val categoryId: Long, // ID da categoria (obrigatório)
    val unitId: Long, // ID da unidade de medida (obrigatório)
    val brandId: Long? = null, // ID da marca (opcional)
    
    // Informações adicionais
    val imageUrl: String? = null, // Foto opcional
    val averagePrice: Double = 0.0,
    val observation: String? = null, // Observação opcional
    val nutritionalInfo: NutritionalInfo? = null,
    
    // Controle
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Propriedades calculadas baseadas nos lotes
    fun getTotalQuantity(batches: List<ProductBatch>): Double {
        return batches.filter { !it.isConsumed }.sumOf { it.quantity }
    }
    
    fun getEarliestExpiryDate(batches: List<ProductBatch>): LocalDate? {
        return batches.filter { !it.isConsumed }
            .minByOrNull { it.expiryDate }?.expiryDate
    }
    
    fun hasExpiredBatches(batches: List<ProductBatch>): Boolean {
        return batches.any { !it.isConsumed && it.expiryDate.isBefore(LocalDate.now()) }
    }
}

data class NutritionalInfo(
    val calories: Double? = null,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fat: Double? = null,
    val fiber: Double? = null,
    val sodium: Double? = null
)
