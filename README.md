# 🛒 PantryManager

> **Sistema Inteligente de Gerenciamento de Despensa e Compras com IA**

Um aplicativo Android nativo que revoluciona o controle de despensa através de automação inteligente, scanner de códigos, múltiplos lotes e integração com ChatGPT para preenchimento automático de dados.

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](htt### Documentação Técnica Completa

- 🤖 **[PROMPT_PROJETO.md](PROMPT_PROJETO.md)** - Documentação técnica completa para reprodução por IA
- 🔧 **[DESENVOLVIMENTO.md](DESENVOLVIMENTO.md)** - Guia completo de desenvolvimento e setup  
- 🔑 **[OPENAI_SETUP.md](OPENAI_SETUP.md)** - Configuração e uso da integração OpenAI
- ✨ **[QUALITY.md](QUALITY.md)** - Padrões de qualidade e boas práticas

### Documentação Adicional

- 📋 [FUNCIONALIDADES_IMPLEMENTADAS.md](FUNCIONALIDADES_IMPLEMENTADAS.md) - Lista detalhada de funcionalidades
- 🚀 [FIREBASE_SCANNER_INTEGRATION.md](FIREBASE_SCANNER_INTEGRATION.md) - Integração Firebase e Scannerlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com)
[![OpenAI](https://img.shields.io/badge/OpenAI-412991?style=for-the-badge&logo=openai&logoColor=white)](https://openai.com)
[![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#️-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#️-instalação)
- [Como Usar](#-como-usar)
- [Arquitetura](#️-arquitetura)
- [Contribuição](#-contribuição)
- [Documentação](#-documentação)
- [Licença](#-licença)
- [Contato](#-contato)

---

## 🎯 Sobre o Projeto

O **PantryManager** é um aplicativo Android desenvolvido como Trabalho de Conclusão de Curso (TCC) que resolve os principais problemas de gestão doméstica de alimentos:

### 🤔 Problemas que Resolve

- **Desperdício de Alimentos**: Controle rigoroso de validade com múltiplos lotes
- **Compras Duplicadas**: Scanner de códigos com validação EAN única por usuário
- **Cadastro Manual Demorado**: Automação via IA (ChatGPT) para preenchimento automático
- **Desorganização**: Sistema estruturado com categorias, marcas e unidades de medida
- **Falta de Controle**: Interface moderna com exclusão múltipla e operações em lote

### 🎯 Objetivo

Criar um sistema completo, moderno e inteligente que automatize ao máximo o processo de gerenciamento de despensa, utilizando as mais recentes tecnologias Android e inteligência artificial.

---

## ✨ Funcionalidades

### 🤖 Automação com Inteligência Artificial

**Nova integração com OpenAI/ChatGPT para automação completa do cadastro:**

- **Busca Automática de Categorias**: IA sugere categorias baseadas no nome do produto
- **Busca Automática de Marcas**: Identificação inteligente de marcas conhecidas  
- **Busca Automática de Unidades**: Sugestão de unidades de medida apropriadas
- **Remoção de Duplicatas**: IA identifica e remove categorias/marcas/unidades duplicadas
- **Preenchimento Inteligente**: Cadastro com sugestões automáticas
- **Fallback Robusto**: Funciona mesmo sem conexão ou cota da API esgotada

### 📱 Scanner e Cadastro Inteligente

- **Scanner QR Code/Código de Barras**: CameraX + ML Kit com detecção em tempo real
- **EAN Único por Usuário**: Validação em tempo real para evitar duplicatas
- **Múltiplos Formatos**: Suporte a EAN-13, EAN-8, UPC-A, UPC-E, Code 128
- **Importação de Cupom Fiscal**: NFC-e e CF-e SAT com consulta automática na Sefaz
- **Busca Automática de Produtos**: Base nacional com preenchimento automático

### 📦 Sistema de Lotes Avançado

- **Múltiplos Lotes por Produto**: Cada produto pode ter vários lotes independentes
- **Datas de Validade Obrigatórias**: Controle rigoroso de validade por lote
- **Interface Intuitiva**: UI/UX otimizada para gerenciar múltiplos lotes
- **Validação Automática**: Verificação de dados de lote em tempo real
- **Histórico Completo**: Rastreamento de entrada e saída por lote

### 🗑️ Operações em Lote

- **Exclusão Múltipla**: Seleção e remoção em lote em todas as telas
- **Seleção Intuitiva**: Toque longo para ativar modo seleção múltipla
- **Operações Otimizadas**: Firebase Batch para máxima performance
- **Confirmação de Segurança**: Dialogs para prevenir exclusões acidentais
- **Feedback Visual**: Estados visuais claros para itens selecionados

### 🔐 Autenticação e Segurança

- **Firebase Authentication**: Sistema de login seguro e robusto
- **Google Sign-in**: Login rápido com conta Google
- **Isolamento por Usuário**: Cada usuário vê apenas seus próprios dados
- **Validação EAN Única**: Por usuário, não global

### 🏷️ Organização Completa

- **Categorias**: Sistema completo com 39 categorias pré-cadastradas
- **Marcas**: Gerenciamento inteligente de marcas
- **Unidades de Medida**: 27 unidades pré-cadastradas + personalização
- **CRUD Completo**: Criar, ler, atualizar e deletar para todas as entidades

## �️ Tecnologias

### Linguagens e Frameworks

- **[Kotlin](https://kotlinlang.org)** - Linguagem principal (100% Kotlin)
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - UI moderna e declarativa
- **[Material Design 3](https://m3.material.io)** - Sistema de design atualizado

### Backend e Banco de Dados

- **[Firebase](https://firebase.google.com)**
  - **Firestore** - Banco NoSQL em tempo real
  - **Authentication** - Autenticação segura + Google Sign-in
  - **Storage** - Armazenamento de imagens
- **[OpenAI API](https://openai.com)** - Integração com ChatGPT para automações

### Bibliotecas e Dependências

- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)** - Injeção de dependência
- **[CameraX](https://developer.android.com/training/camerax)** - API moderna de câmera
- **[ML Kit](https://developers.google.com/ml-kit)** - Machine Learning para scanner
- **[Retrofit](https://square.github.io/retrofit/)** + **[Moshi](https://github.com/square/moshi)** - Cliente HTTP e serialização JSON
- **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** - Programação assíncrona

### Arquitetura

- **[Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)** - Separação clara de responsabilidades
- **MVVM Pattern** - Model-View-ViewModel
- **SOLID Principles** - Princípios de design orientado a objetos
- **Repository Pattern** - Abstração de fontes de dados

---

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter:

### Ambiente de Desenvolvimento

- **Android Studio** Hedgehog+ (2023.1.1 ou superior)
- **JDK 19** ou superior  
- **Android SDK** API 34
- **Git** para controle de versão

### Contas e Chaves de API

- **Conta Firebase** - [Criar conta](https://console.firebase.google.com)
- **Chave OpenAI** (opcional) - [Obter chave](https://platform.openai.com/api-keys)

### Dispositivo/Emulador

- **Android 7.0+** (API 24 ou superior)
- **Câmera** (para funcionalidade de scanner)
- **Conexão com Internet**

---

## ⚙️ Instalação

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/PantryManager.git
cd PantryManager
```

### 2. Configuração do Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com)
2. Crie um novo projeto ou use um existente
3. Ative os seguintes serviços:
   - **Authentication** (Email/Password + Google Sign-in)
   - **Firestore Database**
   - **Storage**
4. Baixe o arquivo `google-services.json`
5. Coloque o arquivo na pasta `app/` do projeto

### 3. Configuração da OpenAI (Opcional)

Para habilitar as automações com IA, configure sua chave da API:

```properties
# local.properties
OPENAI_API_KEY=sk-sua-chave-aqui
```

> 💡 **Nota**: O app funciona perfeitamente sem a chave OpenAI, apenas as sugestões automáticas ficam desabilitadas.

### 4. Build e Execução

1. Abra o projeto no Android Studio
2. Aguarde a sincronização do Gradle
3. Execute no dispositivo ou emulador:

```bash
# Via linha de comando
./gradlew installDebug

# Via Android Studio
Run > Run 'app'
```

---

## 🚀 Como Usar

### Primeiro Acesso

1. **Faça login** com email/senha ou Google
2. **Explore as categorias** pré-cadastradas
3. **Cadastre seu primeiro produto** via scanner ou manualmente

### Cadastro de Produtos

#### Via Scanner (Recomendado)
1. Toque no ícone da câmera
2. Aponte para o código de barras
3. Aguarde o preenchimento automático
4. Adicione lotes com datas de validade
5. Salve o produto

#### Via IA (ChatGPT)  
1. Digite o nome do produto
2. Aguarde as sugestões automáticas de categoria, marca e unidade
3. Selecione as opções desejadas
4. Adicione lotes e salve

#### Manual
1. Preencha todos os campos obrigatórios
2. Selecione categoria, marca e unidade
3. Adicione pelo menos um lote
4. Salve o produto

### Gerenciamento

- **Ver produtos**: Lista com busca e filtros
- **Editar**: Toque no produto para editar
- **Exclusão múltipla**: Toque longo para seleção múltipla
- **Gerenciar categorias/marcas/unidades**: Menu lateral

### Importação de Cupom Fiscal

1. Acesse "Importar NFe"
2. Escaneie o QR Code do cupom
3. Aguarde o processamento automático
4. Revise os produtos importados
5. Salve no seu acervo

---

## 🏗️ Arquitetura

O projeto segue **Clean Architecture** com separação clara de responsabilidades:

```text
📱 PRESENTATION LAYER
   ├── UI (Compose Screens)
   ├── ViewModels (MVVM)
   └── Navigation
   
🧠 DOMAIN LAYER  
   ├── Entities (Models)
   ├── Use Cases (Business Logic)
   └── Repository Interfaces
   
💾 DATA LAYER
   ├── Repository Implementations  
   ├── Data Sources (Firebase/API)
   ├── DTOs & Mappers
   └── Services (OpenAI, etc.)
```

### Fluxo de Dados

1. **UI** → ViewModel → Use Case → Repository → Data Source
2. **Data Source** → Repository → Use Case → ViewModel → **UI**

### Princípios Aplicados

- ✅ **Single Responsibility** - Uma responsabilidade por classe
- ✅ **Open/Closed** - Extensível via interfaces
- ✅ **Liskov Substitution** - Implementações intercambiáveis  
- ✅ **Interface Segregation** - Interfaces específicas
- ✅ **Dependency Inversion** - Dependências via abstrações

---

## 🤝 Contribuição

Contribuições são sempre bem-vindas! Siga estes passos:

### Como Contribuir

1. **Fork** o projeto
2. **Clone** seu fork: `git clone https://github.com/seu-usuario/PantryManager.git`
3. **Crie uma branch**: `git checkout -b feature/nova-funcionalidade`
4. **Faça suas alterações** seguindo os padrões do projeto
5. **Commit**: `git commit -m 'feat: adiciona nova funcionalidade'`
6. **Push**: `git push origin feature/nova-funcionalidade`
7. **Abra um Pull Request**

### Padrões de Código

- **Kotlin Coding Conventions**
- **Clean Architecture** obrigatória
- **Testes unitários** para use cases
- **Documentação** atualizada

### Padrões de Commit

```
feat: nova funcionalidade
fix: correção de bug  
docs: atualização de documentação
style: formatação, sem mudança de lógica
refactor: refatoração sem nova funcionalidade
test: adição ou correção de testes
chore: configuração, build, etc.
```

---

## 📚 Documentação

### **🔍 Scanner de QR Code/Código de Barras**

- ✅ **CameraX + ML Kit**: Scanner nativo com detecção em tempo real
- ✅ **Múltiplos Formatos**: EAN-13, EAN-8, UPC-A, UPC-E, Code 128
- ✅ **Interface Moderna**: Preview da câmera com indicadores visuais
- ✅ **Flash Toggle**: Controle de flash para ambientes com pouca luz
- ✅ **Validação EAN Única**: Verifica se código já foi cadastrado pelo usuário

### **🧾 Importação de Cupom Fiscal**

- ✅ **NFC-e e CF-e SAT**: Suporte completo a cupons fiscais brasileiros
- ✅ **Consulta Automática**: Integração com APIs da Sefaz para obter dados
- ✅ **Processamento Inteligente**: Detecta tipo de QR Code automaticamente
- ✅ **Importação em Lote**: Importa todos os produtos do cupom de uma vez
- ✅ **Respeita EAN Único**: Produtos duplicados são atualizados, não duplicados

### **🔍 Pesquisa Automática de Produtos**

- ✅ **Base Nacional**: Produtos brasileiros (códigos 789)
- ✅ **Preenchimento Automático**: Nome, marca, categoria, preço
- ✅ **Criação de Entidades**: Auto-criação de marca, categoria e unidade
- ✅ **Fallback Inteligente**: Produto genérico quando não encontrado

## 🗄️ **Modelo de Dados Estruturado**

### **📊 Entidades Separadas**

- ✅ **Product**: Produto principal com referências
- ✅ **Category**: Categorias organizadas (39 pré-cadastradas)  
- ✅ **MeasurementUnit**: Unidades de medida (27 pré-cadastradas)
- ✅ **Brand**: Marcas dos produtos
- ✅ **ProductBatch**: Lotes com data de validade
- ✅ **FiscalReceipt**: Cupons fiscais importados
- ✅ **User**: Dados do usuário autenticado

### **🔐 Segurança por Usuário**

- ✅ **Isolamento Total**: Cada usuário vê apenas seus dados
- ✅ **EAN Único**: Por usuário (não global)
- ✅ **Validação Automática**: Em inserção e atualização
- ✅ **Filtros Seguros**: Todas as consultas filtradas por userId

## 🎨 **Interface Moderna**

### **📱 Telas de Gerenciamento**

- ✅ **Grid Visual**: Visualização em cards organizados
- ✅ **Seleção Múltipla**: Checkboxes para operações em lote
- ✅ **Exclusão Múltipla**: Deletar vários itens de uma vez
- ✅ **Busca e Filtros**: Encontrar itens rapidamente
- ✅ **Estados Visuais**: Loading, erro, sucesso com feedback

### **🔧 Funcionalidades de CRUD**

- ✅ **Produtos**: Cadastro, edição, exclusão com lotes
- ✅ **Categorias**: Gerenciamento completo com cores
- ✅ **Unidades**: Gerenciamento de unidades de medida
- ✅ **Marcas**: Cadastro e organização de marcas
- ✅ **Lotes**: Múltiplos lotes por produto com validade

## 🏗️ **Arquitetura Clean/SOLID**

### **📁 Estrutura de Camadas**

```
presentation/           # Interface de usuário
├── viewmodel/         # Estados e lógica de apresentação  
├── ui/screens/        # Telas Jetpack Compose
└── ui/components/     # Componentes reutilizáveis

domain/                # Regras de negócio
├── entity/           # Entidades de domínio
├── repository/       # Contratos dos repositórios  
└── usecase/          # Casos de uso

data/                  # Acesso a dados
├── repository/       # Implementações Firebase
├── dto/              # Mapeamento de dados
├── service/          # APIs externas (Sefaz)
└── defaults/         # Dados padrão

di/                   # Injeção de dependência
```

### **🎯 Princípios SOLID Aplicados**

- ✅ **Single Responsibility**: Uma responsabilidade por classe
- ✅ **Open/Closed**: Extensível sem modificação
- ✅ **Liskov Substitution**: Interfaces bem definidas  
- ✅ **Interface Segregation**: Interfaces específicas
- ✅ **Dependency Inversion**: Injeção com Hilt

## 🔥 **Tecnologias e Frameworks**

### **📱 Android Moderno**

- ✅ **Java 19**: Versão mais recente
- ✅ **Jetpack Compose**: UI declarativa
- ✅ **Material Design 3**: Design system atualizado
- ✅ **CameraX**: API moderna de câmera
- ✅ **ML Kit**: Machine Learning para scanner

### **🔥 Backend e Armazenamento**

- ✅ **Firebase Firestore**: Banco NoSQL em tempo real
- ✅ **Firebase Auth**: Autenticação segura
- ✅ **Firebase Storage**: Armazenamento de imagens
- ✅ **OpenAI API**: Integração com ChatGPT para automações
- ✅ **Hilt**: Injeção de dependência

### **🛠️ Bibliotecas e Ferramentas**

- ✅ **Retrofit**: Cliente HTTP para APIs
- ✅ **Moshi**: Serialização JSON moderna (substituiu Gson)
- ✅ **OkHttp**: Cliente HTTP com interceptors para logging
- ✅ **Coroutines**: Programação assíncrona
- ✅ **StateFlow**: Gerenciamento de estado reativo
- ✅ **KSP**: Kotlin Symbol Processing para Hilt

## 📋 **Fluxos de Uso**

### **🔍 Cadastro via Scanner**

```
Tela Produto → Scanner EAN → Busca Online → 
Preenchimento Automático → Validação Única → 
Criação Entidades → Salvar Firebase
```

### **🤖 Cadastro com Automação IA**

```
Tela Produto → Digite Nome → IA Busca Categorias/Marcas/Unidades → 
Seleção Automática → Adicionar Lotes → Validação → 
Salvar Firebase com Todas Entidades
```

### **⚡ Gerenciamento de Lotes**

```
Cadastro Produto → Adicionar Lote → Data Validade Obrigatória → 
Quantidade e Preço → Validação → Lista de Lotes → 
Salvar Múltiplos Lotes
```

### **🧾 Importação de Cupom**

```
Menu NFe → Scanner Cupom → Detecção Tipo → 
Consulta Sefaz → Extração Dados → Validação EAN → 
Importação Produtos → Criação Lotes
```

### **🗑️ Exclusão em Lote**

```
Tela Gerenciamento → Seleção Múltipla → 
Confirmação → Exclusão Firebase
```

## 🚀 **Como Executar**

### **📋 Pré-requisitos**

- Android Studio Hedgehog ou superior
- JDK 19
- Android SDK API 34
- Dispositivo/Emulador Android 7.0+ (API 24)

### **⚙️ Configuração**

1. Clone o repositório
2. Configure Firebase (google-services.json)
3. **NOVO!** Configure OpenAI API Key no `local.properties`:
   ```properties
   OPENAI_API_KEY=sk-sua-chave-aqui
   ```
4. Sincronize dependências do Gradle
5. Execute no dispositivo/emulador

> 📖 **Veja o arquivo [OPENAI_SETUP.md](OPENAI_SETUP.md) para instruções detalhadas da configuração OpenAI**

### **📱 Gerando APK**

```bash
./gradlew assembleDebug          # APK de debug
./gradlew assembleRelease        # APK de produção
```

## 📊 **Funcionalidades Implementadas**

### **✅ Sistema Completo**

- [x] Autenticação com Firebase Auth
- [x] Scanner QR Code/Código de barras  
- [x] Importação de cupom fiscal (NFC-e/CF-e SAT)
- [x] EAN único por usuário
- [x] Pesquisa automática de produtos
- [x] Criação automática de entidades relacionadas
- [x] **NOVO!** Automação com OpenAI/ChatGPT
- [x] **NOVO!** Busca automática de categorias, marcas e unidades via IA
- [x] **NOVO!** Sistema de múltiplos lotes por produto
- [x] **NOVO!** Datas de validade obrigatórias por lote
- [x] **NOVO!** Exclusão múltipla em todas as telas de gerenciamento
- [x] **NOVO!** Remoção automática de duplicatas via IA
- [x] CRUD completo para todas entidades
- [x] Interface moderna com seleção múltipla
- [x] Dados padrão pré-cadastrados
- [x] Arquitetura Clean/SOLID
- [x] Firebase integrado completamente

### **🎯 Diferenciais**

- [x] **Multi-tenant**: Isolamento por usuário
- [x] **Validação EAN**: Única por usuário  
- [x] **Import Inteligente**: Cupom fiscal completo
- [x] **Scanner Nativo**: CameraX + ML Kit
- [x] **Auto-complete**: Criação de entidades relacionadas
- [x] **🤖 IA Integrada**: OpenAI/ChatGPT para automações
- [x] **🔄 Smart Deduplication**: Remoção automática de duplicatas
- [x] **📦 Multi-Batch System**: Múltiplos lotes com validação
- [x] **🗑️ Bulk Operations**: Exclusão múltipla otimizada
- [x] **Interface Moderna**: Material Design 3
- [x] **Arquitetura Sólida**: Clean + SOLID

## ⚙️ **Configuração do Projeto**

### **📝 Configuração das API Keys**

1. **Copie o arquivo template:**
   ```bash
   cp local.properties.template local.properties
   ```

2. **Configure suas chaves no arquivo `local.properties`:**
   ```properties
   # OpenAI API Key (obrigatório para funcionalidades de IA)
   OPENAI_API_KEY=sk-proj-SUA_CHAVE_OPENAI_AQUI
   
   # Google Maps API Key (opcional)
   GOOGLE_MAPS_API_KEY=SUA_CHAVE_GOOGLE_MAPS_AQUI
   
   # Firebase Web API Key (opcional)
   FIREBASE_API_KEY=SUA_CHAVE_FIREBASE_WEB_AQUI
   ```

3. **Obtenha suas chaves:**
   - **OpenAI API**: https://platform.openai.com/api-keys
   - **Google Maps**: https://console.cloud.google.com/apis/credentials
   - **Firebase**: https://console.firebase.google.com/

⚠️ **IMPORTANTE**: Nunca commite o arquivo `local.properties` no Git!

## 📄 **Documentação Completa**

- 🤖 **[PROMPT_PROJETO.md](PROMPT_PROJETO.md)** - **NOVO!** Documentação técnica completa para reprodução por IA
- � **[DESENVOLVIMENTO.md](DESENVOLVIMENTO.md)** - **NOVO!** Guia completo de desenvolvimento e setup
- �🔑 **[OPENAI_SETUP.md](OPENAI_SETUP.md)** - **NOVO!** Configuração e uso da integração OpenAI
- ✨ [QUALITY.md](QUALITY.md) - Padrões de qualidade e boas práticas
- 📋 [FUNCIONALIDADES_IMPLEMENTADAS.md](FUNCIONALIDADES_IMPLEMENTADAS.md) - Lista completa de funcionalidades
- 🚀 [FIREBASE_SCANNER_INTEGRATION.md](FIREBASE_SCANNER_INTEGRATION.md) - Integração Firebase e Scanner

---

## 📄 Licença

Este projeto está sob a licença MIT. Isso significa que você pode:

- ✅ Usar comercialmente  
- ✅ Modificar o código
- ✅ Distribuir
- ✅ Uso privado

**Requisitos:**
- Incluir o aviso de licença e copyright
- Documentar mudanças significativas

Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📞 Contato

### Desenvolvedor

**Seu Nome**
- 📧 Email: [seu.email@exemplo.com](mailto:seu.email@exemplo.com)
- 💼 LinkedIn: [seu-perfil](https://linkedin.com/in/seu-perfil)
- 🐙 GitHub: [@seu-usuario](https://github.com/seu-usuario)

### Projeto

- 🌐 **Repositório**: [github.com/seu-usuario/PantryManager](https://github.com/seu-usuario/PantryManager)
- 🐛 **Issues**: [Reportar problemas](https://github.com/seu-usuario/PantryManager/issues)
- 💡 **Sugestões**: [Discussões](https://github.com/seu-usuario/PantryManager/discussions)

### Suporte

- � **Documentação**: Consulte os arquivos `.md` do projeto
- 🤝 **Contribuições**: Pull requests são bem-vindos
- ❓ **Dúvidas**: Abra uma issue ou discussion no GitHub

---

## 🎯 Status do Projeto

> **�🚀 PROJETO FINALIZADO - VERSÃO 3.0.0 COM IA**

### ✅ Funcionalidades Implementadas

- **Completo sistema de automação** com OpenAI/ChatGPT
- **Scanner de códigos** com validação EAN única
- **Sistema de múltiplos lotes** com controle de validade
- **Exclusão múltipla** em todas as telas de gerenciamento
- **Arquitetura Clean/SOLID** aplicada completamente
- **Firebase integrado** com segurança por usuário
- **Interface moderna** em Jetpack Compose
- **Documentação técnica completa** para reprodução

### 📊 Métricas

- **100% Kotlin** - Linguagem moderna e segura
- **Clean Architecture** - Código manutenível e testável  
- **SOLID Principles** - Design orientado a objetos
- **Firebase Security** - Isolamento total por usuário
- **IA Integration** - Automação inteligente

---

<div align="center">

**🎉 O PantryManager está pronto para revolucionar o gerenciamento de despensa! 🎉**

*Desenvolvido com ❤️ como Trabalho de Conclusão de Curso*

**Tecnologias:** Android • Kotlin • Jetpack Compose • Firebase • OpenAI • Clean Architecture

</div>