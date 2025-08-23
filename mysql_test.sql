-- ============================================
-- SCRIPT DE TESTE E VALIDAÇÃO - MYSQL
-- Versão: 1.0
-- Data: 23/08/2025
-- ============================================

-- TESTE DE CONECTIVIDADE E CONFIGURAÇÕES
SELECT 'Testando conectividade MySQL...' AS status;

-- Verificar versão do MySQL
SELECT VERSION() AS mysql_version;

-- Verificar charset padrão
SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';

-- Verificar se o banco foi criado
SHOW DATABASES LIKE 'pantry_manager';

-- Usar o banco de dados
USE pantry_manager;

-- Verificar se todas as tabelas foram criadas
SELECT 'Verificando tabelas criadas...' AS status;
SHOW TABLES;

-- Contar registros em cada tabela
SELECT 
    TABLE_NAME,
    TABLE_ROWS as 'Aproximate Rows',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) as 'Size MB'
FROM information_schema.TABLES 
WHERE table_schema = 'pantry_manager'
ORDER BY TABLE_NAME;

-- Testar dados iniciais
SELECT 'Testando dados iniciais...' AS status;

-- Verificar categorias inseridas
SELECT COUNT(*) as total_categories FROM categories;
SELECT name, color, icon FROM categories LIMIT 5;

-- Verificar unidades de medida
SELECT COUNT(*) as total_units FROM measurement_units;
SELECT name, abbreviation, multiply_quantity_by_price FROM measurement_units LIMIT 5;

-- Testar views criadas
SELECT 'Testando views...' AS status;
SHOW FULL TABLES WHERE Table_type = 'VIEW';

-- Testar procedures criadas
SELECT 'Testando procedures...' AS status;
SHOW PROCEDURE STATUS WHERE Db = 'pantry_manager';

-- Testar triggers
SELECT 'Testando triggers...' AS status;
SHOW TRIGGERS FROM pantry_manager;

-- Testar chaves estrangeiras
SELECT 'Testando foreign keys...' AS status;
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'pantry_manager'
ORDER BY TABLE_NAME;

-- Testar índices criados
SELECT 'Testando índices...' AS status;
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    NON_UNIQUE,
    INDEX_TYPE
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'pantry_manager'
ORDER BY TABLE_NAME, INDEX_NAME;

SELECT 'Teste concluído com sucesso!' AS final_status;
