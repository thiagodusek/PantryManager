# 🛒 PantryManager

**Sistema Completo de Gerenciamento de Despensa e Compras**

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com)
[![Java 19](https://img.shields.io/badge/Java-19-orange?style=for-the-badge&logo=java&logoColor=white)](https://openjdk.org/projects/jdk/19/)
[![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 🚀 **VERSÃO 2.0.0 - SISTEMA COMPLETO** 

### **🎯 FUNCIONALIDADES PRINCIPAIS**

## 📱 **Sistema de Scanner e Cadastro Inteligente**

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
- ✅ **Hilt**: Injeção de dependência

### **🛠️ Bibliotecas e Ferramentas**

- ✅ **Retrofit**: Cliente HTTP para APIs
- ✅ **Gson**: Serialização JSON
- ✅ **Coroutines**: Programação assíncrona
- ✅ **StateFlow**: Gerenciamento de estado reativo

## 📋 **Fluxos de Uso**

### **🔍 Cadastro via Scanner**

```
Tela Produto → Scanner EAN → Busca Online → 
Preenchimento Automático → Validação Única → 
Criação Entidades → Salvar Firebase
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
3. Sincronize dependências do Gradle
4. Execute no dispositivo/emulador

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
- [x] CRUD completo para todas entidades
- [x] Interface com exclusão múltipla
- [x] Dados padrão pré-cadastrados
- [x] Arquitetura Clean/SOLID
- [x] Firebase integrado completamente

### **🎯 Diferenciais**

- [x] **Multi-tenant**: Isolamento por usuário
- [x] **Validação EAN**: Única por usuário  
- [x] **Import Inteligente**: Cupom fiscal completo
- [x] **Scanner Nativo**: CameraX + ML Kit
- [x] **Auto-complete**: Criação de entidades relacionadas
- [x] **Interface Moderna**: Material Design 3
- [x] **Arquitetura Sólida**: Clean + SOLID

## 📄 **Documentação Completa**

- 📋 [FUNCIONALIDADES_IMPLEMENTADAS.md](FUNCIONALIDADES_IMPLEMENTADAS.md) - Lista completa de funcionalidades
- 🚀 [FIREBASE_SCANNER_INTEGRATION.md](FIREBASE_SCANNER_INTEGRATION.md) - Integração Firebase e Scanner
- 📝 [PROMPT_PROJETO.md](PROMPT_PROJETO.md) - Especificações técnicas
- ✨ [QUALITY.md](QUALITY.md) - Padrões de qualidade e boas práticas

## 🎯 **Status do Projeto**

**🚀 PROJETO FINALIZADO E FUNCIONAL**

- ✅ **Todas as funcionalidades** solicitadas implementadas
- ✅ **Build successful** - APK gerado com sucesso  
- ✅ **Arquitetura Clean/SOLID** completamente aplicada
- ✅ **Firebase integrado** com segurança por usuário
- ✅ **Scanner funcional** com validações
- ✅ **Importação de cupom** completa
- ✅ **Interface moderna** em Jetpack Compose
- ✅ **Documentação completa** atualizada

**O PantryManager está pronto para uso e atende todos os requisitos especificados! 🎉**

---

**Desenvolvido com ❤️ usando Clean Architecture e SOLID principles**