# DESENVOLVIMENTO.md - Guia de Desenvolvimento

## 🚀 Setup de Desenvolvimento Rápido

### Pré-requisitos
- Android Studio Hedgehog+ (2023.1.1+)
- JDK 19
- Android SDK API 34
- Git
- Conta Firebase
- Chave OpenAI API (opcional para automações)

### 🔧 Configuração Inicial

#### 1. Clone e Setup Básico
```bash
git clone <repository-url>
cd PantryManager
```

#### 2. Configuração Firebase
1. Crie projeto no [Firebase Console](https://console.firebase.google.com)
2. Ative Authentication (Email/Password + Google)
3. Ative Firestore Database
4. Baixe `google-services.json` → `app/`

#### 3. Configuração OpenAI (Opcional)
```properties
# local.properties
OPENAI_API_KEY=sk-sua-chave-aqui
```

#### 4. Build e Run
```bash
./gradlew clean
./gradlew build
```

### 🏗️ Estrutura para Novos Desenvolvedores

#### Entendendo as Camadas

```
📱 PRESENTATION (UI + ViewModels)
   ↓ (chama Use Cases)
🧠 DOMAIN (Business Logic)  
   ↓ (usa Repository Interfaces)
💾 DATA (Repositories + APIs)
```

#### Fluxo para Nova Feature
1. **Domain**: Criar entidade + use case + interface repository
2. **Data**: Implementar repository + DTOs + mappers
3. **Presentation**: ViewModel + Tela Compose
4. **DI**: Configurar injeção de dependência

### 🔨 Comandos Úteis

#### Build e Deploy
```bash
# Debug APK
./gradlew assembleDebug

# Release APK  
./gradlew assembleRelease

# Install on device
./gradlew installDebug

# Clean build
./gradlew clean
```

#### Testes
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

#### Debugging
```bash
# Stop Gradle daemon (se travado)
./gradlew --stop

# Refresh dependencies
./gradlew --refresh-dependencies

# Build com logs detalhados
./gradlew build --info
```

### 🐛 Troubleshooting Comum

#### Build Errors
- **KSP errors**: Verificar módulos Hilt (@Binds vs @Provides)
- **Gradle sync**: Limpar cache + restart IDE
- **Dependencies**: Verificar versões no `build.gradle`

#### Runtime Errors
- **Firebase**: Verificar `google-services.json`
- **OpenAI**: Verificar chave API no `local.properties`
- **Permissions**: Verificar no AndroidManifest.xml

### 🎯 Desenvolvimento de Novas Features

#### Template Use Case
```kotlin
@Singleton
class NewFeatureUseCase @Inject constructor(
    private val repository: FeatureRepository
) {
    suspend operator fun invoke(params: Params): Result<Output> {
        return try {
            val result = repository.performOperation(params)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

#### Template Repository
```kotlin
@Singleton
class FeatureRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FeatureRepository {
    
    override suspend fun performOperation(params: Params): Output = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        // Firebase operations
    }
}
```

#### Template ViewModel
```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val useCase: NewFeatureUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UiState<Data>())
    val uiState = _uiState.asStateFlow()
    
    fun performAction() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = useCase(params)) {
                is Result.Success -> handleSuccess(result.data)
                is Result.Error -> handleError(result.exception)
            }
        }
    }
}
```

### 📝 Convenções de Código

#### Git Commits
```
feat: adiciona scanner de código de barras
fix: corrige validação de EAN duplicado  
docs: atualiza README com novas funcionalidades
refactor: extrai lógica de validação para use case
test: adiciona testes para ProductRepository
```

#### Branches
- `main`: Produção
- `develop`: Desenvolvimento
- `feature/nome-feature`: Nova funcionalidade
- `fix/nome-bug`: Correção de bug

#### Code Style
- Kotlin Coding Conventions
- 4 espaços para indentação
- Line length: 120 characters
- Use trailing commas em listas multi-linha

### 🚀 Deploy e Distribuição

#### Debug APK
```bash
./gradlew assembleDebug
# APK em: app/build/outputs/apk/debug/
```

#### Release APK
1. Configure signing no `build.gradle`
2. `./gradlew assembleRelease`
3. APK em: `app/build/outputs/apk/release/`

#### Play Store Bundle
```bash
./gradlew bundleRelease
# AAB em: app/build/outputs/bundle/release/
```

### 📚 Recursos de Aprendizado

#### Documentação do Projeto
- [README.md](README.md) - Visão geral
- [PROMPT_PROJETO.md](PROMPT_PROJETO.md) - Specs técnicas
- [OPENAI_SETUP.md](OPENAI_SETUP.md) - Setup OpenAI
- [QUALITY.md](QUALITY.md) - Padrões de qualidade

#### Tecnologias Principais
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Firebase Android](https://firebase.google.com/docs/android/setup)
- [OpenAI API](https://platform.openai.com/docs)

### 🎯 Roadmap de Funcionalidades

#### Próximas Features
- [ ] Cache local para OpenAI responses
- [ ] Modo offline com sincronização
- [ ] Notificações de validade
- [ ] Export/import de dados
- [ ] Tema escuro
- [ ] Widget home screen

#### Melhorias Técnicas
- [ ] Testes de UI automatizados
- [ ] CI/CD pipeline
- [ ] Performance monitoring
- [ ] Crash reporting
- [ ] Analytics de uso

---

**Happy Coding! 🚀**
