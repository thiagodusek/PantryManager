-- ============================================
-- VERIFICAÇÃO SIMPLES - SINTAXE SQL MYSQL
-- Versão: 1.0
-- ============================================

-- Testar se o banco pode ser criado
CREATE SCHEMA IF NOT EXISTS `pantry_manager_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `pantry_manager_test`;

-- Testar criação de tabela simples
CREATE TABLE `test_table` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Testar inserção de dados
INSERT INTO `test_table` (`name`) VALUES ('Teste MySQL');

-- Testar consulta
SELECT * FROM `test_table`;

-- Testar procedure simples
DELIMITER $$
CREATE PROCEDURE `test_procedure`()
BEGIN
    SELECT 'MySQL está funcionando corretamente!' AS message;
END$$
DELIMITER ;

-- Executar procedure
CALL `test_procedure`();

-- Limpeza (remover banco de teste)
DROP SCHEMA `pantry_manager_test`;

-- Mensagem de sucesso
SELECT 'Sintaxe SQL válida para MySQL!' AS status;
