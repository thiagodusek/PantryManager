package com.pantrymanager.data.firebase

/**
 * Documentação da Integração Firebase para PantryManager
 * 
 * Este arquivo documenta como os dados da tela de registro são armazenados no Firebase Firestore
 * 
 * ESTRUTURA DO FIRESTORE:
 * 
 * Coleção: "users"
 * Documento: {userId} (gerado automaticamente pelo Firebase Auth)
 * 
 * Estrutura do documento:
 * {
 *   "id": "string",                    // ID único do usuário
 *   "nome": "string",                  // Nome do usuário
 *   "sobrenome": "string",             // Sobrenome do usuário
 *   "email": "string",                 // Email único
 *   "cpf": "string",                   // CPF do usuário
 *   "endereco": {                      // Objeto endereço
 *     "endereco": "string",            // Logradouro
 *     "numero": "string",              // Número
 *     "complemento": "string?",        // Complemento (opcional)
 *     "cep": "string",                 // CEP (8 dígitos)
 *     "cidade": "string",              // Cidade
 *     "estado": "string",              // Estado (sigla: SP, RJ, etc)
 *     "latitude": "double?",           // Latitude (opcional)
 *     "longitude": "double?"           // Longitude (opcional)
 *   },
 *   "login": "string",                 // Login único
 *   "nfePermissions": "boolean",       // Permissões NFe
 *   "searchRadius": "double",          // Raio de busca em km
 *   "profileImageUrl": "string?",      // URL da foto (opcional)
 *   "createdAt": "long",               // Timestamp de criação
 *   "updatedAt": "long"                // Timestamp de atualização
 * }
 * 
 * VALIDAÇÕES IMPLEMENTADAS:
 * 
 * 1. Email único: Verificação antes do cadastro
 * 2. CPF único: Verificação antes do cadastro com validação de dígito verificador
 * 3. Login único: Verificação antes do cadastro
 * 4. CEP: Validação de formato e busca automática via ViaCEP
 * 5. Estados: Seleção limitada aos estados brasileiros
 * 
 * FLUXO DE CADASTRO:
 * 
 * 1. Usuário preenche formulário
 * 2. Validação de campos obrigatórios e formatos (CPF, CEP)
 * 3. Validação de unicidade (email, CPF e login) no Firestore
 * 4. Criação do usuário no Firebase Auth
 * 5. Salvamento dos dados completos no Firestore
 * 6. Login automático após sucesso
 * 
 * ÍNDICES RECOMENDADOS NO FIRESTORE:
 * 
 * - Campo "email" (single field index)
 * - Campo "cpf" (single field index) 
 * - Campo "login" (single field index)
 * - Campo "endereco.estado" (single field index) - para buscas por região
 * - Campo "createdAt" (single field index) - para ordenação
 * 
 * REGRAS DE SEGURANÇA FIRESTORE:
 * 
 * rules_version = '2';
 * service cloud.firestore {
 *   match /databases/{database}/documents {
 *     match /users/{userId} {
 *       allow read, write: if request.auth != null && request.auth.uid == userId;
 *       allow read: if request.auth != null && resource.data.id == request.auth.uid;
 *     }
 *   }
 * }
 * 
 * EXEMPLOS DE USO:
 * 
 * // Salvar usuário
 * val user = User(
 *   nome = "João",
 *   sobrenome = "Silva",
 *   email = "joao@email.com",
 *   cpf = "12345678901",
 *   endereco = Address(
 *     endereco = "Rua das Flores",
 *     numero = "123",
 *     cep = "01234567",
 *     cidade = "São Paulo",
 *     estado = "SP"
 *   ),
 *   login = "joaosilva",
 *   nfePermissions = true,
 *   searchRadius = 10.0
 * )
 * 
 * viewModel.register(user, "senha123", "senha123")
 * 
 * // Buscar usuário atual
 * val currentUser = getCurrentUserUseCase()
 * 
 * // Validar email
 * val isEmailInUse = validateUserCredentialsUseCase.isEmailInUse("teste@email.com")
 * 
 * // Validar CPF
 * val isCpfInUse = validateUserCredentialsUseCase.isCpfInUse("12345678901")
 * 
 * // Validar login
 * val isLoginInUse = validateUserCredentialsUseCase.isLoginInUse("usuario123")
 * 
 * // Validar todos de uma vez
 * val validationResult = validateUserCredentialsUseCase.validateUniqueFields("email", "cpf", "login")
 */
object FirebaseDocumentation {
    
    const val COLLECTION_USERS = "users"
    
    // Estrutura de exemplo para referência
    data class FirestoreUserExample(
        val id: String = "abc123",
        val nome: String = "João",
        val sobrenome: String = "Silva", 
        val email: String = "joao@email.com",
        val cpf: String = "12345678901",
        val endereco: FirestoreAddressExample = FirestoreAddressExample(),
        val login: String = "joaosilva",
        val nfePermissions: Boolean = true,
        val searchRadius: Double = 10.0,
        val profileImageUrl: String? = null,
        val createdAt: Long = 1698756000000,
        val updatedAt: Long = 1698756000000
    )
    
    data class FirestoreAddressExample(
        val endereco: String = "Rua das Flores",
        val numero: String = "123",
        val complemento: String? = "Apto 45",
        val cep: String = "01234567",
        val cidade: String = "São Paulo",
        val estado: String = "SP",
        val latitude: Double? = -23.5505,
        val longitude: Double? = -46.6333
    )
}
