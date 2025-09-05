package com.pantrymanager.utils

/**
 * Utilitários para validação e formatação de CEP brasileiro
 */
object CepUtils {
    
    /**
     * Formatar CEP no padrão XXXXX-XXX
     */
    fun formatCep(cep: String): String {
        val cleanCep = cep.replace(Regex("[^0-9]"), "")
        return when {
            cleanCep.length == 8 -> "${cleanCep.substring(0, 5)}-${cleanCep.substring(5)}"
            cleanCep.length > 5 -> "${cleanCep.substring(0, 5)}-${cleanCep.substring(5, minOf(cleanCep.length, 8))}"
            else -> cleanCep
        }
    }
    
    /**
     * Remove formatação do CEP, mantendo apenas dígitos
     */
    fun cleanCep(cep: String): String {
        return cep.replace(Regex("[^0-9]"), "")
    }
    
    /**
     * Valida se o CEP tem o formato correto (8 dígitos)
     */
    fun isValidCep(cep: String): Boolean {
        val cleanCep = cleanCep(cep)
        return cleanCep.length == 8 && cleanCep.all { it.isDigit() }
    }
    
    /**
     * Máscara de CEP para input field
     */
    fun applyCepMask(input: String): String {
        val digits = input.filter { it.isDigit() }.take(8)
        return when {
            digits.length <= 5 -> digits
            digits.length <= 8 -> "${digits.take(5)}-${digits.drop(5)}"
            else -> "${digits.take(5)}-${digits.drop(5).take(3)}"
        }
    }
    
    /**
     * Aplica máscara de CEP permitindo digitação fluida
     */
    fun applyFluidCepMask(input: String): String {
        // Remove tudo que não é dígito
        val digitsOnly = input.filter { it.isDigit() }
        
        return when (digitsOnly.length) {
            0 -> ""
            in 1..5 -> digitsOnly
            in 6..8 -> "${digitsOnly.substring(0, 5)}-${digitsOnly.substring(5)}"
            else -> "${digitsOnly.substring(0, 5)}-${digitsOnly.substring(5, 8)}"
        }
    }
    
    /**
     * Retorna uma representação visual amigável do CEP durante a digitação
     */
    fun getVisualCepProgress(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> "Digite o CEP (ex: 01234567)"
            digits.length < 5 -> "Digite mais ${5 - digits.length} números"
            digits.length == 5 -> "Continue digitando (${digits})"
            digits.length < 8 -> "Digite mais ${8 - digits.length} números"
            digits.length == 8 -> formatCep(digits)
            else -> formatCep(digits.take(8))
        }
    }
}
