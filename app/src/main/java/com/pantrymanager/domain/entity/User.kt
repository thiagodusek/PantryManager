package com.pantrymanager.domain.entity

data class User(
    val id: String = "",
    val nome: String,
    val sobrenome: String,
    val email: String,
    val cpf: String,
    val endereco: Address,
    val login: String,
    val nfePermissions: Boolean = false,
    val searchRadius: Double = 5.0, // km
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class Address(
    val endereco: String,
    val numero: String,
    val complemento: String? = null,
    val cep: String,
    val cidade: String,
    val estado: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)
