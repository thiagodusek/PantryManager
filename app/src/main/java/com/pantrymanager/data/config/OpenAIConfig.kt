package com.pantrymanager.data.config

/**
 * Configurações da OpenAI API
 */
object OpenAIConfig {
    
    /**
     * TODO: IMPORTANTE - Configure sua chave da OpenAI
     * 
     * OPÇÕES PARA CONFIGURAR:
     * 
     * 1. Via BuildConfig (Recomendado para produção):
     *    - Adicione no build.gradle: buildConfigField "String", "OPENAI_API_KEY", '"sua_chave_aqui"'
     *    - Use: BuildConfig.OPENAI_API_KEY
     * 
     * 2. Via local.properties (Recomendado para desenvolvimento):
     *    - Adicione no local.properties: OPENAI_API_KEY=sua_chave_aqui
     *    - Carregue no build.gradle e use via BuildConfig
     * 
     * 3. Via variável de ambiente:
     *    - Configure OPENAI_API_KEY como variável de ambiente
     *    - Use System.getenv("OPENAI_API_KEY")
     * 
     * 4. Diretamente no código (NÃO recomendado):
     *    - Substitua "YOUR_OPENAI_API_KEY" pela sua chave
     *    - NUNCA faça commit da chave no controle de versão
     */
    
    // Método para obter a chave da API
    fun getApiKey(): String {
        // Opção 1: Via BuildConfig (recomendado)
        val buildConfigKey = com.pantrymanager.BuildConfig.OPENAI_API_KEY
        if (buildConfigKey != "YOUR_OPENAI_API_KEY_HERE" && buildConfigKey.isNotBlank()) {
            return buildConfigKey
        }
        
        // Opção 2: Via variável de ambiente
        val envKey = System.getenv("OPENAI_API_KEY")
        if (!envKey.isNullOrBlank()) {
            return envKey
        }
        
        // Opção 3: Chave não configurada
        throw IllegalStateException(
            "OpenAI API Key não configurada!\n" +
            "Adicione OPENAI_API_KEY=sua_chave no arquivo local.properties\n" +
            "Ou configure a variável de ambiente OPENAI_API_KEY"
        )
    }
    
    // Configurações da API
    const val DEFAULT_MODEL = "gpt-3.5-turbo"
    const val MAX_TOKENS = 800
    const val TEMPERATURE = 0.3
    const val BASE_URL = "https://api.openai.com/v1/"
    
    // Validar se a chave está configurada
    fun isApiKeyConfigured(): Boolean {
        return try {
            val key = getApiKey()
            key.startsWith("sk-") && key.isNotBlank()
        } catch (e: IllegalStateException) {
            false
        }
    }
}
