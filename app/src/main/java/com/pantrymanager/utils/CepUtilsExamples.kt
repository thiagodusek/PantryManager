package com.pantrymanager.utils

/**
 * Exemplos de uso do CepUtils para demonstrar a correção
 */
object CepUtilsExamples {
    
    fun demonstrateUsage() {
        // Exemplos de entrada e saída esperada
        val testCases = listOf(
            "" to "",                    // Entrada vazia
            "1" to "1",                 // 1 dígito
            "12345" to "12345",         // 5 dígitos (sem traço ainda)
            "123456" to "12345-6",      // 6 dígitos (começa o traço)
            "1234567" to "12345-67",    // 7 dígitos
            "12345678" to "12345-678",  // 8 dígitos (completo)
            "123456789" to "12345-678", // 9 dígitos (corta no 8°)
            "12345-678" to "12345-678", // Já formatado
            "12345-6789" to "12345-678" // Já formatado com dígito extra
        )
        
        println("=== Teste da função applyFluidCepMask ===")
        testCases.forEach { (input, expected) ->
            val result = CepUtils.applyFluidCepMask(input)
            val status = if (result == expected) "✅" else "❌"
            println("$status Input: '$input' -> Output: '$result' (Expected: '$expected')")
        }
        
        println("\n=== Teste da função cleanCep ===")
        val cleanCases = listOf(
            "12345678" to "12345678",
            "12345-678" to "12345678",
            "12.345-678" to "12345678",
            "12 345 678" to "12345678"
        )
        
        cleanCases.forEach { (input, expected) ->
            val result = CepUtils.cleanCep(input)
            val status = if (result == expected) "✅" else "❌"
            println("$status Input: '$input' -> Clean: '$result' (Expected: '$expected')")
        }
        
        println("\n=== Teste da função getVisualCepProgress ===")
        val progressCases = listOf(
            "" to "Digite o CEP (ex: 01234567)",
            "1" to "Digite mais 4 números",
            "12" to "Digite mais 3 números", 
            "123" to "Digite mais 2 números",
            "1234" to "Digite mais 1 números",
            "12345" to "Continue digitando (12345)",
            "123456" to "Digite mais 2 números",
            "1234567" to "Digite mais 1 números",
            "12345678" to "12345-678"
        )
        
        progressCases.forEach { (input, expected) ->
            val result = CepUtils.getVisualCepProgress(input)
            val status = if (result == expected) "✅" else "❌"
            println("$status Input: '$input' -> Progress: '$result' (Expected: '$expected')")
        }
        
        println("\n=== Teste da validação ===")
        val validationCases = listOf(
            "12345678" to true,
            "12345-678" to true,
            "1234567" to false,
            "123456789" to false,
            "abcd5678" to false
        )
        
        validationCases.forEach { (input, expected) ->
            val result = CepUtils.isValidCep(input)
            val status = if (result == expected) "✅" else "❌"
            println("$status Input: '$input' -> Valid: $result (Expected: $expected)")
        }
    }
}
