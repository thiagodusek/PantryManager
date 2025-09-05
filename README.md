# 🛒 PantryManager

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" />
  <img src="https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material 3" />
</div>

> **Um aplicativo Android nativo moderno para gerenciamento inteligente de despensa e compras**

## 🔥 **STATUS: DEMO COMPLETO + GOOGLE SIGN-IN FUNCIONAL**

### **✅ IMPLEMENTADO E FUNCIONAL**
- 🔥 **Google Sign-In**: **TOTALMENTE FUNCIONAL** com Firebase Auth
- 🔐 **Autenticação**: Login/registro com Firebase (funcional)
- 📦 **Produtos**: CRUD completo (demo com UI funcional)
- 🏷️ **Categorias**: CRUD completo (demo com UI funcional)
- 📏 **Unidades**: CRUD completo (demo com UI funcional)
- 🧭 **Navegação**: Proteção de rotas por autenticação
- 🎨 **UI/UX**: Interface moderna com Jetpack Compose
- 📱 **Estado Reativo**: StateFlow e gerenciamento profissional

### 🔥 **DESTAQUE: GOOGLE SIGN-IN FUNCIONAL**
- **Interface real** do Google para seleção de conta
- **Autenticação OAuth 2.0** completa
- **Firebase integration** com Firestore
- **Persistência de sessão** automática
- **Logout completo** (Firebase + Google)
- **Documentação detalhada** para configuração

### 📋 **Características do Modo Demo:**
- ✅ **Interface Totalmente Funcional**: Navegação completa entre todas as telas
- ✅ **Mensagens de Sucesso**: Confirmações visuais para todas as operações  
- ✅ **Google Sign-In Real**: Autenticação funcional com contas Google
- ✅ **CRUD Simulado**: Operações mostram sucesso (produtos/categorias/unidades)
- ✅ **UX/UI Completo**: Material Design 3 com animações e transições

### 🎯 **Como Testar:**
1. **Configure Firebase** (ver `GOOGLE_SIGNIN_QUICKSTART.md`)
2. Execute o app no Android Studio
3. **Teste Google Sign-In**: Clique "Entrar com Google" → selecione conta
4. Teste todos os CRUDs: Produtos, Categorias, Unidades
5. Observe as mensagens de sucesso em tempo real
6. Navegue por todas as funcionalidades disponíveis

PantryManager é uma solução completa para organizar sua despensa, criar listas de compras inteligentes, importar notas fiscais eletrônicas (NFe), encontrar receitas baseadas nos ingredientes disponíveis e descobrir as melhores ofertas em lojas próximas.

## ✨ Funcionalidades

### 🏠 **Gestão de Despensa**

- **Cadastro de Produtos**: EAN, nome, descrição, categoria, unidade, observações e fotos
- **Controle de Estoque**: Quantidades, datas de validade e alertas de vencimento
- **Organização por Categorias**: Sistema flexível de categorização
- **Histórico de Consumo**: Análise de padrões de uso

### 📋 **Listas de Compras Inteligentes**

- **Criação Automática**: Baseada no consumo e estoque atual
- **Compartilhamento**: Sincronização entre dispositivos da família
- **Organização por Loja**: Otimização do percurso de compras
- **Controle de Orçamento**: Estimativa de gastos

### 🧾 **Integração NFe**

- **Importação Automática**: Leitura de QR Code de notas fiscais
- **Atualização de Estoque**: Adição automática de produtos comprados
- **Histórico de Compras**: Análise de gastos e fornecedores
- **Comparação de Preços**: Evolução de preços por estabelecimento

### 👨‍🍳 **Sistema de Receitas**

- **Sugestões Inteligentes**: Baseadas nos ingredientes disponíveis
- **Receitas Personalizadas**: Criação e edição de receitas próprias
- **Lista de Ingredientes**: Geração automática de lista de compras
- **Categorização**: Receitas por tipo, dificuldade e tempo

### 🗺️ **Localização e Ofertas**

- **Mapa de Lojas**: Estabelecimentos próximos com produtos desejados
- **Alertas de Promoções**: Notificações de ofertas relevantes
- **Comparação de Preços**: Entre diferentes estabelecimentos
- **Rotas Otimizadas**: Melhor trajeto para múltiplas lojas

### 📊 **Dashboard e Análises**

- **Visão Geral**: Status da despensa em tempo real
- **Relatórios de Gastos**: Análise financeira detalhada
- **Tendências de Consumo**: Padrões de uso familiar
- **Alertas Inteligentes**: Produtos próximos ao vencimento

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** e **SOLID**, garantindo código limpo, testável e escalável:

```
app/
├── presentation/          # 🎨 UI Layer (Compose, ViewModels, Navigation)
│   ├── ui/
│   │   ├── screens/      # Telas organizadas por feature
│   │   ├── components/   # Componentes reutilizáveis
│   │   ├── navigation/   # Sistema de navegação
│   │   └── theme/        # Material Design 3
│   └── viewmodel/        # ViewModels com StateFlow
├── domain/               # 🧠 Business Logic Layer
│   ├── entity/          # Entidades do domínio
│   ├── repository/      # Contratos dos repositórios
│   └── usecase/         # Casos de uso (CRUD operations)
├── data/                # 💾 Data Layer
│   ├── repository/      # Implementação dos repositórios
│   ├── datasource/      # DAOs do Room
│   └── dto/             # Data Transfer Objects
├── di/                  # 🔧 Dependency Injection (Hilt)
├── auth/                # 🔐 Autenticação (Firebase Auth)
├── nfe/                 # 🧾 Integração NFe
├── maps/                # 🗺️ Localização e mapas
└── utils/               # 🛠️ Utilitários e extensões
```

### 🎯 **Princípios Aplicados**

- **MVVM Pattern**: Separação clara entre UI e lógica de negócio
- **Repository Pattern**: Abstração da camada de dados
- **Dependency Injection**: Inversão de dependências com Hilt
- **Reactive Programming**: StateFlow/Flow para estados reativos
- **Single Source of Truth**: Room como fonte única da verdade

## 🛠️ Tecnologias

### **Core**

- ![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-0095D5?logo=kotlin&logoColor=white) - Linguagem principal
- ![Jetpack Compose](https://img.shields.io/badge/Compose-2024.02.00-4285F4?logo=jetpackcompose&logoColor=white) - UI Toolkit moderno
- ![Material 3](https://img.shields.io/badge/Material%203-1.1.2-757575?logo=material-design&logoColor=white) - Design System

### **Arquitetura**

- ![Hilt](https://img.shields.io/badge/Hilt-2.47-FF6F00?logo=dagger&logoColor=white) - Dependency Injection
- ![Navigation](https://img.shields.io/badge/Navigation-2.7.4-4CAF50?logo=android&logoColor=white) - Navegação entre telas
- ![ViewModel](https://img.shields.io/badge/ViewModel-2.7.0-2196F3?logo=android&logoColor=white) - Gerenciamento de estado

### **Dados**

- ![Room](https://img.shields.io/badge/Room-2.5.0-FF9800?logo=sqlite&logoColor=white) - Banco de dados local
- ![Firebase](https://img.shields.io/badge/Firebase-32.2.3-FFCA28?logo=firebase&logoColor=black) - Backend como serviço
- ![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-48B983?logo=square&logoColor=white) - Cliente HTTP

### **Funcionalidades Especiais**

- ![CameraX](https://img.shields.io/badge/CameraX-1.3.0-4CAF50?logo=android&logoColor=white) - Captura de fotos
- ![Google Maps](https://img.shields.io/badge/Maps-18.1.0-4285F4?logo=googlemaps&logoColor=white) - Localização
- ![ML Kit](https://img.shields.io/badge/ML%20Kit-16.0.0-FF6F00?logo=tensorflow&logoColor=white) - OCR para NFe

## 🚀 Como Executar

### **Pré-requisitos**

- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 11 ou superior
- Android SDK 34
- Dispositivo/Emulador Android 7.0 (API 24) ou superior

### **1. Clone o Repositório**

```bash
git clone https://github.com/seuusuario/PantryManager.git
cd PantryManager
```

### **2. Configuração do Firebase**

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com/)
2. Adicione um app Android com o package name `com.pantrymanager`
3. Baixe o arquivo `google-services.json` e coloque em `app/`
4. Ative Authentication, Firestore e Storage no console

### **3. Build e Execução**

```bash
# Fazer build do projeto
./gradlew assembleDebug

# Instalar no dispositivo
./gradlew installDebug

# Executar testes
./gradlew test
```

### **4. Executar no Android Studio**

1. Abra o projeto no Android Studio
2. Aguarde a sincronização do Gradle
3. Conecte um dispositivo ou inicie um emulador
4. Clique em "Run" ou use `Ctrl+R`

## 🎯 Funcionalidades Implementadas

### ✅ **CRUD Completo**

- [x] **Produtos**: Cadastro, edição, exclusão e listagem com seleção direta
- [x] **Categorias**: Gerenciamento com validações e edição inline
- [x] **Unidades**: Sistema flexível de medidas com modais
- [x] **Usuários**: Perfis e sistema de autenticação

### ✅ **Interface Moderna**

- [x] **Material Design 3**: Components e theming atualizado
- [x] **Navigation Drawer**: Menu lateral organizado por seções
- [x] **Cards Interativos**: Seleção direta para edição/exclusão em listas
- [x] **Estados Reativos**: Loading, error, success com feedback visual
- [x] **Formulários Inteligentes**: Validações em tempo real

### ✅ **Arquitetura Sólida**

- [x] **Clean Architecture**: Camadas presentation, domain, data
- [x] **MVVM**: ViewModels reativos com StateFlow
- [x] **Repository Pattern**: Abstração completa de dados
- [x] **Dependency Injection**: Hilt configurado com módulos
- [x] **Use Cases**: Lógica de negócio encapsulada

### 🚧 **Em Desenvolvimento**

- [ ] **Firebase Integration**: Auth, Firestore, Storage
- [ ] **NFe Integration**: QR Code scanning e parsing XML
- [ ] **Maps Integration**: Google Maps e location services
- [ ] **Recipe System**: Sugestões baseadas em estoque
- [ ] **Push Notifications**: Alertas de vencimento e ofertas
- [ ] **Camera Integration**: Fotos de produtos
- [ ] **Barcode Scanner**: Leitura de códigos EAN

## 📂 Estrutura do Projeto

```
PantryManager/
├── 📁 app/
│   ├── 📁 src/main/java/com/pantrymanager/
│   │   ├── 🎨 presentation/        # UI e ViewModels
│   │   │   ├── ui/screens/        # Telas por feature
│   │   │   ├── ui/components/     # Componentes reutilizáveis
│   │   │   ├── ui/navigation/     # Sistema de rotas
│   │   │   └── viewmodel/         # ViewModels reativos
│   │   ├── 🧠 domain/             # Entidades e Use Cases
│   │   │   ├── entity/           # Modelos de domínio
│   │   │   ├── repository/       # Contratos
│   │   │   └── usecase/          # Casos de uso CRUD
│   │   ├── 💾 data/               # Repositórios e DAOs
│   │   │   ├── repository/       # Implementações
│   │   │   ├── datasource/       # Room DAOs
│   │   │   └── dto/              # Data Transfer Objects
│   │   ├── 🔧 di/                 # Módulos Hilt
│   │   ├── 🔐 auth/               # Autenticação
│   │   ├── 🧾 nfe/                # Integração NFe
│   │   ├── 🗺️ maps/               # Localização
│   │   └── 🛠️ utils/              # Utilitários
│   └── 📁 src/test/               # Testes unitários
├── 📄 build.gradle                # Configuração do projeto
├── 📄 README.md                   # Este arquivo
├── 📄 CRUD_COMPLETO.md           # Documentação CRUD
└── 📄 STATUS_LISTAS_INTERATIVAS.md # Status das funcionalidades
```

## 📱 Menu de Funcionalidades

### **Gestão de Catálogo**

- **Cadastrar Produto** → Formulário completo com validações
- **Gerenciar Produtos** → Lista com edição/exclusão direta
- **Cadastrar Categoria** → Modal de criação rápida  
- **Gerenciar Categorias** → Edição inline na lista
- **Cadastrar Unidade** → Formulário nome/abreviação
- **Gerenciar Unidades** → Modal de edição/exclusão

### **Gestão de Despensa**

- **Itens da Despensa** → Controle de estoque (placeholder)
- **Importar NFe** → Leitura QR Code (placeholder)

### **Funcionalidades Avançadas**

- **Listas de Compras** → Criação e gerenciamento (placeholder)
- **Receitas** → Sistema de sugestões (placeholder)
- **Dashboard** → Análises e relatórios (placeholder)
- **Lojas Próximas** → Mapas e localização (placeholder)
- **Promoções** → Alertas de ofertas (placeholder)

## 🧪 Testes

O projeto está preparado para testes abrangentes:

```bash
# Testes unitários
./gradlew test

# Testes instrumentados
./gradlew connectedAndroidTest

# Relatório de cobertura
./gradlew jacocoTestReport
```

### **Estrutura de Testes Planejada**

- **Unit Tests**: Use Cases, ViewModels, Repositories
- **Integration Tests**: Room DAOs, API calls  
- **UI Tests**: Compose screens, Navigation flows
- **E2E Tests**: Fluxos completos de usuário

## 🤝 Como Contribuir

Contribuições são sempre bem-vindas! Para contribuir:

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/MinhaFeature`)
3. **Commit** suas mudanças (`git commit -m 'feat: Adiciona funcionalidade incrível'`)
4. **Push** para a branch (`git push origin feature/MinhaFeature`)
5. Abra um **Pull Request**

### **Padrões de Commit**

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `style:` Formatação
- `refactor:` Refatoração
- `test:` Testes
- `chore:` Configuração

## 📄 Licença

Este projeto está licenciado sob a **MIT License**.

## 👥 Autor

- **Seu Nome** - *Desenvolvedor Principal* - [@seuusuario](https://github.com/seuusuario)

## 🙏 Agradecimentos

- Material Design Team pela inspiração visual
- Android Team pelo Jetpack Compose
- Comunidade Kotlin pelo suporte
- Firebase Team pelas ferramentas

---

<div align="center">
  <p><strong>Feito com ❤️ e muito ☕</strong></p>
  <p>Se este projeto te ajudou, considere dar uma ⭐!</p>
</div>
