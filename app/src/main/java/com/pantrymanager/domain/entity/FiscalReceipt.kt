package com.pantrymanager.domain.entity

import java.time.LocalDateTime

/**
 * Entidade que representa um cupom fiscal
 */
data class FiscalReceipt(
    val id: Long = 0,
    val userId: String = "",
    val storeId: String? = null,
    val storeName: String = "",
    val storeCnpj: String? = null,
    val receiptNumber: String = "",
    val accessKey: String? = null, // Chave de acesso da NFCe
    val totalAmount: Double = 0.0,
    val purchaseDate: LocalDateTime = LocalDateTime.now(),
    val items: List<FiscalReceiptItem> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isProcessed: Boolean = false, // Se os produtos já foram importados
    val processingNotes: String? = null // Observações sobre o processamento
)

/**
 * Item de um cupom fiscal
 */
data class FiscalReceiptItem(
    val id: Long = 0,
    val fiscalReceiptId: Long = 0,
    val productCode: String? = null, // Código do produto no estabelecimento
    val ean: String? = null, // EAN/código de barras
    val name: String = "",
    val description: String? = null,
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val unit: String? = null, // Unidade (kg, un, etc)
    val category: String? = null, // Categoria inferida ou extraída
    val brand: String? = null, // Marca inferida ou extraída
    val isImported: Boolean = false, // Se já foi importado como produto
    val importedProductId: Long? = null, // ID do produto importado
    val importNotes: String? = null // Observações sobre a importação
)
