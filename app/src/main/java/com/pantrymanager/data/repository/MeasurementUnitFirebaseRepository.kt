package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pantrymanager.data.dto.MeasurementUnitFirebaseDto
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toFirebaseDto
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementUnitFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MeasurementUnitRepository {
    
    companion object {
        private const val MEASUREMENT_UNITS_COLLECTION = "measurement_units"
    }

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    override suspend fun getAllMeasurementUnits(): List<MeasurementUnit> {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(MeasurementUnitFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getMeasurementUnitById(unitId: Long): MeasurementUnit? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .document(unitId.toString())
                .get()
                .await()
            
            snapshot.toObject(MeasurementUnitFirebaseDto::class.java)?.let { dto ->
                if (dto.userId == userId) {
                    dto.copy(id = snapshot.id).toDomain()
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertMeasurementUnit(unit: MeasurementUnit): Long {
        return try {
            val userId = getCurrentUserId()
            val unitDto = unit.toFirebaseDto(userId).copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .add(unitDto)
                .await()
                
            docRef.id.hashCode().toLong()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateMeasurementUnit(unit: MeasurementUnit) {
        try {
            val userId = getCurrentUserId()
            val unitDto = unit.toFirebaseDto(userId).copy(
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .document(unit.id.toString())
                .set(unitDto)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteMeasurementUnit(unitId: Long) {
        try {
            val userId = getCurrentUserId()
            
            // First verify the unit belongs to the user
            val doc = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .document(unitId.toString())
                .get()
                .await()
                
            val unitDto = doc.toObject(MeasurementUnitFirebaseDto::class.java)
            if (unitDto?.userId == userId) {
                firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                    .document(unitId.toString())
                    .delete()
                    .await()
            } else {
                throw SecurityException("User not authorized to delete this measurement unit")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteMeasurementUnits(ids: List<Long>) {
        try {
            val userId = getCurrentUserId()
            
            val batch = firestore.batch()
            
            ids.forEach { id ->
                val docRef = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                    .document(id.toString())
                batch.delete(docRef)
            }
            
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun findByName(name: String): MeasurementUnit? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("name", name)
                .limit(1)
                .get()
                .await()
            
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(MeasurementUnitFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun findByAbbreviation(abbreviation: String): MeasurementUnit? {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("abbreviation", abbreviation)
                .limit(1)
                .get()
                .await()
            
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(MeasurementUnitFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchMeasurementUnits(query: String): List<MeasurementUnit> {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("name")
                .get()
                .await()
            
            // Firebase doesn't support case-insensitive text search out of the box
            // So we filter on the client side
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(MeasurementUnitFirebaseDto::class.java)?.let { dto ->
                    dto.copy(id = doc.id).toDomain()
                }
            }.filter { unit ->
                unit.name.contains(query, ignoreCase = true) ||
                unit.abbreviation.contains(query, ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun findOrCreateMeasurementUnit(name: String, abbreviation: String): MeasurementUnit {
        try {
            val userId = getCurrentUserId()
            
            // First try to find existing unit
            val existingUnit = findByName(name)
            if (existingUnit != null) {
                return existingUnit
            }
            
            // Create new unit if not found
            val newUnit = MeasurementUnit(
                id = 0, // Will be overridden by Firebase
                name = name,
                abbreviation = abbreviation,
                multiplyQuantityByPrice = false
            )
            
            val unitDto = newUnit.toFirebaseDto(userId).copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val docRef = firestore.collection(MEASUREMENT_UNITS_COLLECTION)
                .add(unitDto)
                .await()
                
            return newUnit.copy(id = docRef.id.hashCode().toLong())
        } catch (e: Exception) {
            throw e
        }
    }
}
