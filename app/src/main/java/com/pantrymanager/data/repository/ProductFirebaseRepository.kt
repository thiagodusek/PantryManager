package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pantrymanager.data.dto.ProductFirebaseDto
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toFirebaseDto
import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProductRepository {
    
    companion object {
        private const val PRODUCTS_COLLECTION = "products"
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    override fun getAllProducts(userId: String): Flow<List<Product>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .orderBy("name")
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getProductById(id: Long, userId: String): Flow<Product?> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .document(id.toString())
                .get()
                .await()
            
            val product = snapshot.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                if (dto.userId == actualUserId) {
                    dto.copy(id = snapshot.id).toDomain()
                } else null
            }
            emit(product)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun getProductsByCategory(categoryId: Long, userId: String): Flow<List<Product>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .whereEqualTo("categoryId", categoryId)
                .orderBy("name")
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getProductByEan(ean: String, userId: String): Flow<Product?> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .whereEqualTo("ean", ean)
                .limit(1)
                .get()
                .await()
            
            val product = snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(product)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun searchProducts(query: String, userId: String): Flow<List<Product>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .orderBy("name")
                .get()
                .await()
            
            // Firebase doesn't support case-insensitive text search out of the box
            // So we filter on the client side
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.description?.contains(query, ignoreCase = true) == true ||
                product.ean?.contains(query) == true
            }
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    /**
     * Verifica se um EAN já existe para o usuário atual
     */
    suspend fun isEanAlreadyExists(ean: String, userId: String = "", excludeProductId: Long? = null): Boolean {
        return try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            var query = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .whereEqualTo("ean", ean)
            
            // Se estamos editando um produto, excluí-lo da verificação
            if (excludeProductId != null) {
                query = query.whereNotEqualTo("id", excludeProductId.toString())
            }
            
            val snapshot = query.limit(1).get().await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun insertProduct(product: Product): Long {
        return try {
            val userId = getCurrentUserId()
            
            // Validar EAN único se fornecido
            if (!product.ean.isNullOrBlank()) {
                if (isEanAlreadyExists(product.ean, userId)) {
                    throw IllegalArgumentException("Já existe um produto cadastrado com o código EAN ${product.ean}")
                }
            }
            
            val productDto = product.toFirebaseDto().copy(
                userId = userId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(PRODUCTS_COLLECTION)
                .add(productDto)
                .await()
                
            println("DEBUG - Produto inserido com ID: ${docRef.id}")
            // Retorna o hash do document ID como Long positivo
            docRef.id.hashCode().toLong().let { if (it < 0) -it else it }
        } catch (e: Exception) {
            println("DEBUG - Erro ao inserir produto: ${e.message}")
            throw e
        }
    }

    override suspend fun updateProduct(product: Product) {
        try {
            val userId = getCurrentUserId()
            
            // Validar EAN único se fornecido (excluindo o produto atual)
            if (!product.ean.isNullOrBlank()) {
                if (isEanAlreadyExists(product.ean, userId, product.id)) {
                    throw IllegalArgumentException("Já existe outro produto cadastrado com o código EAN ${product.ean}")
                }
            }
            
            // Busca o documento pela combinação userId + productId
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
                
            // Encontra o documento correto comparando o hash do document ID
            val docToUpdate = snapshot.documents.find { doc ->
                val hashId = doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                hashId == product.id
            }
            
            if (docToUpdate != null) {
                val productDto = product.toFirebaseDto().copy(
                    userId = userId,
                    updatedAt = System.currentTimeMillis()
                )
                
                firestore.collection(PRODUCTS_COLLECTION)
                    .document(docToUpdate.id)
                    .set(productDto)
                    .await()
                    
                println("DEBUG - Produto atualizado com sucesso: ${docToUpdate.id}")
            } else {
                throw IllegalArgumentException("Produto não encontrado")
            }
        } catch (e: Exception) {
            println("DEBUG - Erro ao atualizar produto: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteProduct(product: Product) {
        deleteProductById(product.id, "")
    }

    override suspend fun deleteProductById(id: Long, userId: String) {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            
            // Busca o documento pela combinação userId + productId
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .get()
                .await()
                
            // Encontra o documento correto comparando o hash do document ID
            val docToDelete = snapshot.documents.find { doc ->
                val hashId = doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                hashId == id
            }
            
            if (docToDelete != null) {
                firestore.collection(PRODUCTS_COLLECTION)
                    .document(docToDelete.id)
                    .delete()
                    .await()
                println("DEBUG - Produto deletado com sucesso: ${docToDelete.id}")
            } else {
                throw IllegalArgumentException("Produto não encontrado")
            }
        } catch (e: Exception) {
            println("DEBUG - Erro ao deletar produto: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteProducts(ids: List<Long>, userId: String) {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            
            // Use batch write for better performance
            val batch = firestore.batch()
            
            ids.forEach { id ->
                val docRef = firestore.collection(PRODUCTS_COLLECTION)
                    .document(id.toString())
                batch.delete(docRef)
            }
            
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getMostConsumedProducts(userId: String, limit: Int): Flow<List<Product>> = flow {
        try {
            val actualUserId = userId.ifEmpty { getCurrentUserId() }
            // For now, return top products by name - in a real implementation,
            // this would be based on consumption analytics stored in Firebase
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", actualUserId)
                .orderBy("name")
                .limit(limit.toLong())
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    /**
     * Busca ou cria um produto pelo código de barras/EAN
     */
    override suspend fun findOrCreateProductByEan(ean: String): Product? {
        return try {
            val userId = getCurrentUserId()
            
            // First try to find existing product
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("ean", ean)
                .limit(1)
                .get()
                .await()
            
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(ProductFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
