# QUALITY.md - Padrões de Qualidade e Boas Práticas

## 📋 Visão Geral

Este documento define os padrões de qualidade, boas práticas e diretrizes técnicas aplicadas no desenvolvimento do PantryManager, garantindo código limpo, manutenível e escalável.

## 🏗️ Arquitetura e Design

### Clean Architecture

- **Separação de Responsabilidades**: Camadas bem definidas (Presentation, Domain, Data)
- **Inversão de Dependências**: Dependências apontam sempre para dentro
- **Testabilidade**: Código facilmente testável através de interfaces
- **Independência de Frameworks**: Lógica de negócio independente de tecnologias específicas

### Princípios SOLID

#### Single Responsibility Principle (SRP)
- Cada classe tem uma única responsabilidade
- Use Cases específicos para cada operação de negócio
- ViewModels focados em uma tela ou funcionalidade específica

#### Open/Closed Principle (OCP)
- Interfaces bem definidas para extensibilidade
- Novos comportamentos através de implementações, não modificações
- Strategy pattern para diferentes tipos de busca (local, API, IA)

#### Liskov Substitution Principle (LSP)
- Implementações de repositórios intercambiáveis
- Interfaces respeitam contratos bem definidos
- Polimorfismo sem quebrar funcionalidades

#### Interface Segregation Principle (ISP)
- Interfaces específicas e coesas
- Clientes não dependem de métodos que não usam
- Repositórios especializados por entidade

#### Dependency Inversion Principle (DIP)
- Dependências via abstrações (interfaces)
- Injeção de dependência com Hilt
- Módulos bem organizados e desacoplados

## 🔧 Padrões de Código

### Nomenclatura

#### Classes
```kotlin
// Use Cases
AddProductUseCase
AutoSearchCategoriesUseCase
DeleteMultipleProductsUseCase

// Repositories
ProductRepository (interface)
ProductRepositoryImpl (implementação)

// ViewModels
ProductRegistrationViewModel
CategoryManagementViewModel

// Screens
ProductRegistrationScreen
CategoryManagementScreen
```

#### Funções e Variáveis
```kotlin
// Funções: camelCase
fun addProduct()
fun searchCategories()
suspend fun deleteProducts()

// Variáveis: camelCase
val selectedItems
val isLoading
private val _uiState
```

#### Constantes
```kotlin
// UPPER_SNAKE_CASE
const val API_BASE_URL = "https://api.openai.com/v1/"
const val OPENAI_MODEL = "gpt-3.5-turbo"
const val DEFAULT_TIMEOUT = 30L
```

### Estrutura de Arquivos

```
feature/
├── presentation/
│   ├── viewmodel/     # *ViewModel.kt
│   ├── screen/        # *Screen.kt
│   └── components/    # *Components.kt
├── domain/
│   ├── usecase/       # *UseCase.kt
│   ├── repository/    # *Repository.kt (interface)
│   └── model/         # *.kt (data classes)
└── data/
    ├── repository/    # *RepositoryImpl.kt
    ├── dto/           # *Dto.kt
    ├── service/       # *Service.kt
    └── mapper/        # *Mapper.kt
```

## 🚀 Boas Práticas Implementadas

### Tratamento de Erros

#### Result Pattern
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

// Uso em repositórios
suspend fun addProduct(product: Product): Result<String> {
    return try {
        val id = firestore.collection("products").add(product).await().id
        Result.Success(id)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```

#### Estados de UI
```kotlin
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)
```

### Programação Assíncrona

#### Corrotinas com Contextos Apropriados
```kotlin
// Repositórios com IO context
suspend fun fetchFromApi(): Result<Data> = withContext(Dispatchers.IO) {
    // Network/Database operations
}

// ViewModels com viewModelScope
fun loadData() {
    viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        when (val result = useCase()) {
            is Result.Success -> handleSuccess(result.data)
            is Result.Error -> handleError(result.exception)
        }
    }
}
```

### Validação de Dados

#### Validação em Múltiplas Camadas
```kotlin
// Domain layer - Business rules
fun validateProduct(product: Product): ValidationResult {
    return when {
        product.name.isBlank() -> ValidationResult.Error("Nome obrigatório")
        product.batches.isEmpty() -> ValidationResult.Error("Pelo menos um lote obrigatório")
        product.batches.any { it.expirationDate.before(Date()) } -> 
            ValidationResult.Error("Data de validade não pode ser passada")
        else -> ValidationResult.Success
    }
}

// Presentation layer - UI validation
fun validateForm(): Boolean {
    return productName.isNotBlank() && 
           selectedCategory != null && 
           batches.isNotEmpty() &&
           batches.all { it.expirationDate != null }
}
```

### Integração com APIs Externas

#### OpenAI Integration
```kotlin
// Service layer com tratamento de erros
@Singleton
class OpenAICategorySearchService @Inject constructor(
    private val api: OpenAIApi,
    private val categoryRepository: CategoryRepository
) {
    suspend fun searchCategories(productName: String): List<Category> {
        return try {
            val response = api.getChatCompletion(createRequest(productName))
            if (response.isSuccessful) {
                parseResponse(response.body()?.choices?.firstOrNull()?.message?.content)
            } else {
                emptyList() // Fallback gracioso
            }
        } catch (e: Exception) {
            Log.e("OpenAI", "Error searching categories", e)
            emptyList() // Nunca quebrar o fluxo principal
        }
    }
}
```

#### Retrofit Configuration
```kotlin
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
```

## 🎯 Padrões de UI (Jetpack Compose)

### Composable Organization

#### State Hoisting
```kotlin
@Composable
fun ProductForm(
    product: Product,
    onProductChange: (Product) -> Unit,
    onSave: () -> Unit
) {
    // UI sem estado próprio
}

// ViewModel mantém o estado
class ProductViewModel : ViewModel() {
    private val _product = MutableStateFlow(Product())
    val product = _product.asStateFlow()
}
```

#### Reusable Components
```kotlin
@Composable
fun SelectableItemCard(
    item: Any,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onToggleSelection() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        content()
    }
}
```

### Performance Optimization

#### LazyColumn com Keys
```kotlin
LazyColumn {
    items(items = products, key = { it.id }) { product ->
        ProductCard(product = product)
    }
}
```

#### Remember para Expensive Operations
```kotlin
@Composable
fun ExpensiveScreen() {
    val expensiveValue = remember { calculateExpensiveValue() }
    
    val derivedValue = remember(dependency) {
        deriveSomething(dependency)
    }
}
```

## 🔒 Segurança e Validação

### Firebase Security Rules
```javascript
// Firestore rules - isolamento por usuário
match /products/{document} {
    allow read, write: if resource.data.userId == request.auth.uid;
}

match /categories/{document} {
    allow read, write: if resource.data.userId == request.auth.uid;
}
```

### Input Validation
```kotlin
// Validação de EAN
fun isValidEAN(ean: String): Boolean {
    return ean.matches(Regex("^[0-9]{8,13}$"))
}

// Sanitização de inputs
fun sanitizeProductName(name: String): String {
    return name.trim()
        .replace(Regex("[^a-zA-Z0-9\\s\\-_]"), "")
        .take(100) // Limitar tamanho
}
```

## 🧪 Testes e Qualidade

### Unit Tests Structure
```kotlin
class AddProductUseCaseTest {
    
    @Mock
    private lateinit var productRepository: ProductRepository
    
    private lateinit var useCase: AddProductUseCase
    
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = AddProductUseCase(productRepository)
    }
    
    @Test
    fun `should return success when product is valid`() = runTest {
        // Given
        val product = createValidProduct()
        coEvery { productRepository.addProduct(product) } returns Result.Success("id")
        
        // When
        val result = useCase(product)
        
        // Then
        assertTrue(result is Result.Success)
        coVerify { productRepository.addProduct(product) }
    }
}
```

### Integration Tests
```kotlin
@HiltAndroidTest
class ProductRepositoryImplTest {
    
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    
    @Test
    fun testFirebaseIntegration() {
        // Test real Firebase operations
    }
}
```

## 📊 Performance Guidelines

### Memory Management
- Use `StateFlow` ao invés de `LiveData` para melhor performance
- `remember` para cálculos custosos em Composables
- Evite vazamentos de memória com `viewModelScope.launch`

### Network Optimization
- Cache de respostas da API OpenAI
- Timeout apropriado para requests
- Retry policy para falhas de rede
- Batch operations no Firebase

### Database Optimization
- Índices apropriados no Firestore
- Paginação para listas grandes
- Operações em lote quando possível

## 🔄 Continuous Improvement

### Code Review Checklist
- [ ] Arquitetura Clean respeitada
- [ ] Princípios SOLID aplicados
- [ ] Tratamento de erros implementado
- [ ] Testes unitários incluídos
- [ ] Documentação atualizada
- [ ] Performance considerada
- [ ] Segurança validada

### Refactoring Guidelines
- Manter testes passando durante refactoring
- Uma mudança de cada vez
- Documentar breaking changes
- Manter backward compatibility quando possível

## 📚 Recursos Adicionais

### Documentação Técnica
- [PROMPT_PROJETO.md](PROMPT_PROJETO.md) - Documentação completa para reprodução
- [OPENAI_SETUP.md](OPENAI_SETUP.md) - Setup e configuração OpenAI
- [README.md](README.md) - Visão geral do projeto

### Padrões de Commit
```
feat: adiciona nova funcionalidade
fix: corrige bug
docs: atualiza documentação
style: mudanças de formatação
refactor: refactoring sem mudança de funcionalidade
test: adiciona ou atualiza testes
chore: mudanças de build, configuração, etc.
```

### Branch Strategy
- `main`: Código de produção
- `develop`: Integração de features
- `feature/*`: Desenvolvimento de funcionalidades
- `hotfix/*`: Correções urgentes

---

**Este documento garante que o PantryManager mantenha altos padrões de qualidade, seja facilmente manutenível e possa ser reproduzido e estendido por qualquer desenvolvedor ou IA.**