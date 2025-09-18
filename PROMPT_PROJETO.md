# PROMPT_PROJETO.md - PantryManager

## Documentação Técnica Completa para Reprodução por IA

Este documento contém todas as informações necessárias para que uma IA possa reproduzir completamente o projeto PantryManager a partir do zero, incluindo arquitetura, funcionalidades, implementações e configurações.

## 1. VISÃO GERAL DO PROJETO

### Descrição
PantryManager é um aplicativo Android nativo para gerenciamento completo de despensa e compras, construído com arquitetura limpa (Clean Architecture) e princípios SOLID.

### Principais Funcionalidades
- **Autenticação**: Firebase Auth + Google Sign-in
- **Gerenciamento de Usuários**: Registro, perfil e autenticação
- **Catálogo de Produtos**: Cadastro com scanner de código de barras/QR via CameraX + ML Kit
- **Automação OpenAI**: Busca automática de categorias, marcas e unidades de medida via ChatGPT
- **Sistema de Lotes**: Múltiplos lotes por produto com datas de validade
- **Exclusão Múltipla**: Seleção e remoção em lote de entidades
- **Integração NFe**: Processamento de notas fiscais eletrônicas
- **Listas de Compras**: Criação e gerenciamento
- **Receitas**: Gerenciamento de receitas
- **Dashboard**: Analytics e relatórios
- **Localização**: Recomendação de lojas próximas via Google Maps
- **Comparação de Preços**: Alertas de promoções

### Tecnologias Principais
- **Linguagem**: Kotlin 100%
- **UI**: Jetpack Compose
- **Arquitetura**: Clean Architecture + MVVM
- **Injeção de Dependência**: Hilt + KSP
- **Backend**: Firebase (Auth, Firestore, Storage)
- **Banco Local**: Room
- **APIs**: Retrofit + Moshi
- **IA**: OpenAI API (ChatGPT)
- **Scanner**: CameraX + ML Kit
- **Mapas**: Google Play Services (Maps, Location)

## 2. ARQUITETURA DETALHADA

### Estrutura de Camadas (Clean Architecture)

```
presentation/
├── ui/
│   ├── screens/           # Telas Compose
│   ├── components/        # Componentes reutilizáveis
│   └── theme/            # Tema e estilos
├── viewmodel/            # ViewModels MVVM
└── navigation/           # Navegação

domain/
├── model/               # Entidades de domínio
├── repository/          # Interfaces dos repositórios
└── usecase/            # Casos de uso (business logic)

data/
├── repository/         # Implementações dos repositórios
├── dto/               # Data Transfer Objects
├── service/           # Serviços de API
├── datasource/        # Fontes de dados
│   ├── local/         # Room Database
│   └── remote/        # Firebase/API
└── mapper/            # Mapeadores DTO <-> Domain

di/                    # Módulos Hilt
auth/                  # Autenticação Firebase + Google
nfe/                   # Integração NFe
maps/                  # Google Maps + Location
utils/                 # Utilitários e extensões
```

### Fluxo de Dados
1. **UI (Compose)** → ViewModel
2. **ViewModel** → Use Case
3. **Use Case** → Repository Interface
4. **Repository Implementation** → Data Source (Local/Remote)
5. **Data Source** → External APIs/Firebase/Room

### Princípios SOLID Aplicados
- **Single Responsibility**: Cada classe tem uma responsabilidade única
- **Open/Closed**: Extensível via interfaces, fechado para modificação
- **Liskov Substitution**: Implementações substituíveis
- **Interface Segregation**: Interfaces específicas e coesas
- **Dependency Inversion**: Dependências via abstrações (interfaces)

## 3. CONFIGURAÇÃO DO PROJETO

### Dependências Principais (build.gradle - app)
```kotlin
dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-compose:1.8.2'
    
    // Compose
    implementation platform('androidx.compose:compose-bom:2024.02.00')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.navigation:navigation-compose:2.7.6'
    
    // Hilt
    implementation "com.google.dagger:hilt-android:2.48"
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
    kapt "com.google.dagger:hilt-compiler:2.48"
    
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.android.gms:play-services-auth:20.7.0'
    
    // Room
    implementation "androidx.room:room-runtime:2.6.1"
    implementation "androidx.room:room-ktx:2.6.1"
    kapt "androidx.room:room-compiler:2.6.1"
    
    // Network
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.moshi:moshi:1.15.0'
    implementation 'com.squareup.moshi:moshi-kotlin:1.15.0'
    implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    
    // CameraX + ML Kit
    implementation "androidx.camera:camera-camera2:1.3.1"
    implementation "androidx.camera:camera-lifecycle:1.3.1"
    implementation "androidx.camera:camera-view:1.3.1"
    implementation 'com.google.mlkit:barcode-scanning:17.2.0'
    
    // Maps
    implementation 'com.google.android.gms:play-services-maps:18.2.0'
    implementation 'com.google.android.gms:play-services-location:21.0.1'
}
```

### Configuração KSP (build.gradle - app)
```kotlin
plugins {
    id 'com.google.devtools.ksp' version '1.9.21-1.0.15'
}

ksp {
    arg("verbose", "true")
    arg("moduleName", "app")
    arg("enableDebugMode", "true")
}
```

### Arquivos de Configuração Essenciais
- `google-services.json`: Configuração Firebase
- `local.properties`: Chaves API (OPENAI_API_KEY)
- `proguard-rules.pro`: Regras de ofuscação

## 4. IMPLEMENTAÇÕES CRÍTICAS

### 4.1 Sistema de Autenticação

#### FirebaseAuthManager
```kotlin
@Singleton
class FirebaseAuthManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
    fun isUserLoggedIn(): Boolean
}
```

### 4.2 Integração OpenAI

#### OpenAIApi Interface
```kotlin
interface OpenAIApi {
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body request: OpenAIRequest): Response<OpenAIResponse>
}
```

#### Serviços de Busca Automática
- `OpenAICategorySearchService`: Busca categorias por nome do produto
- `OpenAIBrandSearchService`: Busca marcas por nome do produto
- `OpenAIMeasurementUnitSearchService`: Busca unidades de medida

#### Use Cases de Automação
```kotlin
@Singleton
class AutoSearchCategoriesUseCase @Inject constructor(
    private val searchService: OpenAICategorySearchService,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(productName: String): List<Category>
}
```

### 4.3 Scanner de Código de Barras

#### BarcodeScannerActivity
```kotlin
@AndroidEntryPoint
class BarcodeScannerActivity : ComponentActivity() {
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var barcodeAnalyzer: BarcodeAnalyzer
    
    private fun startScanning() {
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, barcodeAnalyzer)
            }
    }
}
```

### 4.4 Sistema de Lotes Múltiplos

#### ProductBatch Entity
```kotlin
data class ProductBatch(
    val id: String = "",
    val quantity: Double = 0.0,
    val expirationDate: Date,
    val purchasePrice: Double? = null,
    val supplier: String? = null
)
```

#### ProductRegistrationViewModel
```kotlin
class ProductRegistrationViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val autoSearchCategoriesUseCase: AutoSearchCategoriesUseCase,
    // ... outros use cases
) : ViewModel() {
    
    private val _batches = mutableStateListOf<ProductBatch>()
    val batches: List<ProductBatch> = _batches
    
    fun addBatch(batch: ProductBatch) {
        _batches.add(batch)
    }
    
    suspend fun saveProduct() {
        // Salva produto com todos os lotes
    }
}
```

### 4.5 Sistema de Exclusão Múltipla

#### MultiSelectViewModel (Base)
```kotlin
abstract class MultiSelectViewModel<T> : ViewModel() {
    protected val _selectedItems = mutableStateListOf<T>()
    val selectedItems: List<T> = _selectedItems
    val isInSelectionMode: Boolean get() = _selectedItems.isNotEmpty()
    
    fun toggleItemSelection(item: T) {
        if (_selectedItems.contains(item)) {
            _selectedItems.remove(item)
        } else {
            _selectedItems.add(item)
        }
    }
    
    fun clearSelection() {
        _selectedItems.clear()
    }
    
    abstract suspend fun deleteSelectedItems()
}
```

### 4.6 Repositórios Firebase

#### ProductRepository
```kotlin
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProductRepository {
    
    override suspend fun addProduct(product: Product): Result<String> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("User not authenticated"))
            val productWithUser = product.copy(userId = userId)
            
            val docRef = firestore.collection("products").add(productWithUser).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteProducts(productIds: List<String>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val batch = firestore.batch()
            productIds.forEach { productId ->
                val docRef = firestore.collection("products").document(productId)
                batch.delete(docRef)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## 5. CONFIGURAÇÕES DE INJEÇÃO DE DEPENDÊNCIA

### NetworkModule
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    @Named("OpenAI")
    fun provideOpenAIRetrofit(
        @Named("OpenAI") okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    
    @Provides
    @Singleton
    fun provideOpenAIApi(@Named("OpenAI") retrofit: Retrofit): OpenAIApi =
        retrofit.create(OpenAIApi::class.java)
}
```

### ServiceBindsModule
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceBindsModule {
    
    @Binds
    abstract fun bindProductSearchService(
        productSearchServiceImpl: ProductSearchServiceImpl
    ): ProductSearchService
}
```

## 6. ESTRUTURA DE TELAS (COMPOSE)

### ProductRegistrationScreen
- Scanner de código de barras integrado
- Campos automáticos preenchidos via OpenAI
- Interface para múltiplos lotes
- Validação de dados obrigatórios

### ManagementScreens
- `CategoryManagementScreen`: CRUD de categorias + exclusão múltipla
- `BrandManagementScreen`: CRUD de marcas + exclusão múltipla  
- `UnitManagementScreen`: CRUD de unidades + exclusão múltipla
- `ProductManagementScreen`: CRUD de produtos + exclusão múltipla

### Componentes Reutilizáveis
- `SelectableItemCard`: Card com checkbox para seleção múltipla
- `MultiSelectTopBar`: TopBar com ações de seleção múltipla
- `BatchFormDialog`: Dialog para cadastro de lotes
- `ConfirmationDialog`: Dialog de confirmação para exclusões

## 7. FLUXOS DE FUNCIONAMENTO

### 7.1 Cadastro de Produto com Automação
1. Usuário acessa tela de cadastro
2. Opcional: Scanner de código de barras
3. Usuário digita nome do produto
4. Sistema busca automaticamente via OpenAI:
   - Categorias sugeridas
   - Marcas sugeridas
   - Unidades de medida sugeridas
5. Usuário adiciona lotes com datas de validade
6. Sistema salva produto no Firebase

### 7.2 Exclusão Múltipla
1. Usuário acessa tela de gerenciamento
2. Toca e segura em item para entrar em modo seleção
3. Seleciona múltiplos itens
4. Confirma exclusão
5. Sistema remove itens do Firebase em lote

### 7.3 Remoção de Duplicatas
1. Sistema identifica entidades duplicadas
2. Aplica algoritmo de similaridade
3. Remove duplicatas mantendo a mais recente
4. Atualiza referências em outras entidades

## 8. PADRÕES DE CÓDIGO

### Nomenclatura
- **Classes**: PascalCase (ex: `ProductRepository`)
- **Funções/Variáveis**: camelCase (ex: `addProduct`)
- **Constantes**: UPPER_SNAKE_CASE (ex: `API_BASE_URL`)
- **Arquivos**: PascalCase (ex: `ProductRegistrationScreen.kt`)

### Estrutura de Arquivos
```
UseCase: [Action][Entity]UseCase (ex: AddProductUseCase)
Repository: [Entity]Repository + [Entity]RepositoryImpl
ViewModel: [Feature]ViewModel (ex: ProductRegistrationViewModel)
Screen: [Feature]Screen (ex: ProductRegistrationScreen)
```

### Tratamento de Erros
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
```

### Estados de UI
```kotlin
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)
```

## 9. CONFIGURAÇÕES ESPECIAIS

### Chaves API necessárias (local.properties)
```properties
OPENAI_API_KEY=sk-...
GOOGLE_MAPS_API_KEY=AIza...
```

### Permissões Android (AndroidManifest.xml)
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Configuração Firebase
1. Adicionar `google-services.json` na pasta `app/`
2. Configurar Authentication (Email/Password + Google)
3. Configurar Firestore com regras de segurança
4. Configurar Storage para imagens

## 10. TESTES

### Estrutura de Testes
```
test/                     # Unit Tests
├── usecase/             # Testes de Use Cases
├── repository/          # Testes de Repositories
├── viewmodel/           # Testes de ViewModels
└── utils/               # Testes de utilitários

androidTest/             # Integration/UI Tests
├── ui/                  # Testes de UI (Compose)
├── database/            # Testes Room
└── api/                 # Testes de API
```

### Exemplo de Teste
```kotlin
@Test
fun `addProduct should return success when product is valid`() = runTest {
    // Given
    val product = Product(name = "Test Product", ean = "1234567890123")
    val expectedResult = Result.Success("productId")
    coEvery { productRepository.addProduct(product) } returns expectedResult
    
    // When
    val result = addProductUseCase(product)
    
    // Then
    assertEquals(expectedResult, result)
    coVerify { productRepository.addProduct(product) }
}
```

## 11. INSTRUÇÕES PARA REPRODUÇÃO COMPLETA

### Passo 1: Configuração do Projeto
1. Criar novo projeto Android no Android Studio
2. Configurar build.gradle com todas as dependências listadas
3. Adicionar plugins necessários (Hilt, KSP, etc.)
4. Configurar google-services.json do Firebase

### Passo 2: Estrutura de Pastas
1. Criar estrutura de pastas conforme arquitetura Clean
2. Implementar módulos de injeção de dependência
3. Configurar Application class com @HiltAndroidApp

### Passo 3: Implementação das Camadas
1. **Domain**: Entidades, interfaces de repositório, use cases
2. **Data**: Implementações de repositório, DTOs, serviços
3. **Presentation**: ViewModels, telas Compose, navegação

### Passo 4: Configurações Especiais
1. Configurar CameraX para scanner
2. Configurar OpenAI API com chave
3. Configurar Firebase Auth + Firestore
4. Implementar Google Sign-in

### Passo 5: Funcionalidades Avançadas
1. Sistema de lotes múltiplos
2. Exclusão múltipla com seleção
3. Automação via OpenAI
4. Remoção de duplicatas

### Passo 6: Testes e Validação
1. Implementar testes unitários
2. Testes de integração
3. Validação de funcionalidades
4. Testes de UI

## 12. MELHORIAS FUTURAS SUGERIDAS

### Performance
- Cache de respostas OpenAI
- Rate limiting para APIs
- Lazy loading de listas grandes
- Otimização de consultas Firebase

### Funcionalidades
- Modo offline com sincronização
- Backup/restore de dados
- Exportação de relatórios
- Notificações push para validades

### UI/UX
- Tema escuro
- Acessibilidade completa
- Animações avançadas
- Widget para tela inicial

## 13. TROUBLESHOOTING COMUM

### Erros de Build
- Limpar projeto: `./gradlew clean`
- Parar daemon: `./gradlew --stop`
- Verificar versões de dependências

### Erros KSP/Hilt
- Verificar configuração KSP
- Validar módulos Hilt
- Não misturar @Binds e @Provides no mesmo módulo

### Erros Firebase
- Verificar google-services.json
- Validar regras Firestore
- Confirmar configuração Authentication

### Erros OpenAI
- Verificar chave API válida
- Validar formato de request
- Implementar tratamento de rate limiting

---

**Este documento serve como guia completo para reprodução do projeto PantryManager por uma IA, contendo todos os detalhes técnicos, arquiteturais e de implementação necessários.**
