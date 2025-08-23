# 📋 INSTRUÇÕES PARA EXECUTAR O BANCO DE DADOS NO MYSQL

## 🚀 **Pré-requisitos**
- MySQL Server 5.7+ ou 8.0+ instalado
- Cliente MySQL (MySQL Workbench, phpMyAdmin, ou linha de comando)
- Usuário com privilégios para criar bancos de dados

## 📝 **Passo a Passo para Execução**

### **Método 1: MySQL Workbench (Recomendado)**
1. Abra o MySQL Workbench
2. Conecte ao seu servidor MySQL
3. Abra o arquivo `database_schema.sql`
4. Clique em **Execute** (⚡) ou pressione `Ctrl+Shift+Enter`
5. Aguarde a execução completa do script

### **Método 2: Linha de Comando**
```bash
# 1. Acesse o MySQL via terminal
mysql -u root -p

# 2. Execute o script diretamente
mysql -u root -p < database_schema.sql

# OU execute dentro do MySQL
mysql> source /caminho/para/database_schema.sql;
```

### **Método 3: phpMyAdmin**
1. Acesse o phpMyAdmin
2. Vá em **SQL** no menu superior
3. Cole o conteúdo do arquivo `database_schema.sql`
4. Clique em **Executar**

## ✅ **Verificações de Compatibilidade**

### **Versões Testadas:**
- ✅ MySQL 8.0+
- ✅ MySQL 5.7+
- ✅ MariaDB 10.3+

### **Configurações Otimizadas:**
- ✅ Character set: `utf8mb4` (suporte completo a emojis)
- ✅ Collation: `utf8mb4_unicode_ci`
- ✅ Engine: `InnoDB` (transações ACID)
- ✅ Foreign keys habilitadas
- ✅ Unique constraints configuradas

## 🔧 **Configurações Específicas do MySQL**

O script já inclui as configurações necessárias:

```sql
-- Configurações automáticas incluídas no script
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET FOREIGN_KEY_CHECKS=0; -- Temporariamente desabilitado
SET UNIQUE_CHECKS=0; -- Temporariamente desabilitado
```

## 📊 **Estrutura Criada**

Após a execução, será criado:

### **Banco de Dados:**
- Nome: `pantry_manager`
- Charset: `utf8mb4`
- Collation: `utf8mb4_unicode_ci`

### **Tabelas (18 tabelas):**
1. `users` - Usuários do sistema
2. `user_addresses` - Endereços dos usuários
3. `categories` - Categorias de produtos
4. `measurement_units` - Unidades de medida
5. `products` - Catálogo de produtos
6. `nutritional_info` - Informações nutricionais
7. `pantry_items` - Itens da despensa
8. `stores` - Lojas parceiras
9. `store_addresses` - Endereços das lojas
10. `store_opening_hours` - Horários de funcionamento
11. `store_prices` - Preços nas lojas
12. `shopping_lists` - Listas de compras
13. `shopping_list_items` - Itens das listas
14. `recipes` - Receitas
15. `recipe_ingredients` - Ingredientes das receitas
16. `nfe_documents` - Notas fiscais
17. `nfe_items` - Itens das notas fiscais
18. `recipe_tags` - Tags das receitas

### **Views (3 views):**
- `v_pantry_complete` - Visão completa da despensa
- `v_stores_complete` - Visão completa das lojas
- `v_shopping_complete` - Visão completa das listas

### **Procedures (3 procedures):**
- `sp_get_items_expiring_soon` - Itens próximos ao vencimento
- `sp_generate_auto_shopping_list` - Lista automática de compras
- `sp_calculate_recipe_cost` - Custo das receitas

### **Triggers (2 triggers):**
- `tr_pantry_update_expired` - Atualiza status de vencimento
- `tr_products_update_timestamp` - Atualiza timestamp

## 🎯 **Dados Iniciais**

O script inclui dados iniciais para:

### **Categorias (15 categorias):**
- Frutas, Verduras, Carnes, Laticínios, Grãos, etc.

### **Unidades de Medida (12 unidades):**
- kg, g, l, ml, unidade, caixa, pacote, etc.

## 🐛 **Solução de Problemas**

### **Erro: "Unknown collation: 'utf8mb4_unicode_ci'"**
```sql
-- Use esta alternativa:
CREATE DATABASE pantry_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### **Erro: "Table doesn't exist"**
- Verifique se todas as tabelas foram criadas
- Execute o script novamente se necessário

### **Erro: "Foreign key constraint fails"**
```sql
-- Desabilite temporariamente as foreign keys:
SET FOREIGN_KEY_CHECKS = 0;
-- Execute seu comando
SET FOREIGN_KEY_CHECKS = 1;
```

## 📈 **Performance**

### **Índices Criados:**
- ✅ Chaves primárias em todas as tabelas
- ✅ Chaves estrangeiras com índices
- ✅ Índices em campos de busca frequente
- ✅ Índices compostos para consultas complexas
- ✅ Full-text index para busca textual

### **Otimizações:**
- ✅ Engine InnoDB para transações
- ✅ Campos otimizados por tipo de dados
- ✅ Relacionamentos bem definidos
- ✅ Views para consultas complexas

## 🔄 **Próximos Passos**

Após a execução:

1. **Teste a estrutura:**
   ```sql
   USE pantry_manager;
   SHOW TABLES;
   DESCRIBE users;
   ```

2. **Verifique os dados iniciais:**
   ```sql
   SELECT * FROM categories;
   SELECT * FROM measurement_units;
   ```

3. **Teste as procedures:**
   ```sql
   CALL sp_get_items_expiring_soon('user123', 7);
   ```

4. **Integre com a aplicação Android**
   - Configure as strings de conexão
   - Teste as operações CRUD
   - Valide a sincronização de dados

## 📞 **Suporte**

Em caso de dúvidas:
- Verifique os logs do MySQL
- Confirme as versões de compatibilidade
- Execute as verificações passo a passo

---
**Versão do Script:** 1.0  
**Data:** 23/08/2025  
**Compatibilidade:** MySQL 5.7+, MySQL 8.0+, MariaDB 10.3+
