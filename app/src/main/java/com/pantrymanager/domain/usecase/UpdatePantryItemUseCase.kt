package com.pantrymanager.domain.usecase

import com.pantrymanager.domain.entity.PantryItem
import com.pantrymanager.domain.repository.PantryItemRepository
import javax.inject.Inject

class UpdatePantryItemUseCase @Inject constructor(
    private val repository: PantryItemRepository
) {
    suspend operator fun invoke(item: PantryItem): Result<Unit> {
        return try {
            repository.updateItem(item)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
