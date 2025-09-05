# 🚀 Configuração Completa do Login com Google - PantryManager

## ⚠️ Status Atual
O projeto está configurado com credenciais temporárias para desenvolvimento. Para usar o login com Google com SUA conta, siga este guia.

## 📋 Passo a Passo Completo

### **Passo 1: 🔥 Configurar Firebase Console**

1. **Acesse Firebase Console**:
   ```
   https://console.firebase.google.com/
   ```

2. **Criar/Selecionar Projeto**:
   - Se não tiver projeto: Clique em **"Adicionar projeto"**
   - Nome do projeto: `PantryManager` (ou o nome que preferir)
   - Se já tiver: Selecione seu projeto

3. **Configurar Authentication**:
   - No menu lateral: **Authentication** → **Get started** (se for a primeira vez)
   - Vá em **Sign-in method**
   - Clique em **Google**
   - **Ative** o provedor Google
   - **Configure o e-mail de suporte** (seu e-mail)
   - Salve

### **Passo 2: 📱 Adicionar App Android**

1. **Na página inicial do projeto Firebase**:
   - Clique no ícone **Android** para adicionar app
   - **Nome do pacote Android**: `com.pantrymanager` ⚠️ (IMPORTANTE: use exatamente este nome)
   - **Apelido do app**: `PantryManager`
   - **SHA-1** (opcional para desenvolvimento): deixe em branco por enquanto

2. **Baixar google-services.json**:
   - Firebase irá gerar o arquivo
   - **BAIXE** o arquivo `google-services.json`
   - **SUBSTITUA** o arquivo em: `app/google-services.json`

### **Passo 3: 🔧 Google Cloud Console - Credenciais OAuth**

1. **Acesse Google Cloud Console**:
   ```
   https://console.cloud.google.com/apis/credentials
   ```
   - Selecione o projeto que o Firebase criou (mesmo nome)

2. **Criar Credencial Android**:
   - Clique **+ CRIAR CREDENCIAIS** → **ID do cliente OAuth 2.0**
   - **Tipo de aplicativo**: Android
   - **Nome**: PantryManager Android
   - **Nome do pacote**: `com.pantrymanager`
   - **SHA-1**: Para desenvolvimento, use esta SHA-1:
   ```
   364CC4F8B3B98E2B0C6D1A2308BB91C0D080FAF5
   ```
   - Clique **Criar**

3. **Criar Credencial Web** (OBRIGATÓRIO para Firebase):
   - Clique **+ CRIAR CREDENCIAIS** → **ID do cliente OAuth 2.0**
   - **Tipo de aplicativo**: Aplicativo da Web
   - **Nome**: PantryManager Web
   - **Clique Criar**
   - **⚠️ COPIE o CLIENT_ID gerado** (você vai precisar no próximo passo)

### **Passo 4: 🔄 Atualizar Arquivos do Projeto**

1. **Baixar novo google-services.json**:
   - Volte ao Firebase Console
   - **Configurações do projeto** → **Seus aplicativos**
   - Clique no app Android → **Baixar google-services.json**
   - **Substitua** o arquivo em `app/google-services.json`

2. **Atualizar CLIENT_ID**:
   - Abra: `app/src/main/res/values/strings.xml`
   - Encontre a linha:
   ```xml
   <string name="default_web_client_id">123456789012-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com</string>
   ```
   - **Substitua** pelo CLIENT_ID da credencial Web que você copiou no Passo 3

### **Passo 5: 🔨 Build e Teste**

1. **Fazer Build**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Instalar no dispositivo/emulador**:
   ```bash
   ./gradlew installDebug
   ```

3. **Testar Login**:
   - Abra o app
   - Clique em "Entrar com Google"
   - Deve aparecer a tela de login do Google com sua conta

## 🐛 Resolução de Problemas

### **Erro: "API Key not valid"**
- Verifique se baixou o `google-services.json` correto
- Certifique-se que o package name está correto (`com.pantrymanager`)

### **Erro: "OAuth client not found"**
- Verifique se criou a credencial Web no Google Cloud Console
- Certifique-se que o `default_web_client_id` está correto no strings.xml

### **Erro: "SHA-1 fingerprint"**
- Para desenvolvimento, use a SHA-1 fornecida acima
- Para produção, gere a SHA-1 do seu keystore de assinatura

### **Login não funciona**
- Verifique se o Authentication está ativado no Firebase
- Confirme que o Google Sign-In está habilitado
- Verifique se tem conexão com internet

## 📝 Arquivos que Precisa Atualizar

1. ✅ `app/google-services.json` - Baixar do Firebase
2. ✅ `app/src/main/res/values/strings.xml` - Atualizar default_web_client_id

## 🎯 Resultado Esperado

Após seguir todos os passos:
- ✅ Login com Google funcionando
- ✅ Integração Firebase ativa
- ✅ App pronto para desenvolvimento/testes
- ✅ Credenciais configuradas corretamente

## ⚡ Comandos Rápidos

```bash
# Build do projeto
./gradlew assembleDebug

# Instalar no dispositivo
./gradlew installDebug

# Gerar APK de release
./gradlew assembleRelease

# Limpar build (se houver problemas)
./gradlew clean
```

---

## 📞 Precisa de Ajuda?

Se encontrar algum problema:
1. Verifique se seguiu todos os passos na ordem
2. Confirme se os package names estão corretos
3. Certifique-se que baixou o google-services.json atualizado
4. Verifique se tem internet e as permissões corretas

**O projeto já está preparado com todo o código necessário. Você só precisa configurar as credenciais!**
