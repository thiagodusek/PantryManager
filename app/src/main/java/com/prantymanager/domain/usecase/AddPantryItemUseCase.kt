package com.prantymanager.domain.usecase

import com.prantymanager.domain.entity.PantryItem
import com.prantymanager.domain.repository.PantryItemRepository
import javax.inject.Inject

class AddPantryItemUseCase @Inject constructor(
    private val repository: PantryItemRepository
) {
    suspend operator fun invoke(item: PantryItem): Result<Long> {
        return try {
            val id = repository.insertItem(item)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
