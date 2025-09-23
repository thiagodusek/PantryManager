package com.pantrymanager.data.mapper

import com.pantrymanager.data.dto.MeasurementUnitEntity
import com.pantrymanager.domain.entity.MeasurementUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementUnitMapper @Inject constructor() {

    fun entityToDomain(entity: MeasurementUnitEntity): MeasurementUnit {
        return MeasurementUnit(
            id = entity.id,
            name = entity.name,
            abbreviation = entity.abbreviation,
            description = entity.description,
            multiplyQuantityByPrice = entity.multiplyQuantityByPrice
        )
    }

    fun domainToEntity(domain: MeasurementUnit): MeasurementUnitEntity {
        return MeasurementUnitEntity(
            id = domain.id,
            name = domain.name,
            abbreviation = domain.abbreviation,
            description = domain.description,
            multiplyQuantityByPrice = domain.multiplyQuantityByPrice,
            createdAt = domain.createdAt.toString(),
            updatedAt = domain.updatedAt.toString()
        )
    }

    fun entityListToDomainList(entities: List<MeasurementUnitEntity>): List<MeasurementUnit> {
        return entities.map { entityToDomain(it) }
    }

    fun domainListToEntityList(domains: List<MeasurementUnit>): List<MeasurementUnitEntity> {
        return domains.map { domainToEntity(it) }
    }
}
