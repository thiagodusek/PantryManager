package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pantrymanager.data.dto.FiscalReceiptFirebaseDto
import com.pantrymanager.data.dto.FiscalReceiptItemFirebaseDto
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toFirebaseDto
import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.entity.FiscalReceiptItem
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.entity.ProductBatch
import com.pantrymanager.domain.repository.BrandRepository
import com.pantrymanager.domain.repository.CategoryRepository
import com.pantrymanager.domain.repository.FiscalReceiptRepository
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import com.pantrymanager.domain.repository.ProductRepository
import com.pantrymanager.domain.repository.ProductBatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiscalReceiptFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val productBatchRepository: ProductBatchRepository
) : FiscalReceiptRepository {
    
    companion object {
        private const val FISCAL_RECEIPTS_COLLECTION = "fiscal_receipts"
        private const val FISCAL_RECEIPT_ITEMS_COLLECTION = "fiscal_receipt_items"
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    override fun getFiscalReceiptsByUser(userId: String): Flow<List<FiscalReceipt>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .orderBy("purchaseDate")
                .get()
                .await()
            
            val receipts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(FiscalReceiptFirebaseDto::class.java)?.let { dto ->
                    // Load items for each receipt
                    val itemsSnapshot = firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                        .whereEqualTo("fiscalReceiptId", doc.id)
                        .get()
                        .await()
                    
                    val items = itemsSnapshot.documents.mapNotNull { itemDoc ->
                        itemDoc.toObject(FiscalReceiptItemFirebaseDto::class.java)?.let { itemDto ->
                            itemDto.copy(id = itemDoc.id).toDomain()
                        }
                    }
                    
                    dto.copy(id = doc.id).toDomain(items)
                }
            }
            emit(receipts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getFiscalReceiptById(id: Long): Flow<FiscalReceipt?> = flow {
        try {
            val snapshot = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .document(id.toString())
                .get()
                .await()
            
            val receipt = snapshot.toObject(FiscalReceiptFirebaseDto::class.java)?.let { dto ->
                // Load items
                val itemsSnapshot = firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                    .whereEqualTo("fiscalReceiptId", snapshot.id)
                    .get()
                    .await()
                
                val items = itemsSnapshot.documents.mapNotNull { itemDoc ->
                    itemDoc.toObject(FiscalReceiptItemFirebaseDto::class.java)?.let { itemDto ->
                        itemDto.copy(id = itemDoc.id).toDomain()
                    }
                }
                
                dto.copy(id = snapshot.id).toDomain(items)
            }
            emit(receipt)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override suspend fun insertFiscalReceipt(fiscalReceipt: FiscalReceipt): Long {
        return try {
            val userId = getCurrentUserId()
            val receiptDto = fiscalReceipt.toFirebaseDto().copy(
                userId = userId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .add(receiptDto)
                .await()
            
            // Insert items
            insertFiscalReceiptItems(
                fiscalReceipt.items.map { item ->
                    item.copy(fiscalReceiptId = docRef.id.hashCode().toLong())
                }
            )
                
            docRef.id.hashCode().toLong()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateFiscalReceipt(fiscalReceipt: FiscalReceipt) {
        try {
            val userId = getCurrentUserId()
            val receiptDto = fiscalReceipt.toFirebaseDto().copy(
                userId = userId,
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .document(fiscalReceipt.id.toString())
                .set(receiptDto)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteFiscalReceipt(fiscalReceipt: FiscalReceipt) {
        deleteFiscalReceiptById(fiscalReceipt.id)
    }

    override suspend fun deleteFiscalReceiptById(id: Long) {
        try {
            // Delete items first
            val itemsSnapshot = firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                .whereEqualTo("fiscalReceiptId", id.toString())
                .get()
                .await()
            
            val batch = firestore.batch()
            itemsSnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            
            // Delete receipt
            val receiptRef = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .document(id.toString())
            batch.delete(receiptRef)
            
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getFiscalReceiptItems(fiscalReceiptId: Long): Flow<List<FiscalReceiptItem>> = flow {
        try {
            val snapshot = firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                .whereEqualTo("fiscalReceiptId", fiscalReceiptId.toString())
                .orderBy("name")
                .get()
                .await()
            
            val items = snapshot.documents.mapNotNull { doc ->
                doc.toObject(FiscalReceiptItemFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(items)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun insertFiscalReceiptItems(items: List<FiscalReceiptItem>) {
        try {
            val batch = firestore.batch()
            
            items.forEach { item ->
                val itemDto = item.toFirebaseDto()
                val docRef = firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION).document()
                batch.set(docRef, itemDto)
            }
            
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateFiscalReceiptItem(item: FiscalReceiptItem) {
        try {
            val itemDto = item.toFirebaseDto()
            
            firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                .document(item.id.toString())
                .set(itemDto)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun markItemAsImported(itemId: Long, productId: Long) {
        try {
            firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                .document(itemId.toString())
                .update(
                    mapOf(
                        "isImported" to true,
                        "importedProductId" to productId.toString(),
                        "importNotes" to "Produto importado automaticamente do cupom fiscal"
                    )
                )
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun processFiscalReceiptItems(fiscalReceiptId: Long): Result<List<Long>> {
        return try {
            val items = getFiscalReceiptItems(fiscalReceiptId)
            var importedProductIds = mutableListOf<Long>()
            
            items.collect { itemsList ->
                importedProductIds = importItemsAsProducts(
                    itemsList.filter { !it.isImported }.map { it.id }
                ).getOrThrow().toMutableList()
            }
            
            // Mark receipt as processed
            firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .document(fiscalReceiptId.toString())
                .update(
                    mapOf(
                        "isProcessed" to true,
                        "processingNotes" to "Processamento automático concluído: ${importedProductIds.size} produtos importados",
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
                
            Result.success(importedProductIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun importItemsAsProducts(itemIds: List<Long>): Result<List<Long>> {
        return try {
            val importedProductIds = mutableListOf<Long>()
            
            for (itemId in itemIds) {
                val itemSnapshot = firestore.collection(FISCAL_RECEIPT_ITEMS_COLLECTION)
                    .document(itemId.toString())
                    .get()
                    .await()
                
                val item = itemSnapshot.toObject(FiscalReceiptItemFirebaseDto::class.java)
                    ?.copy(id = itemSnapshot.id)
                    ?.toDomain()
                
                if (item != null) {
                    val productId = importItemAsProduct(item)
                    if (productId != null) {
                        importedProductIds.add(productId)
                        markItemAsImported(itemId, productId)
                    }
                }
            }
            
            Result.success(importedProductIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun importItemAsProduct(item: FiscalReceiptItem): Long? {
        return try {
            // Verificar se já existe produto com mesmo EAN
            if (!item.ean.isNullOrBlank()) {
                val existingProduct = (productRepository as ProductFirebaseRepository)
                    .findOrCreateProductByEan(item.ean)
                
                if (existingProduct != null) {
                    // Produto já existe, apenas criar um novo lote
                    createProductBatch(existingProduct, item)
                    return existingProduct.id
                }
            }
            
            // Criar/buscar entidades relacionadas
            val category = createOrFindCategory(item.category)
            val brand = createOrFindBrand(item.brand)
            val unit = createOrFindMeasurementUnit(item.unit)
            
            // Criar novo produto
            val newProduct = Product(
                name = item.name,
                description = item.description,
                ean = item.ean,
                brand = brand?.name,
                categoryId = category?.id ?: 0L,
                unitId = unit?.id ?: 0L,
                averagePrice = item.unitPrice,
                observation = "Importado automaticamente do cupom fiscal"
            )
            
            val productId = productRepository.insertProduct(newProduct)
            
            // Criar lote do produto
            createProductBatch(newProduct.copy(id = productId), item)
            
            productId
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun createProductBatch(product: Product, item: FiscalReceiptItem): Long? {
        return try {
            val batch = ProductBatch(
                productId = product.id,
                batchNumber = "CUPOM_${item.fiscalReceiptId}_${item.id}",
                quantity = item.quantity,
                expiryDate = LocalDate.now().plusMonths(6), // Data estimada
                purchaseDate = LocalDate.now(),
                purchasePrice = item.totalPrice,
                purchaseLocation = "Importado de cupom fiscal"
            )
            
            productBatchRepository.insertBatch(batch)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun createOrFindCategory(categoryName: String?): Category? {
        return if (!categoryName.isNullOrBlank()) {
            try {
                categoryRepository.findOrCreateCategory(categoryName)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    private suspend fun createOrFindBrand(brandName: String?): Brand? {
        return if (!brandName.isNullOrBlank()) {
            try {
                brandRepository.findOrCreateBrand(brandName)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    private suspend fun createOrFindMeasurementUnit(unitName: String?): MeasurementUnit? {
        return if (!unitName.isNullOrBlank()) {
            try {
                measurementUnitRepository.findOrCreateMeasurementUnit(unitName, unitName)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    override fun getUnprocessedFiscalReceipts(userId: String): Flow<List<FiscalReceipt>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .whereEqualTo("isProcessed", false)
                .orderBy("purchaseDate")
                .get()
                .await()
            
            val receipts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(FiscalReceiptFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(receipts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getFiscalReceiptsByStore(userId: String, storeName: String): Flow<List<FiscalReceipt>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .whereEqualTo("storeName", storeName)
                .orderBy("purchaseDate")
                .get()
                .await()
            
            val receipts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(FiscalReceiptFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(receipts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun searchFiscalReceipts(userId: String, query: String): List<FiscalReceipt> {
        return try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(FISCAL_RECEIPTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .get()
                .await()
            
            // Firebase doesn't support full-text search, so we filter client-side
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(FiscalReceiptFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }.filter { receipt ->
                receipt.storeName.contains(query, ignoreCase = true) ||
                receipt.receiptNumber.contains(query, ignoreCase = true) ||
                receipt.accessKey?.contains(query, ignoreCase = true) == true
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
