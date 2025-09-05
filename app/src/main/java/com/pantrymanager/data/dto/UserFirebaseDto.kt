package com.pantrymanager.data.dto

import com.google.firebase.firestore.IgnoreExtraProperties
import com.pantrymanager.domain.entity.Address
import com.pantrymanager.domain.entity.User

/**
 * DTO para representar dados do usuário no Firebase Firestore
 */
@IgnoreExtraProperties
data class UserFirebaseDto(
    val id: String = "",
    val nome: String = "",
    val sobrenome: String = "",
    val email: String = "",
    val cpf: String = "",
    val endereco: AddressFirebaseDto = AddressFirebaseDto(),
    val login: String = "",
    val nfePermissions: Boolean = false,
    val searchRadius: Double = 5.0,
    val profileImageUrl: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    // Construtor vazio necessário para Firebase
    constructor() : this(
        id = "",
        nome = "",
        sobrenome = "",
        email = "",
        cpf = "",
        endereco = AddressFirebaseDto(),
        login = "",
        nfePermissions = false,
        searchRadius = 5.0,
        profileImageUrl = null,
        createdAt = 0L,
        updatedAt = 0L
    )
}

/**
 * DTO para representar endereço no Firebase Firestore
 */
@IgnoreExtraProperties
data class AddressFirebaseDto(
    val endereco: String = "",
    val numero: String = "",
    val complemento: String? = null,
    val cep: String = "",
    val cidade: String = "",
    val estado: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    // Construtor vazio necessário para Firebase
    constructor() : this(
        endereco = "",
        numero = "",
        complemento = null,
        cep = "",
        cidade = "",
        estado = "",
        latitude = null,
        longitude = null
    )
}

/**
 * Extensões para conversão entre entidades de domínio e DTOs Firebase
 */
fun User.toFirebaseDto(): UserFirebaseDto {
    return UserFirebaseDto(
        id = this.id,
        nome = this.nome,
        sobrenome = this.sobrenome,
        email = this.email,
        cpf = this.cpf,
        endereco = this.endereco.toFirebaseDto(),
        login = this.login,
        nfePermissions = this.nfePermissions,
        searchRadius = this.searchRadius,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun UserFirebaseDto.toDomainEntity(): User {
    return User(
        id = this.id,
        nome = this.nome,
        sobrenome = this.sobrenome,
        email = this.email,
        cpf = this.cpf,
        endereco = this.endereco.toDomainEntity(),
        login = this.login,
        nfePermissions = this.nfePermissions,
        searchRadius = this.searchRadius,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Address.toFirebaseDto(): AddressFirebaseDto {
    return AddressFirebaseDto(
        endereco = this.endereco,
        numero = this.numero,
        complemento = this.complemento,
        cep = this.cep,
        cidade = this.cidade,
        estado = this.estado,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun AddressFirebaseDto.toDomainEntity(): Address {
    return Address(
        endereco = this.endereco,
        numero = this.numero,
        complemento = this.complemento,
        cep = this.cep,
        cidade = this.cidade,
        estado = this.estado,
        latitude = this.latitude,
        longitude = this.longitude
    )
}
