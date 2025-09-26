package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pantrymanager.data.dto.BrandFirebaseDto
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toFirebaseDto
import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.repository.BrandRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrandFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : BrandRepository {
    
    companion object {
        private const val BRANDS_COLLECTION = "brands"
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    override suspend fun getAllBrands(): List<Brand> {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(BRANDS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(BrandFirebaseDto::class.java)?.let { dto ->
                    Brand(
                        id = doc.id.hashCode().toLong().let { if (it < 0) -it else it }, // Garantir ID positivo
                        name = dto.name
                    )
                }
            }
        } catch (e: Exception) {
            println("DEBUG - Erro ao carregar marcas: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getBrandById(brandId: Long): Brand? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(BRANDS_COLLECTION)
                .document(brandId.toString())
                .get()
                .await()
            
            snapshot.toObject(BrandFirebaseDto::class.java)?.let { dto ->
                if (dto.userId == userId) {
                    dto.copy(id = snapshot.id).toDomain()
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun findByName(name: String): Brand? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(BRANDS_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("name", name)
                .limit(1)
                .get()
                .await()
            
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(BrandFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchBrands(query: String): List<Brand> {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(BRANDS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .get()
                .await()
            
            // Firebase doesn't support case-insensitive text search out of the box
            // So we filter on the client side
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(BrandFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }.filter { brand ->
                brand.name.contains(query, ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun insertBrand(brand: Brand): Long {
        return try {
            val userId = getCurrentUserId()
            val brandDto = brand.toFirebaseDto(userId).copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(BRANDS_COLLECTION)
                .add(brandDto)
                .await()
                
            println("DEBUG - Marca inserida com ID: ${docRef.id}")
            // Retorna o hash do document ID como Long positivo
            docRef.id.hashCode().toLong().let { if (it < 0) -it else it }
        } catch (e: Exception) {
            println("DEBUG - Erro ao inserir marca: ${e.message}")
            throw e
        }
    }

    override suspend fun updateBrand(brand: Brand) {
        try {
            val userId = getCurrentUserId()
            
            // Busca o documento pela combinação userId + brandId
            val snapshot = firestore.collection(BRANDS_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
                
            // Encontra o documento correto comparando o hash do document ID
            val docToUpdate = snapshot.documents.find { doc ->
                val hashId = doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                hashId == brand.id
            }
            
            if (docToUpdate != null) {
                val brandDto = brand.toFirebaseDto(userId).copy(
                    updatedAt = System.currentTimeMillis()
                )
                
                firestore.collection(BRANDS_COLLECTION)
                    .document(docToUpdate.id)
                    .set(brandDto)
                    .await()
                    
                println("DEBUG - Marca atualizada com sucesso: ${docToUpdate.id}")
            } else {
                throw IllegalArgumentException("Marca não encontrada")
            }
        } catch (e: Exception) {
            println("DEBUG - Erro ao atualizar marca: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteBrand(brandId: Long) {
        try {
            val userId = getCurrentUserId()
            
            // Busca o documento pela combinação userId + brandId
            val snapshot = firestore.collection(BRANDS_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
                
            // Encontra o documento correto comparando o hash do document ID
            val docToDelete = snapshot.documents.find { doc ->
                val hashId = doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                hashId == brandId
            }
            
            if (docToDelete != null) {
                firestore.collection(BRANDS_COLLECTION)
                    .document(docToDelete.id)
                    .delete()
                    .await()
                println("DEBUG - Marca deletada com sucesso: ${docToDelete.id}")
            } else {
                throw IllegalArgumentException("Marca não encontrada")
            }
        } catch (e: Exception) {
            println("DEBUG - Erro ao deletar marca: ${e.message}")
            throw e
        }
    }

    /**
     * Busca ou cria uma marca pelo nome
     */
    override suspend fun findOrCreateBrand(name: String): Brand {
        return try {
            val userId = getCurrentUserId()
            
            // First try to find existing brand
            val existingBrand = findByName(name)
            if (existingBrand != null) {
                return existingBrand
            }
            
            // Create new brand if not found
            val newBrand = Brand(
                id = 0, // Will be overridden by Firebase
                name = name
            )
            
            val brandDto = newBrand.toFirebaseDto(userId).copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(BRANDS_COLLECTION)
                .add(brandDto)
                .await()
                
            newBrand.copy(id = docRef.id.hashCode().toLong())
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Exclui múltiplas marcas
     */
    override suspend fun deleteBrands(ids: List<Long>) {
        try {
            val userId = getCurrentUserId()
            
            // Use batch write for better performance
            val batch = firestore.batch()
            
            ids.forEach { id ->
                val docRef = firestore.collection(BRANDS_COLLECTION)
                    .document(id.toString())
                batch.delete(docRef)
            }
            
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }
}
