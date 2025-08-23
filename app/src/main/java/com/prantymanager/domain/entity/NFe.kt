package com.prantymanager.domain.entity

import java.time.LocalDate

data class NFe(
    val id: Long = 0,
    val userId: String,
    val accessKey: String, // Chave de acesso da NFe
    val issueDate: LocalDate,
    val storeCnpj: String,
    val storeName: String,
    val totalValue: Double,
    val items: List<NFeItem>,
    val processed: Boolean = false,
    val createdAt: LocalDate = LocalDate.now()
)

data class NFeItem(
    val id: Long = 0,
    val nfeId: Long,
    val productName: String,
    val productCode: String? = null,
    val quantity: Double,
    val unit: String,
    val unitPrice: Double,
    val totalPrice: Double,
    val productId: Long? = null // Link to existing product if matched
)
