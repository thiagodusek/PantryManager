package com.pantrymanager.domain.usecase

import com.pantrymanager.domain.entity.PantryItem
import com.pantrymanager.domain.repository.PantryItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpiredItemsUseCase @Inject constructor(
    private val repository: PantryItemRepository
) {
    operator fun invoke(): Flow<List<PantryItem>> {
        return repository.getExpiredItems()
    }
}
