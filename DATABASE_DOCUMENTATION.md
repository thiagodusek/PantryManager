# 🗄️ Documentação do Banco de Dados - PantryManager

## 📋 Visão Geral

O banco de dados do **PantryManager** foi projetado seguindo as melhores práticas de modelagem relacional, com foco em:

- **Integridade Referencial**: Uso extensivo de chaves estrangeiras
- **Performance**: Índices otimizados para consultas frequentes
- **Escalabilidade**: Estrutura preparada para crescimento
- **Manutenibilidade**: Código bem documentado e organizado
- **Segurança**: Validações e restrições adequadas

## 🏗️ Arquitetura do Banco

### **Engine**: InnoDB
- Suporte completo a transações ACID
- Chaves estrangeiras e integridade referencial
- Row-level locking para melhor concorrência

### **Charset**: UTF8MB4
- Suporte completo a caracteres Unicode
- Compatibilidade com emojis
- Máxima compatibilidade internacional

## 📊 Estrutura das Tabelas

### 👥 **Módulo de Usuários**

#### `users`
Tabela principal de usuários do sistema.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | VARCHAR(255) | ID único (Firebase UID) |
| nome | VARCHAR(100) | Nome do usuário |
| sobrenome | VARCHAR(100) | Sobrenome do usuário |
| email | VARCHAR(255) | E-mail único |
| cpf | VARCHAR(14) | CPF único |
| login | VARCHAR(100) | Login único |
| nfe_permissions | BOOLEAN | Permissão para NFe |
| search_radius | DECIMAL(10,2) | Raio de busca em km |
| profile_image_url | TEXT | URL da foto de perfil |

#### `user_addresses`
Endereços dos usuários para geolocalização.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| user_id | VARCHAR(255) | FK para users |
| endereco | VARCHAR(255) | Logradouro |
| numero | VARCHAR(20) | Número |
| complemento | VARCHAR(100) | Complemento |
| cep | VARCHAR(9) | CEP |
| cidade | VARCHAR(100) | Cidade |
| estado | VARCHAR(2) | UF |
| latitude | DECIMAL(10,8) | Coordenada GPS |
| longitude | DECIMAL(11,8) | Coordenada GPS |

### 🏷️ **Módulo de Produtos**

#### `categories`
Categorias para organização de produtos.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| name | VARCHAR(100) | Nome da categoria |
| color | VARCHAR(7) | Cor em hex |
| icon | VARCHAR(50) | Nome do ícone |

#### `measurement_units`
Unidades de medida para produtos.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| name | VARCHAR(50) | Nome completo |
| abbreviation | VARCHAR(10) | Abreviação |
| multiply_quantity_by_price | BOOLEAN | Se multiplica qtd × preço |

#### `products`
Catálogo principal de produtos.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| ean | VARCHAR(20) | Código de barras |
| name | VARCHAR(255) | Nome do produto |
| description | TEXT | Descrição detalhada |
| category_id | BIGINT | FK para categories |
| unit_id | BIGINT | FK para measurement_units |
| brand | VARCHAR(100) | Marca |
| average_price | DECIMAL(10,2) | Preço médio |
| image_url | TEXT | URL da imagem |

#### `nutritional_info`
Informações nutricionais dos produtos.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| product_id | BIGINT | FK para products |
| calories | DECIMAL(8,2) | Calorias por porção |
| protein | DECIMAL(8,2) | Proteínas (g) |
| carbs | DECIMAL(8,2) | Carboidratos (g) |
| fat | DECIMAL(8,2) | Gorduras (g) |
| fiber | DECIMAL(8,2) | Fibras (g) |
| sodium | DECIMAL(8,2) | Sódio (mg) |

### 🏠 **Módulo de Despensa**

#### `pantry_items`
Itens armazenados na despensa dos usuários.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| product_id | BIGINT | FK para products |
| user_id | VARCHAR(255) | FK para users |
| quantity | DECIMAL(10,3) | Quantidade |
| unit | VARCHAR(50) | Unidade |
| expiry_date | DATE | Data de vencimento |
| purchase_date | DATE | Data de compra |
| purchase_price | DECIMAL(10,2) | Preço pago |
| store | VARCHAR(255) | Loja de compra |
| is_expired | BOOLEAN | Status de vencimento |
| is_consumed | BOOLEAN | Status de consumo |

### 🏪 **Módulo de Lojas**

#### `stores`
Cadastro de lojas parceiras.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| name | VARCHAR(255) | Nome da loja |
| phone | VARCHAR(20) | Telefone |
| email | VARCHAR(255) | E-mail |
| website | VARCHAR(500) | Site |
| rating | DECIMAL(3,2) | Avaliação (0-5) |
| is_open | BOOLEAN | Status de funcionamento |

#### `store_addresses`
Endereços das lojas.

#### `store_opening_hours`
Horários de funcionamento por dia da semana.

#### `store_prices`
Preços dos produtos em cada loja.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| store_id | BIGINT | FK para stores |
| product_id | BIGINT | FK para products |
| price | DECIMAL(10,2) | Preço atual |
| is_on_promotion | BOOLEAN | Em promoção |
| promotion_price | DECIMAL(10,2) | Preço promocional |
| promotion_end_date | TIMESTAMP | Fim da promoção |

### 🛒 **Módulo de Compras**

#### `shopping_lists`
Listas de compras dos usuários.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| user_id | VARCHAR(255) | FK para users |
| name | VARCHAR(255) | Nome da lista |
| is_completed | BOOLEAN | Status de conclusão |

#### `shopping_list_items`
Itens das listas de compras.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Chave primária |
| shopping_list_id | BIGINT | FK para shopping_lists |
| product_id | BIGINT | FK para products |
| quantity | DECIMAL(10,3) | Quantidade desejada |
| is_purchased | BOOLEAN | Status de compra |
| estimated_price | DECIMAL(10,2) | Preço estimado |
| actual_price | DECIMAL(10,2) | Preço real |

### 👨‍🍳 **Módulo de Receitas** (Futuro)

#### `recipes`
Receitas dos usuários.

#### `recipe_ingredients`
Ingredientes necessários para cada receita.

### 📄 **Módulo de NFe** (Futuro)

#### `nfe_documents`
Notas fiscais eletrônicas importadas.

#### `nfe_items`
Itens das notas fiscais.

## 🔍 Views Otimizadas

### `v_products_complete`
Produtos com informações completas de categoria, unidade e nutrição.

### `v_pantry_items_complete`
Itens da despensa com informações do produto e dias para vencer.

### `v_stores_complete`
Lojas com endereços completos formatados.

## ⚙️ Procedures Utilitárias

### `sp_get_items_expiring_soon`
```sql
CALL sp_get_items_expiring_soon('user_id', 7);
```
Retorna itens que vencem nos próximos N dias.

### `sp_generate_auto_shopping_list`
```sql
CALL sp_generate_auto_shopping_list('user_id', 'Lista Automática');
```
Gera lista de compras automaticamente baseada em itens com baixo estoque.

## 🚀 Triggers Automáticos

### `tr_pantry_items_check_expired`
Atualiza automaticamente o status `is_expired` quando a data de vencimento passa.

### `tr_update_average_price`
Recalcula o preço médio dos produtos quando preços de loja são atualizados.

## 📈 Índices de Performance

### Índices Simples
- Por campos de busca frequente (nome, email, data)
- Por chaves estrangeiras
- Por campos de filtro (status, tipo)

### Índices Compostos
- `idx_pantry_user_product`: (user_id, product_id)
- `idx_pantry_user_expired`: (user_id, is_expired, is_consumed)
- `idx_shopping_user_completed`: (user_id, is_completed)

### Índices Full-Text
- `ft_product_search`: Busca em nome, descrição e marca
- `ft_recipe_search`: Busca em receitas (futuro)

## 🛠️ Dados Iniciais

### Categorias Pré-definidas
- Alimentos, Bebidas, Higiene, Limpeza
- Laticínios, Carnes, Frutas, Verduras
- Padaria, Congelados, Outros

### Unidades de Medida Padrão
- kg, g, l, ml (com multiplicação por preço)
- un, pct, cx, lt, grf, sc (sem multiplicação)
- m, cm, dz, pr

## 🔒 Recursos de Segurança

### Integridade Referencial
- Todas as chaves estrangeiras configuradas
- Ações CASCADE e RESTRICT apropriadas
- Validações de domínio

### Constraints Únicos
- E-mail único por usuário
- CPF único por usuário  
- Chave de acesso NFe única
- Combinações únicas onde necessário

## 📊 Estimativa de Volumes

### Usuários: ~1K-10K
- Média de 1 endereço por usuário
- ~50-500 itens na despensa por usuário

### Produtos: ~50K-100K
- ~15 categorias principais
- ~20 unidades de medida
- Informação nutricional para ~30% dos produtos

### Lojas: ~1K-5K
- Média de 1-3 endereços por loja
- ~7 horários de funcionamento por loja
- ~10K-50K preços de produtos

### Transações: ~100K-1M/mês
- Atualizações de despensa
- Criação de listas de compras
- Sincronização de preços

## 🚀 Roadmap de Melhorias

### Fase 1 (MVP) ✅
- [x] Estrutura básica de usuários
- [x] Catálogo de produtos
- [x] Gestão de despensa
- [x] Listas de compras
- [x] Sistema de lojas e preços

### Fase 2 (Expansão) 🔄
- [ ] Sistema de receitas
- [ ] Importação de NFe
- [ ] Notificações automáticas
- [ ] Analytics avançado

### Fase 3 (Otimização) 📋
- [ ] Particionamento de tabelas grandes
- [ ] Réplicas de leitura
- [ ] Cache de consultas frequentes
- [ ] Arquivamento de dados antigos

## 📝 Convenções de Nomenclatura

### Tabelas
- Nomes no plural em inglês
- Snake_case para múltiplas palavras

### Campos
- Nomes descritivos em inglês
- Snake_case para múltiplas palavras
- Sufixos padronizados (_id, _url, _date, _at)

### Índices
- Prefixo `idx_` para índices simples
- Prefixo `ft_` para full-text
- Prefixo `uk_` para unique keys

### Procedures/Functions
- Prefixo `sp_` para stored procedures
- Prefixo `fn_` para functions
- Snake_case para nomes descritivos

## 🔧 Comandos de Manutenção

### Backup Completo
```bash
mysqldump -u root -p --single-transaction pantry_manager > backup_$(date +%Y%m%d).sql
```

### Backup Estrutura
```bash
mysqldump -u root -p --no-data pantry_manager > schema_only.sql
```

### Análise de Performance
```sql
SHOW PROCESSLIST;
EXPLAIN SELECT * FROM v_products_complete WHERE name LIKE '%produto%';
```

### Estatísticas de Tabelas
```sql
SELECT 
    table_name,
    table_rows,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.tables 
WHERE table_schema = 'pantry_manager'
ORDER BY (data_length + index_length) DESC;
```

---

**Versão**: 1.0  
**Última Atualização**: 23/08/2025  
**Responsável**: Equipe PantryManager
