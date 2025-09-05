package com.pantrymanager.domain.usecase

import com.pantrymanager.domain.entity.PantryItem
import com.pantrymanager.domain.repository.PantryItemRepository
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
