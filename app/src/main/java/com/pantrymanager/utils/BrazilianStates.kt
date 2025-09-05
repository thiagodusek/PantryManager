package com.pantrymanager.utils

/**
 * Utilitários para estados brasileiros
 */
object BrazilianStates {
    
    /**
     * Lista de estados brasileiros com sigla e nome
     */
    val states = listOf(
        "AC" to "Acre",
        "AL" to "Alagoas",
        "AP" to "Amapá",
        "AM" to "Amazonas",
        "BA" to "Bahia",
        "CE" to "Ceará",
        "DF" to "Distrito Federal",
        "ES" to "Espírito Santo",
        "GO" to "Goiás",
        "MA" to "Maranhão",
        "MT" to "Mato Grosso",
        "MS" to "Mato Grosso do Sul",
        "MG" to "Minas Gerais",
        "PA" to "Pará",
        "PB" to "Paraíba",
        "PR" to "Paraná",
        "PE" to "Pernambuco",
        "PI" to "Piauí",
        "RJ" to "Rio de Janeiro",
        "RN" to "Rio Grande do Norte",
        "RS" to "Rio Grande do Sul",
        "RO" to "Rondônia",
        "RR" to "Roraima",
        "SC" to "Santa Catarina",
        "SP" to "São Paulo",
        "SE" to "Sergipe",
        "TO" to "Tocantins"
    )
    
    /**
     * Obtém o nome do estado pela sigla
     */
    fun getStateNameByCode(code: String): String? {
        return states.find { it.first == code.uppercase() }?.second
    }
    
    /**
     * Obtém a sigla do estado pelo nome
     */
    fun getStateCodeByName(name: String): String? {
        return states.find { it.second.equals(name, ignoreCase = true) }?.first
    }
}
