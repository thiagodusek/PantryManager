package com.pantrymanager.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para resposta da API ViaCEP
 */
data class ViaCepResponse(
    @SerializedName("cep")
    val cep: String,
    
    @SerializedName("logradouro")
    val logradouro: String,
    
    @SerializedName("complemento")
    val complemento: String,
    
    @SerializedName("bairro")
    val bairro: String,
    
    @SerializedName("localidade")
    val localidade: String,
    
    @SerializedName("uf")
    val uf: String,
    
    @SerializedName("ibge")
    val ibge: String,
    
    @SerializedName("gia")
    val gia: String,
    
    @SerializedName("ddd")
    val ddd: String,
    
    @SerializedName("siafi")
    val siafi: String,
    
    @SerializedName("erro")
    val erro: Boolean? = null
)
