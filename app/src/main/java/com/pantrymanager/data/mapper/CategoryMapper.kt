package com.pantrymanager.data.mapper

import com.pantrymanager.data.dto.CategoryEntity
import com.pantrymanager.domain.entity.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryMapper @Inject constructor() {

    fun entityToDomain(entity: CategoryEntity): Category {
        return Category(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            color = entity.color ?: "#1976D2",
            icon = entity.icon ?: "category",
            parentCategoryId = entity.parentCategoryId
        )
    }

    fun domainToEntity(domain: Category): CategoryEntity {
        return CategoryEntity(
            id = domain.id,
            name = domain.name,
            color = domain.color,
            icon = domain.icon,
            description = domain.description,
            parentCategoryId = domain.parentCategoryId
        )
    }

    fun entityListToDomainList(entities: List<CategoryEntity>): List<Category> {
        return entities.map { entityToDomain(it) }
    }

    fun domainListToEntityList(domains: List<Category>): List<CategoryEntity> {
        return domains.map { domainToEntity(it) }
    }
}
