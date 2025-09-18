package com.pantrymanager.data.defaults

import com.pantrymanager.domain.entity.Category

/**
 * Categorias padrão do sistema baseadas em supermercados
 * Organizadas hierarquicamente com categorias principais e subcategorias
 */
object DefaultCategories {
    
    val defaultCategories = listOf(
        // ALIMENTAÇÃO E BEBIDAS
        // Categoria principal: Alimentos básicos
        Category(1L, "Cereais e Grãos", "Arroz, feijão, lentilha, aveia, quinoa", "#8BC34A", "grain"),
        Category(2L, "Farinhas e Massas", "Farinha de trigo, massas, macarrão", "#FF9800", "pasta"),
        Category(3L, "Óleos e Temperos", "Óleo, azeite, sal, pimenta, especiarias", "#FFC107", "spice"),
        Category(4L, "Açúcar e Adoçantes", "Açúcar cristal, demerara, mel, adoçante", "#FFEB3B", "sugar"),
        
        // Proteínas
        Category(5L, "Carnes Bovinas", "Carne bovina fresca e processada", "#F44336", "beef"),
        Category(6L, "Carnes Suínas", "Carne suína, linguiça, bacon", "#E91E63", "pork"),
        Category(7L, "Aves", "Frango, peru, codorna", "#9C27B0", "chicken"),
        Category(8L, "Peixes e Frutos do Mar", "Peixes frescos, camarão, lula", "#3F51B5", "fish"),
        Category(9L, "Ovos", "Ovos de galinha, codorna", "#FFEB3B", "egg"),
        
        // Laticínios
        Category(10L, "Leites", "Leite integral, desnatado, condensado", "#FFFFFF", "milk"),
        Category(11L, "Queijos", "Queijo mussarela, prato, parmesão", "#FFF9C4", "cheese"),
        Category(12L, "Iogurtes", "Iogurte natural, com frutas", "#F8BBD9", "yogurt"),
        Category(13L, "Manteiga e Margarina", "Manteiga, margarina, creme de leite", "#FFF59D", "butter"),
        
        // Frutas, Legumes e Verduras
        Category(14L, "Frutas Frescas", "Maçã, banana, laranja, uva", "#4CAF50", "fruit"),
        Category(15L, "Verduras Folhosas", "Alface, rúcula, espinafre, couve", "#8BC34A", "leafy"),
        Category(16L, "Legumes", "Cenoura, batata, cebola, tomate", "#FF5722", "vegetable"),
        Category(17L, "Temperos Frescos", "Salsa, cebolinha, coentro, manjericão", "#4CAF50", "herbs"),
        
        // Bebidas
        Category(18L, "Águas", "Água mineral, com gás, saborizada", "#2196F3", "water"),
        Category(19L, "Refrigerantes", "Coca-cola, guaraná, soda", "#FF9800", "soda"),
        Category(20L, "Sucos", "Suco natural, concentrado, em pó", "#FF5722", "juice"),
        Category(21L, "Bebidas Alcoólicas", "Cerveja, vinho, destilados", "#795548", "alcohol"),
        Category(22L, "Chás e Cafés", "Café em pó, chá preto, verde", "#5D4037", "coffee"),
        Category(23L, "Bebidas Lácteas", "Achocolatado, vitamina, milk-shake", "#F8BBD9", "milkshake"),
        
        // Produtos Processados
        Category(24L, "Enlatados", "Conservas, sardinha, milho, ervilha", "#607D8B", "canned"),
        Category(25L, "Embutidos", "Presunto, mortadela, salame", "#E91E63", "deli"),
        Category(26L, "Congelados", "Hambúrguer, nuggets, sorvete", "#03A9F4", "frozen"),
        Category(27L, "Biscoitos e Crackers", "Biscoito doce, salgado, bolacha", "#FFEB3B", "cookie"),
        Category(28L, "Doces e Chocolates", "Chocolate, bala, chiclete", "#8E24AA", "candy"),
        Category(29L, "Snacks", "Chips, amendoim, pipoca", "#FF9800", "snack"),
        
        // Panificação
        Category(30L, "Pães", "Pão de forma, francês, integral", "#D7CCC8", "bread"),
        Category(31L, "Bolos e Tortas", "Bolo pronto, torta doce", "#F48FB1", "cake"),
        Category(32L, "Massas Prontas", "Pizza congelada, lasanha", "#FF8A65", "ready_meal"),
        
        // HIGIENE PESSOAL
        Category(33L, "Shampoos e Condicionadores", "Cuidados com o cabelo", "#E1BEE7", "shampoo"),
        Category(34L, "Sabonetes", "Sabonete em barra, líquido", "#81C784", "soap"),
        Category(35L, "Cremes e Loções", "Hidratante, protetor solar", "#FFCC02", "lotion"),
        Category(36L, "Desodorantes", "Desodorante roll-on, aerosol", "#B39DDB", "deodorant"),
        Category(37L, "Produtos Bucais", "Pasta de dente, enxaguante", "#4FC3F7", "dental"),
        Category(38L, "Produtos Femininos", "Absorventes, tampões", "#F8BBD9", "feminine"),
        Category(39L, "Produtos Masculinos", "Espuma de barbear, loção", "#90CAF9", "masculine"),
        Category(40L, "Produtos Infantis", "Fralda, pomada, shampoo infantil", "#FFE0B2", "baby"),
        
        // LIMPEZA E CASA
        Category(41L, "Detergentes", "Detergente líquido, em pó", "#4CAF50", "detergent"),
        Category(42L, "Desinfetantes", "Álcool, água sanitária, desinfetante", "#2196F3", "disinfectant"),
        Category(43L, "Produtos de Limpeza", "Multiuso, limpa vidros", "#FF9800", "cleaner"),
        Category(44L, "Papel Higiênico e Toalhas", "Papel higiênico, papel toalha", "#FFFFFF", "tissue"),
        Category(45L, "Esponjas e Panos", "Esponja de limpeza, pano de chão", "#FFEB3B", "sponge"),
        Category(46L, "Sacos de Lixo", "Sacos plásticos para lixo", "#424242", "trash_bag"),
        
        // CASA E DECORAÇÃO
        Category(47L, "Utensílios de Cozinha", "Panelas, talheres, copos", "#607D8B", "kitchenware"),
        Category(48L, "Produtos Descartáveis", "Pratos, copos, guardanapos descartáveis", "#FFFDE7", "disposable"),
        Category(49L, "Velas e Incensos", "Velas perfumadas, incenso", "#FF7043", "candle"),
        Category(50L, "Pilhas e Baterias", "Pilhas AA, AAA, baterias", "#37474F", "battery"),
        
        // PETSHOP
        Category(51L, "Ração para Cães", "Ração seca, úmida para cães", "#8D6E63", "dog_food"),
        Category(52L, "Ração para Gatos", "Ração seca, úmida para gatos", "#FF8A65", "cat_food"),
        Category(53L, "Produtos de Higiene Pet", "Shampoo, escova para pets", "#A5D6A7", "pet_hygiene"),
        Category(54L, "Acessórios Pet", "Coleira, brinquedos, cama", "#FFAB91", "pet_accessories"),
        
        // FARMÁCIA E SAÚDE
        Category(55L, "Medicamentos", "Remédios de venda livre", "#F44336", "medicine"),
        Category(56L, "Vitaminas e Suplementos", "Vitamina C, complexo B", "#4CAF50", "vitamin"),
        Category(57L, "Primeiros Socorros", "Band-aid, gaze, álcool", "#FF9800", "first_aid"),
        Category(58L, "Produtos Naturais", "Chás medicinais, ervas", "#8BC34A", "natural"),
        
        // BEBÊ
        Category(59L, "Fraldas", "Fraldas descartáveis", "#E1F5FE", "diaper"),
        Category(60L, "Alimentação Infantil", "Papinha, leite em pó", "#FFF3E0", "baby_food"),
        Category(61L, "Higiene do Bebê", "Shampoo, sabonete infantil", "#F3E5F5", "baby_hygiene"),
        Category(62L, "Acessórios do Bebê", "Mamadeira, chupeta", "#E8F5E8", "baby_accessories")
    )
    
    fun getById(id: Long): Category? {
        return defaultCategories.find { it.id == id }
    }
    
    fun findByName(name: String): Category? {
        return defaultCategories.find { 
            it.name.equals(name, ignoreCase = true) ||
            name.contains(it.name, ignoreCase = true) ||
            it.name.contains(name, ignoreCase = true)
        }
    }
    
    fun getCategoriesByParent(parentId: Long?): List<Category> {
        return defaultCategories.filter { it.parentCategoryId == parentId }
    }
}
