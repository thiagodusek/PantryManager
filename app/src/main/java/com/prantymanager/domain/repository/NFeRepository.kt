package com.prantymanager.domain.repository

import com.prantymanager.domain.entity.NFe
import kotlinx.coroutines.flow.Flow

interface NFeRepository {
    suspend fun importNFeByAccessKey(userId: String, accessKey: String): Result<NFe>
    suspend fun processNFeItems(nfe: NFe): Result<Unit>
    fun getNFesByUser(userId: String): Flow<List<NFe>>
    fun getNFeById(id: Long): Flow<NFe?>
    suspend fun deleteNFe(nfe: NFe)
    suspend fun deleteNFeById(id: Long)
    fun getUnprocessedNFes(userId: String): Flow<List<NFe>>
}
