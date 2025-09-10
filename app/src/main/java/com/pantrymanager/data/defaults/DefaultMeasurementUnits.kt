package com.pantrymanager.data.defaults

import com.pantrymanager.domain.entity.MeasurementUnit

/**
 * Unidades de medida padrão do sistema
 * Baseado nas unidades mais comuns em supermercados
 */
object DefaultMeasurementUnits {
    
    val defaultUnits = listOf(
        // Peso
        MeasurementUnit(1L, "Quilograma", "kg", "Unidade de massa - 1000 gramas", true),
        MeasurementUnit(2L, "Grama", "g", "Unidade básica de massa", true),
        MeasurementUnit(3L, "Miligrama", "mg", "Unidade pequena de massa", true),
        MeasurementUnit(4L, "Tonelada", "t", "Unidade grande de massa - 1000 kg", true),
        
        // Volume
        MeasurementUnit(5L, "Litro", "L", "Unidade de volume", true),
        MeasurementUnit(6L, "Mililitro", "mL", "Unidade pequena de volume", true),
        MeasurementUnit(7L, "Metro cúbico", "m³", "Unidade grande de volume", true),
        MeasurementUnit(8L, "Centímetro cúbico", "cm³", "Unidade pequena de volume", true),
        
        // Unidades
        MeasurementUnit(9L, "Unidade", "un", "Peça individual", false),
        MeasurementUnit(10L, "Par", "par", "Conjunto de duas peças", false),
        MeasurementUnit(11L, "Dúzia", "dz", "Conjunto de 12 unidades", false),
        MeasurementUnit(12L, "Cento", "cento", "Conjunto de 100 unidades", false),
        MeasurementUnit(13L, "Pacote", "pct", "Embalagem fechada", false),
        MeasurementUnit(14L, "Caixa", "cx", "Recipiente de armazenamento", false),
        MeasurementUnit(15L, "Fardo", "fardo", "Grande quantidade embalada", false),
        MeasurementUnit(16L, "Saco", "saco", "Embalagem flexível", false),
        
        // Comprimento
        MeasurementUnit(17L, "Metro", "m", "Unidade de comprimento", false),
        MeasurementUnit(18L, "Centímetro", "cm", "Unidade pequena de comprimento", false),
        MeasurementUnit(19L, "Milímetro", "mm", "Unidade muito pequena de comprimento", false),
        
        // Área
        MeasurementUnit(20L, "Metro quadrado", "m²", "Unidade de área", false),
        MeasurementUnit(21L, "Centímetro quadrado", "cm²", "Unidade pequena de área", false),
        
        // Especiais para alimentos
        MeasurementUnit(22L, "Colher de sopa", "colher sopa", "Medida culinária - aprox. 15ml", false),
        MeasurementUnit(23L, "Colher de chá", "colher chá", "Medida culinária - aprox. 5ml", false),
        MeasurementUnit(24L, "Xícara", "xícara", "Medida culinária - aprox. 240ml", false),
        MeasurementUnit(25L, "Copo", "copo", "Medida culinária - aprox. 200ml", false),
        MeasurementUnit(26L, "Pitada", "pitada", "Quantidade muito pequena", false),
        MeasurementUnit(27L, "Punhado", "punhado", "Quantidade que cabe na mão", false),
        
        // Tempo (para produtos com validade)
        MeasurementUnit(28L, "Dia", "dia", "Unidade de tempo", false),
        MeasurementUnit(29L, "Semana", "sem", "7 dias", false),
        MeasurementUnit(30L, "Mês", "mês", "Aproximadamente 30 dias", false),
        MeasurementUnit(31L, "Ano", "ano", "12 meses", false),
        
        // Energia
        MeasurementUnit(32L, "Caloria", "cal", "Unidade de energia alimentar", false),
        MeasurementUnit(33L, "Quilocaloria", "kcal", "1000 calorias", false),
        MeasurementUnit(34L, "Joule", "J", "Unidade de energia do SI", false),
        
        // Especiais para líquidos
        MeasurementUnit(35L, "Garrafa", "garrafa", "Recipiente para líquidos", false),
        MeasurementUnit(36L, "Lata", "lata", "Recipiente metálico", false),
        MeasurementUnit(37L, "Ampola", "ampola", "Pequeno recipiente de vidro", false),
        MeasurementUnit(38L, "Frasco", "frasco", "Recipiente pequeno", false),
        
        // Tecidos e materiais
        MeasurementUnit(39L, "Metro linear", "m linear", "Comprimento de material", false),
        MeasurementUnit(40L, "Rolo", "rolo", "Material enrolado", false),
        
        // Unidades adicionais comuns em supermercados
        MeasurementUnit(41L, "Bandeja", "bandeja", "Embalagem plana para alimentos", false),
        MeasurementUnit(42L, "Pote", "pote", "Recipiente pequeno com tampa", false),
        MeasurementUnit(43L, "Tubo", "tubo", "Recipiente cilíndrico", false),
        MeasurementUnit(44L, "Sachê", "sachê", "Embalagem pequena selada", false),
        MeasurementUnit(45L, "Envelope", "envelope", "Embalagem plana selada", false),
        MeasurementUnit(46L, "Blister", "blister", "Embalagem transparente moldada", false),
        MeasurementUnit(47L, "Cartela", "cartela", "Embalagem em cartão", false),
        MeasurementUnit(48L, "Bisnaga", "bisnaga", "Tubo flexível", false),
        MeasurementUnit(49L, "Spray", "spray", "Recipiente com borrifador", false),
        MeasurementUnit(50L, "Aerossol", "aerossol", "Recipiente pressurizado", false),
        MeasurementUnit(51L, "Maço", "maço", "Conjunto amarrado ou agrupado", false),
        MeasurementUnit(52L, "Molho", "molho", "Grupo pequeno de itens", false),
        MeasurementUnit(53L, "Cacho", "cacho", "Frutas agrupadas naturalmente", false),
        MeasurementUnit(54L, "Cabeça", "cabeça", "Unidade para vegetais como alface", false),
        MeasurementUnit(55L, "Pé", "pé", "Unidade para plantas", false)
    )
    
    fun getById(id: Long): MeasurementUnit? {
        return defaultUnits.find { it.id == id }
    }
    
    fun getByAbbreviation(abbreviation: String): MeasurementUnit? {
        return defaultUnits.find { it.abbreviation.equals(abbreviation, ignoreCase = true) }
    }
}
