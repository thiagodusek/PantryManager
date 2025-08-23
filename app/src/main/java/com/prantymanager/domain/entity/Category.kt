package com.prantymanager.domain.entity

data class Category(
    val id: Long = 0,
    val name: String, // Nome obrigatório
    val color: String = "#1976D2",
    val icon: String = "category"
)
