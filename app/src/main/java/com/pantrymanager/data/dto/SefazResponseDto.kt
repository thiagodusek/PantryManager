package com.pantrymanager.data.dto

import com.google.gson.annotations.SerializedName
import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.entity.FiscalReceiptItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * DTO para resposta da consulta à Sefaz
 */
data class SefazResponseDto(
    @SerializedName("status")
    val status: String = "",
    
    @SerializedName("message")
    val message: String = "",
    
    @SerializedName("data")
    val data: SefazFiscalReceiptDto? = null
)

/**
 * DTO do cupom fiscal da Sefaz
 */
data class SefazFiscalReceiptDto(
    @SerializedName("chave_acesso")
    val accessKey: String = "",
    
    @SerializedName("numero_nota")
    val receiptNumber: String = "",
    
    @SerializedName("data_emissao")
    val issueDate: String = "",
    
    @SerializedName("valor_total")
    val totalAmount: Double = 0.0,
    
    @SerializedName("estabelecimento")
    val establishment: SefazEstablishmentDto? = null,
    
    @SerializedName("itens")
    val items: List<SefazItemDto> = emptyList()
)

/**
 * DTO do estabelecimento (loja) da Sefaz
 */
data class SefazEstablishmentDto(
    @SerializedName("razao_social")
    val businessName: String = "",
    
    @SerializedName("nome_fantasia")
    val tradeName: String = "",
    
    @SerializedName("cnpj")
    val cnpj: String = "",
    
    @SerializedName("endereco")
    val address: String = ""
)

/**
 * DTO do item do cupom fiscal da Sefaz
 */
data class SefazItemDto(
    @SerializedName("codigo_produto")
    val productCode: String = "",
    
    @SerializedName("ean")
    val ean: String? = null,
    
    @SerializedName("descricao")
    val description: String = "",
    
    @SerializedName("quantidade")
    val quantity: Double = 0.0,
    
    @SerializedName("unidade")
    val unit: String = "",
    
    @SerializedName("valor_unitario")
    val unitPrice: Double = 0.0,
    
    @SerializedName("valor_total")
    val totalPrice: Double = 0.0,
    
    @SerializedName("ncm")
    val ncm: String? = null,
    
    @SerializedName("cfop")
    val cfop: String? = null
)

/**
 * Extensões para conversão para entidades de domínio
 */
fun SefazFiscalReceiptDto.toDomain(): FiscalReceipt {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val purchaseDate = try {
        LocalDateTime.parse(issueDate, dateFormatter)
    } catch (e: DateTimeParseException) {
        try {
            // Tentar formato alternativo
            val alternateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            LocalDateTime.parse(issueDate, alternateFormatter)
        } catch (e2: DateTimeParseException) {
            LocalDateTime.now()
        }
    }
    
    return FiscalReceipt(
        accessKey = accessKey,
        receiptNumber = receiptNumber,
        storeName = establishment?.tradeName ?: establishment?.businessName ?: "",
        storeCnpj = establishment?.cnpj,
        totalAmount = totalAmount,
        purchaseDate = purchaseDate,
        items = items.map { it.toDomain() }
    )
}

fun SefazItemDto.toDomain(): FiscalReceiptItem {
    return FiscalReceiptItem(
        productCode = productCode,
        ean = ean?.takeIf { it.isNotBlank() },
        name = extractProductName(description),
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        totalPrice = totalPrice,
        unit = unit,
        category = inferCategoryFromDescription(description),
        brand = inferBrandFromDescription(description)
    )
}

/**
 * Extrai o nome do produto da descrição, removendo informações extras
 */
private fun extractProductName(description: String): String {
    return description
        .split(" - ").firstOrNull() // Remove descrições após hífen
        ?.split(",").firstOrNull() // Remove descrições após vírgula
        ?.trim()
        ?: description
}

/**
 * Tenta inferir a categoria do produto baseado na descrição
 */
private fun inferCategoryFromDescription(description: String): String? {
    val lowerDesc = description.lowercase()
    
    return when {
        lowerDesc.contains("leite") || lowerDesc.contains("iogurte") || 
        lowerDesc.contains("queijo") || lowerDesc.contains("manteiga") -> "Laticínios"
        
        lowerDesc.contains("carne") || lowerDesc.contains("frango") || 
        lowerDesc.contains("peixe") || lowerDesc.contains("linguiça") -> "Carnes"
        
        lowerDesc.contains("arroz") || lowerDesc.contains("feijão") || 
        lowerDesc.contains("macarrão") || lowerDesc.contains("pão") -> "Grãos e Cereais"
        
        lowerDesc.contains("banana") || lowerDesc.contains("maçã") || 
        lowerDesc.contains("laranja") || lowerDesc.contains("fruta") -> "Frutas"
        
        lowerDesc.contains("alface") || lowerDesc.contains("tomate") || 
        lowerDesc.contains("cebola") || lowerDesc.contains("verdura") -> "Verduras e Legumes"
        
        lowerDesc.contains("refrigerante") || lowerDesc.contains("suco") || 
        lowerDesc.contains("água") || lowerDesc.contains("bebida") -> "Bebidas"
        
        lowerDesc.contains("sabonete") || lowerDesc.contains("shampoo") || 
        lowerDesc.contains("pasta") || lowerDesc.contains("higiene") -> "Higiene"
        
        lowerDesc.contains("detergente") || lowerDesc.contains("amaciante") || 
        lowerDesc.contains("desinfetante") || lowerDesc.contains("limpeza") -> "Limpeza"
        
        else -> null
    }
}

/**
 * Tenta inferir a marca do produto baseado na descrição
 */
private fun inferBrandFromDescription(description: String): String? {
    val commonBrands = listOf(
        "Nestlé", "Coca-Cola", "Pepsi", "Unilever", "Johnson", "Colgate",
        "Sadia", "Perdigão", "Seara", "Aurora", "Frimesa", "BRF",
        "Danone", "Vigor", "Parmalat", "Itambé", "Piracanjuba",
        "Bauducco", "Marilan", "Nabisco", "Piraquê", "Adria"
    )
    
    val upperDesc = description.uppercase()
    return commonBrands.find { brand ->
        upperDesc.contains(brand.uppercase())
    }
}
