-- ============================================
-- SCRIPT SQL PARA O PROJETO PANTRYMANAGER
-- Versão: 1.0
-- Data: 23/08/2025
-- Autor: Sistema PantryManager
-- ============================================

-- Configurações iniciais para MySQL
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Garantir que o MySQL está usando UTF8MB4
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;

-- ============================================
-- CRIAÇÃO DO BANCO DE DADOS
-- ============================================
CREATE SCHEMA IF NOT EXISTS `pantry_manager` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `pantry_manager`;

-- ============================================
-- TABELAS PRINCIPAIS
-- ============================================

-- Tabela de Usuários
CREATE TABLE `users` (
    `id` VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'ID único do usuário (Firebase UID)',
    `nome` VARCHAR(100) NOT NULL COMMENT 'Nome do usuário',
    `sobrenome` VARCHAR(100) NOT NULL COMMENT 'Sobrenome do usuário',
    `email` VARCHAR(255) NOT NULL UNIQUE COMMENT 'E-mail único do usuário',
    `cpf` VARCHAR(14) NOT NULL UNIQUE COMMENT 'CPF do usuário',
    `login` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Login único do usuário',
    `nfe_permissions` BOOLEAN DEFAULT FALSE COMMENT 'Permissão para acessar NFe',
    `search_radius` DECIMAL(10,2) DEFAULT 5.0 COMMENT 'Raio de busca em km',
    `profile_image_url` TEXT NULL COMMENT 'URL da foto de perfil',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    
    INDEX `idx_email` (`email`),
    INDEX `idx_cpf` (`cpf`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabela de usuários do sistema';

-- Tabela de Endereços dos Usuários
CREATE TABLE `user_addresses` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(255) NOT NULL COMMENT 'ID do usuário',
    `endereco` VARCHAR(255) NOT NULL COMMENT 'Logradouro',
    `numero` VARCHAR(20) NOT NULL COMMENT 'Número do endereço',
    `complemento` VARCHAR(100) NULL COMMENT 'Complemento do endereço',
    `cep` VARCHAR(9) NOT NULL COMMENT 'CEP',
    `cidade` VARCHAR(100) NOT NULL COMMENT 'Cidade',
    `estado` VARCHAR(2) NOT NULL COMMENT 'Estado (UF)',
    `latitude` DECIMAL(10,8) NULL COMMENT 'Latitude para geolocalização',
    `longitude` DECIMAL(11,8) NULL COMMENT 'Longitude para geolocalização',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_cep` (`cep`),
    INDEX `idx_location` (`latitude`, `longitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Endereços dos usuários';

-- Tabela de Categorias
CREATE TABLE `categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL COMMENT 'Nome da categoria',
    `color` VARCHAR(7) DEFAULT '#1976D2' COMMENT 'Cor da categoria (hex)',
    `icon` VARCHAR(50) DEFAULT 'category' COMMENT 'Ícone da categoria',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY `uk_category_name` (`name`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Categorias de produtos';

-- Tabela de Unidades de Medida
CREATE TABLE `measurement_units` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT 'Nome da unidade (ex: Quilograma)',
    `abbreviation` VARCHAR(10) NOT NULL COMMENT 'Abreviação (ex: kg)',
    `multiply_quantity_by_price` BOOLEAN DEFAULT FALSE COMMENT 'Se multiplica quantidade pelo preço',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY `uk_unit_name` (`name`),
    UNIQUE KEY `uk_unit_abbreviation` (`abbreviation`),
    INDEX `idx_name` (`name`),
    INDEX `idx_abbreviation` (`abbreviation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Unidades de medida';

-- Tabela de Produtos
CREATE TABLE `products` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ean` VARCHAR(20) NULL COMMENT 'Código de barras',
    `name` VARCHAR(255) NOT NULL COMMENT 'Nome do produto',
    `description` TEXT NULL COMMENT 'Descrição do produto',
    `category_id` BIGINT NOT NULL COMMENT 'ID da categoria',
    `unit_id` BIGINT NOT NULL COMMENT 'ID da unidade de medida',
    `observation` TEXT NULL COMMENT 'Observações sobre o produto',
    `image_url` TEXT NULL COMMENT 'URL da imagem do produto',
    `brand` VARCHAR(100) NULL COMMENT 'Marca do produto',
    `average_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Preço médio',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (`unit_id`) REFERENCES `measurement_units`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE KEY `uk_product_ean` (`ean`),
    INDEX `idx_name` (`name`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_unit_id` (`unit_id`),
    INDEX `idx_brand` (`brand`),
    INDEX `idx_average_price` (`average_price`),
    FULLTEXT INDEX `ft_product_search` (`name`, `description`, `brand`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Produtos do sistema';

-- Tabela de Informações Nutricionais
CREATE TABLE `nutritional_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL COMMENT 'ID do produto',
    `calories` DECIMAL(8,2) NULL COMMENT 'Calorias por porção',
    `protein` DECIMAL(8,2) NULL COMMENT 'Proteínas em gramas',
    `carbs` DECIMAL(8,2) NULL COMMENT 'Carboidratos em gramas',
    `fat` DECIMAL(8,2) NULL COMMENT 'Gorduras em gramas',
    `fiber` DECIMAL(8,2) NULL COMMENT 'Fibras em gramas',
    `sodium` DECIMAL(8,2) NULL COMMENT 'Sódio em miligramas',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY `uk_nutritional_product` (`product_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Informações nutricionais dos produtos';

-- Tabela de Itens da Despensa
CREATE TABLE `pantry_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL COMMENT 'ID do produto',
    `user_id` VARCHAR(255) NOT NULL COMMENT 'ID do usuário',
    `quantity` DECIMAL(10,3) NOT NULL COMMENT 'Quantidade do produto',
    `unit` VARCHAR(50) NOT NULL COMMENT 'Unidade de medida',
    `expiry_date` DATE NULL COMMENT 'Data de vencimento',
    `purchase_date` DATE DEFAULT (CURRENT_DATE) COMMENT 'Data de compra',
    `purchase_price` DECIMAL(10,2) NULL COMMENT 'Preço de compra',
    `store` VARCHAR(255) NULL COMMENT 'Loja onde foi comprado',
    `is_expired` BOOLEAN DEFAULT FALSE COMMENT 'Item vencido',
    `is_consumed` BOOLEAN DEFAULT FALSE COMMENT 'Item consumido',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_expiry_date` (`expiry_date`),
    INDEX `idx_purchase_date` (`purchase_date`),
    INDEX `idx_is_expired` (`is_expired`),
    INDEX `idx_is_consumed` (`is_consumed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Itens da despensa dos usuários';

-- Tabela de Lojas
CREATE TABLE `stores` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL COMMENT 'Nome da loja',
    `phone` VARCHAR(20) NULL COMMENT 'Telefone da loja',
    `email` VARCHAR(255) NULL COMMENT 'E-mail da loja',
    `website` VARCHAR(500) NULL COMMENT 'Site da loja',
    `rating` DECIMAL(3,2) NULL COMMENT 'Avaliação da loja (0-5)',
    `is_open` BOOLEAN DEFAULT TRUE COMMENT 'Loja aberta',
    `image_url` TEXT NULL COMMENT 'URL da imagem da loja',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX `idx_name` (`name`),
    INDEX `idx_rating` (`rating`),
    INDEX `idx_is_open` (`is_open`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lojas parceiras';

-- Tabela de Endereços das Lojas
CREATE TABLE `store_addresses` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL COMMENT 'ID da loja',
    `endereco` VARCHAR(255) NOT NULL COMMENT 'Logradouro',
    `numero` VARCHAR(20) NOT NULL COMMENT 'Número',
    `complemento` VARCHAR(100) NULL COMMENT 'Complemento',
    `cep` VARCHAR(9) NOT NULL COMMENT 'CEP',
    `cidade` VARCHAR(100) NOT NULL COMMENT 'Cidade',
    `estado` VARCHAR(2) NOT NULL COMMENT 'Estado',
    `latitude` DECIMAL(10,8) NULL COMMENT 'Latitude',
    `longitude` DECIMAL(11,8) NULL COMMENT 'Longitude',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`store_id`) REFERENCES `stores`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_store_id` (`store_id`),
    INDEX `idx_cep` (`cep`),
    INDEX `idx_location` (`latitude`, `longitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Endereços das lojas';

-- Tabela de Horários de Funcionamento
CREATE TABLE `store_opening_hours` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL COMMENT 'ID da loja',
    `day_of_week` TINYINT NOT NULL COMMENT 'Dia da semana (1-7, Dom-Sab)',
    `open_time` TIME NOT NULL COMMENT 'Horário de abertura',
    `close_time` TIME NOT NULL COMMENT 'Horário de fechamento',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`store_id`) REFERENCES `stores`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY `uk_store_day` (`store_id`, `day_of_week`),
    INDEX `idx_store_id` (`store_id`),
    INDEX `idx_day_of_week` (`day_of_week`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Horários de funcionamento das lojas';

-- Tabela de Preços dos Produtos nas Lojas
CREATE TABLE `store_prices` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `store_id` BIGINT NOT NULL COMMENT 'ID da loja',
    `product_id` BIGINT NOT NULL COMMENT 'ID do produto',
    `price` DECIMAL(10,2) NOT NULL COMMENT 'Preço atual',
    `unit` VARCHAR(50) NOT NULL COMMENT 'Unidade de medida do preço',
    `is_on_promotion` BOOLEAN DEFAULT FALSE COMMENT 'Em promoção',
    `promotion_price` DECIMAL(10,2) NULL COMMENT 'Preço promocional',
    `promotion_end_date` TIMESTAMP NULL COMMENT 'Data fim da promoção',
    `last_updated` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última atualização',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`store_id`) REFERENCES `stores`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY `uk_store_product` (`store_id`, `product_id`),
    INDEX `idx_store_id` (`store_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_price` (`price`),
    INDEX `idx_promotion` (`is_on_promotion`, `promotion_end_date`),
    INDEX `idx_last_updated` (`last_updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Preços dos produtos nas lojas';

-- Tabela de Listas de Compras
CREATE TABLE `shopping_lists` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(255) NOT NULL COMMENT 'ID do usuário',
    `name` VARCHAR(255) NOT NULL COMMENT 'Nome da lista',
    `is_completed` BOOLEAN DEFAULT FALSE COMMENT 'Lista finalizada',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_name` (`name`),
    INDEX `idx_is_completed` (`is_completed`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Listas de compras dos usuários';

-- Tabela de Itens das Listas de Compras
CREATE TABLE `shopping_list_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `shopping_list_id` BIGINT NOT NULL COMMENT 'ID da lista de compras',
    `product_id` BIGINT NOT NULL COMMENT 'ID do produto',
    `quantity` DECIMAL(10,3) NOT NULL COMMENT 'Quantidade desejada',
    `unit` VARCHAR(50) NOT NULL COMMENT 'Unidade de medida',
    `is_purchased` BOOLEAN DEFAULT FALSE COMMENT 'Item comprado',
    `estimated_price` DECIMAL(10,2) NULL COMMENT 'Preço estimado',
    `actual_price` DECIMAL(10,2) NULL COMMENT 'Preço real pago',
    `store` VARCHAR(255) NULL COMMENT 'Loja onde foi comprado',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`shopping_list_id`) REFERENCES `shopping_lists`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_shopping_list_id` (`shopping_list_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_is_purchased` (`is_purchased`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Itens das listas de compras';

-- ============================================
-- TABELAS PARA FUNCIONALIDADES FUTURAS
-- ============================================

-- Tabela de Receitas
CREATE TABLE `recipes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(255) NOT NULL COMMENT 'ID do usuário criador',
    `name` VARCHAR(255) NOT NULL COMMENT 'Nome da receita',
    `description` TEXT NULL COMMENT 'Descrição da receita',
    `instructions` LONGTEXT NOT NULL COMMENT 'Instruções de preparo',
    `prep_time` INT NULL COMMENT 'Tempo de preparo em minutos',
    `cook_time` INT NULL COMMENT 'Tempo de cozimento em minutos',
    `servings` INT NULL COMMENT 'Número de porções',
    `difficulty_level` ENUM('Fácil', 'Médio', 'Difícil') DEFAULT 'Fácil',
    `image_url` TEXT NULL COMMENT 'URL da imagem da receita',
    `rating` DECIMAL(3,2) NULL COMMENT 'Avaliação média (0-5)',
    `is_public` BOOLEAN DEFAULT FALSE COMMENT 'Receita pública',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_name` (`name`),
    INDEX `idx_difficulty_level` (`difficulty_level`),
    INDEX `idx_rating` (`rating`),
    INDEX `idx_is_public` (`is_public`),
    FULLTEXT INDEX `ft_recipe_search` (`name`, `description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Receitas dos usuários';

-- Tabela de Ingredientes das Receitas
CREATE TABLE `recipe_ingredients` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `recipe_id` BIGINT NOT NULL COMMENT 'ID da receita',
    `product_id` BIGINT NOT NULL COMMENT 'ID do produto/ingrediente',
    `quantity` DECIMAL(10,3) NOT NULL COMMENT 'Quantidade necessária',
    `unit` VARCHAR(50) NOT NULL COMMENT 'Unidade de medida',
    `notes` TEXT NULL COMMENT 'Observações sobre o ingrediente',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`recipe_id`) REFERENCES `recipes`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_recipe_id` (`recipe_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Ingredientes das receitas';

-- Tabela de NFe (Notas Fiscais Eletrônicas)
CREATE TABLE `nfe_documents` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(255) NOT NULL COMMENT 'ID do usuário',
    `access_key` VARCHAR(44) NOT NULL COMMENT 'Chave de acesso da NFe',
    `emission_date` DATE NOT NULL COMMENT 'Data de emissão',
    `store_cnpj` VARCHAR(18) NOT NULL COMMENT 'CNPJ da loja emissora',
    `store_name` VARCHAR(255) NOT NULL COMMENT 'Nome da loja',
    `total_value` DECIMAL(10,2) NOT NULL COMMENT 'Valor total da nota',
    `xml_content` LONGTEXT NULL COMMENT 'Conteúdo XML da NFe',
    `processed` BOOLEAN DEFAULT FALSE COMMENT 'NFe processada',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY `uk_access_key` (`access_key`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_emission_date` (`emission_date`),
    INDEX `idx_store_cnpj` (`store_cnpj`),
    INDEX `idx_processed` (`processed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Notas fiscais eletrônicas importadas';

-- Tabela de Itens da NFe
CREATE TABLE `nfe_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `nfe_id` BIGINT NOT NULL COMMENT 'ID da NFe',
    `product_id` BIGINT NULL COMMENT 'ID do produto (se identificado)',
    `item_code` VARCHAR(50) NULL COMMENT 'Código do item na nota',
    `description` VARCHAR(255) NOT NULL COMMENT 'Descrição do item',
    `quantity` DECIMAL(10,3) NOT NULL COMMENT 'Quantidade comprada',
    `unit` VARCHAR(20) NOT NULL COMMENT 'Unidade',
    `unit_price` DECIMAL(10,4) NOT NULL COMMENT 'Preço unitário',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT 'Preço total do item',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`nfe_id`) REFERENCES `nfe_documents`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX `idx_nfe_id` (`nfe_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_description` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Itens das notas fiscais';

-- ============================================
-- INSERÇÃO DE DADOS INICIAIS
-- ============================================

-- Categorias padrão
INSERT INTO `categories` (`name`, `color`, `icon`) VALUES
('Alimentos', '#FF9800', 'fastfood'),
('Bebidas', '#2196F3', 'local_drink'),
('Higiene', '#9C27B0', 'local_pharmacy'),
('Limpeza', '#4CAF50', 'cleaning_services'),
('Laticínios', '#FFEB3B', 'icecream'),
('Carnes', '#F44336', 'restaurant'),
('Frutas', '#8BC34A', 'agriculture'),
('Verduras', '#4CAF50', 'eco'),
('Padaria', '#FF5722', 'bakery_dining'),
('Congelados', '#00BCD4', 'ac_unit'),
('Outros', '#607D8B', 'category');

-- Unidades de medida padrão
INSERT INTO `measurement_units` (`name`, `abbreviation`, `multiply_quantity_by_price`) VALUES
('Quilograma', 'kg', true),
('Grama', 'g', true),
('Litro', 'l', true),
('Mililitro', 'ml', true),
('Unidade', 'un', false),
('Pacote', 'pct', false),
('Caixa', 'cx', false),
('Lata', 'lt', false),
('Garrafa', 'grf', false),
('Saco', 'sc', false),
('Metro', 'm', true),
('Centímetro', 'cm', true),
('Dúzia', 'dz', false),
('Par', 'pr', false);

-- ============================================
-- VIEWS ÚTEIS
-- ============================================

-- View para produtos com informações completas
CREATE VIEW `v_products_complete` AS
SELECT 
    p.id,
    p.ean,
    p.name,
    p.description,
    p.brand,
    p.average_price,
    p.image_url,
    p.observation,
    c.name AS category_name,
    c.color AS category_color,
    c.icon AS category_icon,
    mu.name AS unit_name,
    mu.abbreviation AS unit_abbreviation,
    mu.multiply_quantity_by_price,
    ni.calories,
    ni.protein,
    ni.carbs,
    ni.fat,
    ni.fiber,
    ni.sodium,
    p.created_at,
    p.updated_at
FROM `products` p
JOIN `categories` c ON p.category_id = c.id
JOIN `measurement_units` mu ON p.unit_id = mu.id
LEFT JOIN `nutritional_info` ni ON p.id = ni.product_id;

-- View para itens da despensa com informações do produto
CREATE VIEW `v_pantry_items_complete` AS
SELECT 
    pi.id,
    pi.quantity,
    pi.unit,
    pi.expiry_date,
    pi.purchase_date,
    pi.purchase_price,
    pi.store,
    pi.is_expired,
    pi.is_consumed,
    p.name AS product_name,
    p.brand,
    p.image_url,
    c.name AS category_name,
    c.color AS category_color,
    u.nome AS user_name,
    u.email AS user_email,
    pi.created_at,
    pi.updated_at,
    DATEDIFF(pi.expiry_date, CURDATE()) AS days_to_expire
FROM `pantry_items` pi
JOIN `products` p ON pi.product_id = p.id
JOIN `categories` c ON p.category_id = c.id
JOIN `users` u ON pi.user_id = u.id;

-- View para lojas com endereços
CREATE VIEW `v_stores_complete` AS
SELECT 
    s.id,
    s.name,
    s.phone,
    s.email,
    s.website,
    s.rating,
    s.is_open,
    s.image_url,
    sa.endereco,
    sa.numero,
    sa.complemento,
    sa.cep,
    sa.cidade,
    sa.estado,
    sa.latitude,
    sa.longitude,
    CONCAT(sa.endereco, ', ', sa.numero, 
           CASE WHEN sa.complemento IS NOT NULL THEN CONCAT(' - ', sa.complemento) ELSE '' END,
           ' - ', sa.cidade, '/', sa.estado, ' - ', sa.cep) AS full_address,
    s.created_at,
    s.updated_at
FROM `stores` s
JOIN `store_addresses` sa ON s.id = sa.store_id;

-- ============================================
-- PROCEDURES ÚTEIS
-- ============================================

DELIMITER $$

-- Procedure para calcular itens próximos ao vencimento
CREATE PROCEDURE `sp_get_items_expiring_soon`(
    IN p_user_id VARCHAR(255),
    IN p_days INT DEFAULT 7
)
BEGIN
    SELECT 
        pi.id,
        p.name AS product_name,
        pi.quantity,
        pi.unit,
        pi.expiry_date,
        DATEDIFF(pi.expiry_date, CURDATE()) AS days_to_expire,
        c.name AS category_name
    FROM `pantry_items` pi
    JOIN `products` p ON pi.product_id = p.id
    JOIN `categories` c ON p.category_id = c.id
    WHERE pi.user_id = p_user_id 
      AND pi.is_consumed = FALSE
      AND pi.expiry_date IS NOT NULL
      AND DATEDIFF(pi.expiry_date, CURDATE()) BETWEEN 0 AND p_days
    ORDER BY pi.expiry_date ASC;
END$$

-- Procedure para gerar lista de compras automática
CREATE PROCEDURE `sp_generate_auto_shopping_list`(
    IN p_user_id VARCHAR(255),
    IN p_list_name VARCHAR(255)
)
BEGIN
    DECLARE v_list_id BIGINT;
    
    -- Criar nova lista de compras
    INSERT INTO `shopping_lists` (`user_id`, `name`) 
    VALUES (p_user_id, p_list_name);
    
    SET v_list_id = LAST_INSERT_ID();
    
    -- Inserir produtos que estão acabando (quantidade baixa ou próximos ao vencimento)
    INSERT INTO `shopping_list_items` (`shopping_list_id`, `product_id`, `quantity`, `unit`, `estimated_price`)
    SELECT 
        v_list_id,
        p.id,
        1.0, -- quantidade padrão
        mu.abbreviation,
        p.average_price
    FROM `products` p
    JOIN `measurement_units` mu ON p.unit_id = mu.id
    WHERE p.id IN (
        SELECT DISTINCT pi.product_id
        FROM `pantry_items` pi
        WHERE pi.user_id = p_user_id
          AND (
            (pi.expiry_date IS NOT NULL AND DATEDIFF(pi.expiry_date, CURDATE()) <= 3) OR
            (pi.quantity <= 1)
          )
          AND pi.is_consumed = FALSE
    );
    
    SELECT v_list_id AS list_id;
END$$

DELIMITER ;

-- ============================================
-- TRIGGERS
-- ============================================

DELIMITER $$

-- Trigger para atualizar status de expirado nos itens da despensa
CREATE TRIGGER `tr_pantry_items_check_expired`
    BEFORE UPDATE ON `pantry_items`
    FOR EACH ROW
BEGIN
    IF NEW.expiry_date IS NOT NULL AND NEW.expiry_date < CURDATE() THEN
        SET NEW.is_expired = TRUE;
    END IF;
END$$

-- Trigger para atualizar preço médio do produto quando um preço de loja é alterado
CREATE TRIGGER `tr_update_average_price`
    AFTER INSERT ON `store_prices`
    FOR EACH ROW
BEGIN
    UPDATE `products` 
    SET `average_price` = (
        SELECT AVG(`price`) 
        FROM `store_prices` 
        WHERE `product_id` = NEW.product_id
    )
    WHERE `id` = NEW.product_id;
END$$

CREATE TRIGGER `tr_update_average_price_update`
    AFTER UPDATE ON `store_prices`
    FOR EACH ROW
BEGIN
    UPDATE `products` 
    SET `average_price` = (
        SELECT AVG(`price`) 
        FROM `store_prices` 
        WHERE `product_id` = NEW.product_id
    )
    WHERE `id` = NEW.product_id;
END$$

DELIMITER ;

-- ============================================
-- ÍNDICES ADICIONAIS PARA PERFORMANCE
-- ============================================

-- Índices compostos para consultas frequentes
CREATE INDEX `idx_pantry_user_product` ON `pantry_items` (`user_id`, `product_id`);
CREATE INDEX `idx_pantry_user_expired` ON `pantry_items` (`user_id`, `is_expired`, `is_consumed`);
CREATE INDEX `idx_shopping_user_completed` ON `shopping_lists` (`user_id`, `is_completed`);
CREATE INDEX `idx_store_prices_product_price` ON `store_prices` (`product_id`, `price`);

-- ============================================
-- COMENTÁRIOS FINAIS E RESTORE SETTINGS
-- ============================================

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- ============================================
-- ESTATÍSTICAS E INFORMAÇÕES
-- ============================================

/*
RESUMO DO BANCO DE DADOS PANTRYMANAGER
=====================================

TABELAS PRINCIPAIS:
- users: Usuários do sistema
- user_addresses: Endereços dos usuários
- categories: Categorias de produtos
- measurement_units: Unidades de medida
- products: Produtos do sistema
- nutritional_info: Informações nutricionais
- pantry_items: Itens da despensa
- stores: Lojas parceiras
- store_addresses: Endereços das lojas
- store_opening_hours: Horários de funcionamento
- store_prices: Preços dos produtos nas lojas
- shopping_lists: Listas de compras
- shopping_list_items: Itens das listas

TABELAS FUTURAS:
- recipes: Receitas dos usuários
- recipe_ingredients: Ingredientes das receitas
- nfe_documents: Notas fiscais eletrônicas
- nfe_items: Itens das notas fiscais

FEATURES IMPLEMENTADAS:
✅ Gestão de usuários e endereços
✅ Catálogo completo de produtos
✅ Sistema de categorias e unidades
✅ Controle de despensa com vencimentos
✅ Gestão de lojas e preços
✅ Listas de compras inteligentes
✅ Views otimizadas para consultas
✅ Procedures para automação
✅ Triggers para integridade
✅ Índices para performance
✅ Dados iniciais (categorias e unidades)

PRÓXIMAS IMPLEMENTAÇÕES:
🔄 Sistema de receitas
🔄 Integração com NFe
🔄 Sistema de avaliações
🔄 Notificações automáticas
🔄 Analytics e relatórios

VERSÃO: 1.0
DATA: 23/08/2025
CHARSET: UTF8MB4 (suporte completo a emojis)
ENGINE: InnoDB (suporte a transações e chaves estrangeiras)
*/
