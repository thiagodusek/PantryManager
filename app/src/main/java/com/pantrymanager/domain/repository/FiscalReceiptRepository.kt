package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.entity.FiscalReceiptItem
import kotlinx.coroutines.flow.Flow

interface FiscalReceiptRepository {
    fun getFiscalReceiptsByUser(userId: String): Flow<List<FiscalReceipt>>
    fun getFiscalReceiptById(id: Long): Flow<FiscalReceipt?>
    suspend fun insertFiscalReceipt(fiscalReceipt: FiscalReceipt): Long
    suspend fun updateFiscalReceipt(fiscalReceipt: FiscalReceipt)
    suspend fun deleteFiscalReceipt(fiscalReceipt: FiscalReceipt)
    suspend fun deleteFiscalReceiptById(id: Long)
    
    // Métodos específicos para itens
    fun getFiscalReceiptItems(fiscalReceiptId: Long): Flow<List<FiscalReceiptItem>>
    suspend fun insertFiscalReceiptItems(items: List<FiscalReceiptItem>)
    suspend fun updateFiscalReceiptItem(item: FiscalReceiptItem)
    suspend fun markItemAsImported(itemId: Long, productId: Long)
    
    // Métodos de importação
    suspend fun processFiscalReceiptItems(fiscalReceiptId: Long): Result<List<Long>>
    suspend fun importItemsAsProducts(itemIds: List<Long>): Result<List<Long>>
    
    // Busca e filtros
    fun getUnprocessedFiscalReceipts(userId: String): Flow<List<FiscalReceipt>>
    fun getFiscalReceiptsByStore(userId: String, storeName: String): Flow<List<FiscalReceipt>>
    suspend fun searchFiscalReceipts(userId: String, query: String): List<FiscalReceipt>
}
