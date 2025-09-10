package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pantrymanager.data.dto.CategoryFirebaseDto
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toFirebaseDto
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CategoryRepository {
    
    companion object {
        private const val CATEGORIES_COLLECTION = "categories"
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    override suspend fun getAllCategories(): List<Category> {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CATEGORIES_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(CategoryFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CATEGORIES_COLLECTION)
                .document(id.toString())
                .get()
                .await()
            
            snapshot.toObject(CategoryFirebaseDto::class.java)?.let { dto ->
                if (dto.userId == userId) {
                    dto.copy(id = snapshot.id).toDomain()
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertCategory(category: Category): Long {
        return try {
            val userId = getCurrentUserId()
            val categoryDto = category.toFirebaseDto(userId).copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(CATEGORIES_COLLECTION)
                .add(categoryDto)
                .await()
                
            docRef.id.hashCode().toLong()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateCategory(category: Category) {
        try {
            val userId = getCurrentUserId()
            val categoryDto = category.toFirebaseDto(userId).copy(
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection(CATEGORIES_COLLECTION)
                .document(category.id.toString())
                .set(categoryDto)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteCategory(category: Category) {
        try {
            deleteCategoryById(category.id)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteCategoryById(id: Long) {
        try {
            val userId = getCurrentUserId()
            
            // First verify the category belongs to the user
            val doc = firestore.collection(CATEGORIES_COLLECTION)
                .document(id.toString())
                .get()
                .await()
                
            val categoryDto = doc.toObject(CategoryFirebaseDto::class.java)
            if (categoryDto?.userId == userId) {
                firestore.collection(CATEGORIES_COLLECTION)
                    .document(id.toString())
                    .delete()
                    .await()
            } else {
                throw SecurityException("User not authorized to delete this category")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun findByName(name: String): Category? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CATEGORIES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("name", name)
                .limit(1)
                .get()
                .await()
            
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(CategoryFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchCategories(query: String): List<Category> {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CATEGORIES_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .get()
                .await()
            
            // Firebase doesn't support case-insensitive text search out of the box
            // So we filter on the client side
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(CategoryFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }.filter { category ->
                category.name.contains(query, ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCategoriesByParent(parentCategoryId: Long?): List<Category> {
        // For now, return empty list as we don't have parent categories implemented
        return emptyList()
    }

    override suspend fun deleteCategories(ids: List<Long>) {
        try {
            val userId = getCurrentUserId()
            
            val batch = firestore.batch()
            
            ids.forEach { id ->
                val docRef = firestore.collection(CATEGORIES_COLLECTION)
                    .document(id.toString())
                batch.delete(docRef)
            }
            
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun findOrCreateCategory(name: String): Category {
        try {
            val userId = getCurrentUserId()
            
            // First try to find existing category
            val existingCategory = findByName(name)
            if (existingCategory != null) {
                return existingCategory
            }
            
            // Create new category if not found
            val newCategory = Category(
                id = 0, // Will be overridden by Firebase
                name = name,
                color = "#2196F3",
                icon = "category"
            )
            
            val categoryDto = newCategory.toFirebaseDto(userId).copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(CATEGORIES_COLLECTION)
                .add(categoryDto)
                .await()
                
            return newCategory.copy(id = docRef.id.hashCode().toLong())
        } catch (e: Exception) {
            throw e
        }
    }
}
