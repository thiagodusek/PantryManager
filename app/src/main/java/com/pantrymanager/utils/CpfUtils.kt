package com.pantrymanager.utils

/**
 * Utilitários para validação e formatação de CPF brasileiro
 */
object CpfUtils {
    
    /**
     * Formatar CPF no padrão XXX.XXX.XXX-XX
     */
    fun formatCpf(cpf: String): String {
        val cleanCpf = cpf.replace(Regex("[^0-9]"), "")
        return when {
            cleanCpf.length == 11 -> "${cleanCpf.substring(0, 3)}.${cleanCpf.substring(3, 6)}.${cleanCpf.substring(6, 9)}-${cleanCpf.substring(9)}"
            cleanCpf.length > 3 && cleanCpf.length <= 6 -> "${cleanCpf.substring(0, 3)}.${cleanCpf.substring(3)}"
            cleanCpf.length > 6 && cleanCpf.length <= 9 -> "${cleanCpf.substring(0, 3)}.${cleanCpf.substring(3, 6)}.${cleanCpf.substring(6)}"
            cleanCpf.length > 9 -> "${cleanCpf.substring(0, 3)}.${cleanCpf.substring(3, 6)}.${cleanCpf.substring(6, 9)}-${cleanCpf.substring(9, minOf(cleanCpf.length, 11))}"
            else -> cleanCpf
        }
    }
    
    /**
     * Remove formatação do CPF, mantendo apenas dígitos
     */
    fun cleanCpf(cpf: String): String {
        return cpf.replace(Regex("[^0-9]"), "")
    }
    
    /**
     * Valida se o CPF tem o formato correto (11 dígitos) e dígito verificador válido
     */
    fun isValidCpf(cpf: String): Boolean {
        val cleanCpf = cleanCpf(cpf)
        
        // Verifica se tem 11 dígitos
        if (cleanCpf.length != 11) return false
        
        // Verifica se todos os dígitos são iguais (casos inválidos como 111.111.111-11)
        if (cleanCpf.all { it == cleanCpf[0] }) return false
        
        // Validação dos dígitos verificadores
        return isValidCpfDigits(cleanCpf)
    }
    
    /**
     * Valida os dígitos verificadores do CPF
     */
    private fun isValidCpfDigits(cpf: String): Boolean {
        // Primeiro dígito verificador
        var sum = 0
        for (i in 0..8) {
            sum += (cpf[i].toString().toInt()) * (10 - i)
        }
        var firstDigit = 11 - (sum % 11)
        if (firstDigit >= 10) firstDigit = 0
        
        if (firstDigit != cpf[9].toString().toInt()) return false
        
        // Segundo dígito verificador
        sum = 0
        for (i in 0..9) {
            sum += (cpf[i].toString().toInt()) * (11 - i)
        }
        var secondDigit = 11 - (sum % 11)
        if (secondDigit >= 10) secondDigit = 0
        
        return secondDigit == cpf[10].toString().toInt()
    }
    
    /**
     * Aplica máscara de CPF permitindo digitação fluida
     */
    fun applyFluidCpfMask(input: String): String {
        val digits = input.filter { it.isDigit() }.take(11)
        
        return when (digits.length) {
            0 -> ""
            in 1..3 -> digits
            in 4..6 -> "${digits.take(3)}.${digits.drop(3)}"
            in 7..9 -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6)}"
            in 10..11 -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6).take(3)}-${digits.drop(9)}"
            else -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6).take(3)}-${digits.drop(9).take(2)}"
        }
    }
    
    /**
     * Retorna uma representação visual amigável do CPF durante a digitação
     */
    fun getVisualCpfProgress(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> "Digite o CPF (ex: 12345678901)"
            digits.length < 11 -> "Digite mais ${11 - digits.length} números"
            digits.length == 11 -> if (isValidCpf(digits)) formatCpf(digits) else "CPF inválido"
            else -> formatCpf(digits.take(11))
        }
    }
}
