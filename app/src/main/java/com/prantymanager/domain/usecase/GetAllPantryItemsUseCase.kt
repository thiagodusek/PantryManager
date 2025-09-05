package com.prantymanager.domain.usecase

import com.prantymanager.domain.entity.PantryItem
import com.prantymanager.domain.repository.PantryItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPantryItemsUseCase @Inject constructor(
    private val repository: PantryItemRepository
) {
    operator fun invoke(): Flow<List<PantryItem>> {
        return repository.getAllItems()
    }
}
