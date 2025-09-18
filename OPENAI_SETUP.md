# 🔧 Configuração da Integração OpenAI/ChatGPT

## 📋 **Pré-requisitos**

1. **Conta OpenAI**: Crie uma conta em [platform.openai.com](https://platform.openai.com)
2. **Chave API**: Gere uma chave API em [platform.openai.com/api-keys](https://platform.openai.com/api-keys)
3. **Créditos**: Adicione créditos na sua conta OpenAI (necessário para usar a API)

## 🔑 **Como Configurar a Chave da API**

### **Opção 1: Via local.properties (Recomendado para desenvolvimento)**

1. Abra o arquivo `local.properties` na raiz do projeto
2. Adicione sua chave da OpenAI:
   ```properties
   OPENAI_API_KEY=sk-proj-sua_chave_aqui_com_todos_os_caracteres
   ```

3. Atualize o `build.gradle` (app) para carregar a chave:
   ```gradle
   android {
       defaultConfig {
           // Outras configurações...
           
           buildConfigField "String", "OPENAI_API_KEY", "\"${localProperties.getProperty('OPENAI_API_KEY', 'NOT_CONFIGURED')}\""
       }
   }
   ```

4. Atualize o `OpenAIConfig.kt`:
   ```kotlin
   fun getApiKey(): String {
       return BuildConfig.OPENAI_API_KEY
   }
   ```

### **Opção 2: Via Variável de Ambiente**

1. Configure a variável de ambiente:
   ```bash
   # Windows
   set OPENAI_API_KEY=sk-proj-sua_chave_aqui
   
   # Linux/Mac
   export OPENAI_API_KEY=sk-proj-sua_chave_aqui
   ```

2. O código já está configurado para usar variáveis de ambiente automaticamente.

### **Opção 3: Diretamente no Código (NÃO recomendado)**

1. Edite `OpenAIConfig.kt`:
   ```kotlin
   fun getApiKey(): String {
       return "sk-proj-sua_chave_aqui_com_todos_os_caracteres"
   }
   ```

⚠️ **IMPORTANTE**: Nunca faça commit da chave no controle de versão!

## 🧪 **Como Testar a Integração**

1. Configure a chave seguindo uma das opções acima
2. Compile e execute o app
3. Use o scanner para ler um código de barras/EAN
4. Observe os logs para ver se a busca via ChatGPT está funcionando:
   ```
   D/OpenAIProductSearch: Buscando produto com EAN: 7891234567890
   D/OpenAIProductSearch: Resposta do ChatGPT: {"ean":"7891234567890",...}
   D/ProductSearchService: Produto encontrado via OpenAI: Nome do Produto
   ```

## 💰 **Custos**

- **GPT-3.5-turbo**: ~$0.002 por 1K tokens
- **Busca por produto**: ~100-200 tokens por consulta
- **Custo estimado**: ~$0.0004 por busca de produto

## 🔧 **Configurações Avançadas**

### **Modelos Disponíveis**
- `gpt-3.5-turbo`: Mais barato, boa qualidade
- `gpt-4`: Mais caro, melhor qualidade
- `gpt-4-turbo`: Equilibrio entre custo e qualidade

### **Parâmetros de Ajuste**
```kotlin
// OpenAIConfig.kt
const val DEFAULT_MODEL = "gpt-3.5-turbo"
const val MAX_TOKENS = 800        // Máximo de tokens na resposta
const val TEMPERATURE = 0.3       // 0.0 = determinístico, 1.0 = criativo
```

## 🐛 **Troubleshooting**

### **Erro: "Chave da OpenAI não configurada"**
- Verifique se a chave está corretamente configurada
- Certifique-se de que não há espaços extras na chave

### **Erro: "401 Unauthorized"**
- Chave inválida ou expirada
- Verifique sua conta OpenAI e gere uma nova chave

### **Erro: "429 Too Many Requests"**
- Limite de rate limit atingido
- Aguarde alguns minutos antes de tentar novamente

### **Erro: "Insufficient credits"**
- Adicione créditos à sua conta OpenAI
- Verifique o saldo em [platform.openai.com/usage](https://platform.openai.com/usage)

## 📊 **Funcionamento da Busca**

1. **Usuário escaneia EAN**: Scanner lê código de barras
2. **Prompt estruturado**: Sistema cria pergunta específica para ChatGPT
3. **Consulta ChatGPT**: API envia pergunta e recebe resposta JSON
4. **Parse da resposta**: Sistema extrai dados do produto
5. **Preenchimento automático**: Campos do formulário são preenchidos
6. **Fallback**: Se ChatGPT falhar, usa base de dados simulada

## 📝 **Exemplo de Resposta do ChatGPT**

```json
{
    "ean": "7891000100103",
    "name": "Leite Condensado Nestlé 395g",
    "description": "Leite condensado tradicional, ideal para sobremesas",
    "imageUrl": null,
    "brand": "Nestlé",
    "category": "Alimentos e Bebidas",
    "unit": "Unidade",
    "unitAbbreviation": "un",
    "averagePrice": 4.99,
    "found": true
}
```

## 🚀 **Próximos Passos**

Após configurar a chave:

1. ✅ Teste com produtos reais
2. ✅ Monitore os custos na OpenAI
3. ✅ Ajuste os prompts conforme necessário
4. ✅ Considere implementar cache para reduzir consultas repetidas
5. ✅ Configure rate limiting para evitar excesso de requests

---

**Desenvolvido com ❤️ usando OpenAI ChatGPT integration**
