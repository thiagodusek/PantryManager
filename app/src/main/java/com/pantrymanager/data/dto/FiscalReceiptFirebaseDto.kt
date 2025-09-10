package com.pantrymanager.data.dto

import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.entity.FiscalReceiptItem
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * DTO Firebase para Cupom Fiscal
 */
data class FiscalReceiptFirebaseDto(
    val id: String = "",
    val userId: String = "",
    val storeId: String? = null,
    val storeName: String = "",
    val storeCnpj: String? = null,
    val receiptNumber: String = "",
    val accessKey: String? = null,
    val totalAmount: Double = 0.0,
    val purchaseDate: Long = 0L, // timestamp
    val isProcessed: Boolean = false,
    val processingNotes: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    // Construtor vazio para Firebase
    constructor() : this("", "", null, "", null, "", null, 0.0, 0L, false, null, 0L, 0L)
}

/**
 * DTO Firebase para Item do Cupom Fiscal
 */
data class FiscalReceiptItemFirebaseDto(
    val id: String = "",
    val fiscalReceiptId: String = "",
    val productCode: String? = null,
    val ean: String? = null,
    val name: String = "",
    val description: String? = null,
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val unit: String? = null,
    val category: String? = null,
    val brand: String? = null,
    val isImported: Boolean = false,
    val importedProductId: String? = null,
    val importNotes: String? = null
) {
    // Construtor vazio para Firebase
    constructor() : this("", "", null, null, "", null, 0.0, 0.0, 0.0, null, null, null, false, null, null)
}

/**
 * Extensões para conversão entre entidades de domínio e DTOs Firebase
 */
fun FiscalReceipt.toFirebaseDto(): FiscalReceiptFirebaseDto {
    return FiscalReceiptFirebaseDto(
        id = this.id.toString(),
        userId = this.userId,
        storeId = this.storeId,
        storeName = this.storeName,
        storeCnpj = this.storeCnpj,
        receiptNumber = this.receiptNumber,
        accessKey = this.accessKey,
        totalAmount = this.totalAmount,
        purchaseDate = this.purchaseDate.toEpochSecond(ZoneOffset.UTC) * 1000,
        isProcessed = this.isProcessed,
        processingNotes = this.processingNotes,
        createdAt = this.createdAt.toEpochSecond(ZoneOffset.UTC) * 1000,
        updatedAt = this.updatedAt.toEpochSecond(ZoneOffset.UTC) * 1000
    )
}

fun FiscalReceiptFirebaseDto.toDomain(items: List<FiscalReceiptItem> = emptyList()): FiscalReceipt {
    return FiscalReceipt(
        id = this.id.hashCode().toLong(),
        userId = this.userId,
        storeId = this.storeId,
        storeName = this.storeName,
        storeCnpj = this.storeCnpj,
        receiptNumber = this.receiptNumber,
        accessKey = this.accessKey,
        totalAmount = this.totalAmount,
        purchaseDate = LocalDateTime.ofEpochSecond(this.purchaseDate / 1000, 0, ZoneOffset.UTC),
        items = items,
        isProcessed = this.isProcessed,
        processingNotes = this.processingNotes,
        createdAt = LocalDateTime.ofEpochSecond(this.createdAt / 1000, 0, ZoneOffset.UTC),
        updatedAt = LocalDateTime.ofEpochSecond(this.updatedAt / 1000, 0, ZoneOffset.UTC)
    )
}

fun FiscalReceiptItem.toFirebaseDto(): FiscalReceiptItemFirebaseDto {
    return FiscalReceiptItemFirebaseDto(
        id = this.id.toString(),
        fiscalReceiptId = this.fiscalReceiptId.toString(),
        productCode = this.productCode,
        ean = this.ean,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        unitPrice = this.unitPrice,
        totalPrice = this.totalPrice,
        unit = this.unit,
        category = this.category,
        brand = this.brand,
        isImported = this.isImported,
        importedProductId = this.importedProductId?.toString(),
        importNotes = this.importNotes
    )
}

fun FiscalReceiptItemFirebaseDto.toDomain(): FiscalReceiptItem {
    return FiscalReceiptItem(
        id = this.id.hashCode().toLong(),
        fiscalReceiptId = this.fiscalReceiptId.hashCode().toLong(),
        productCode = this.productCode,
        ean = this.ean,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        unitPrice = this.unitPrice,
        totalPrice = this.totalPrice,
        unit = this.unit,
        category = this.category,
        brand = this.brand,
        isImported = this.isImported,
        importedProductId = this.importedProductId?.hashCode()?.toLong(),
        importNotes = this.importNotes
    )
}
