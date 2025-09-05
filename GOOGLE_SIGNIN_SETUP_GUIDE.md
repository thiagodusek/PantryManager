# 🔐 Configuração Completa do Google Sign-In - PantryManager

## 📋 Passo a Passo para Ativar Login com Google

### **1. 🔥 Firebase Console - Configuração Básica**

1. **Acesse seu projeto Firebase**:
   - https://console.firebase.google.com/project/pantrymanager-79f09

2. **Ative o Google Sign-In**:
   - Vá em **Authentication** → **Sign-in method**
   - Clique em **Google**
   - **Ative** o provedor Google
   - **Configure o e-mail de suporte do projeto** (obrigatório)

### **2. 🎯 Google Cloud Console - Credenciais OAuth**

1. **Acesse o Google Cloud Console**:
   - https://console.cloud.google.com/apis/credentials?project=pantrymanager-79f09

2. **Criar credenciais OAuth 2.0**:
   - Clique em **+ CRIAR CREDENCIAIS** → **ID do cliente OAuth 2.0**
   - **Tipo de aplicativo**: Android
   - **Nome**: PantryManager Android
   - **Nome do pacote**: `com.pantrymanager`
   - **Impressão digital do certificado SHA-1**: `36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5`

3. **Criar credencial Web** (necessária para Firebase):
   - Clique em **+ CRIAR CREDENCIAIS** → **ID do cliente OAuth 2.0**
   - **Tipo de aplicativo**: Aplicativo da Web
   - **Nome**: PantryManager Web Client
   - **Copie o CLIENT_ID gerado** (será usado no passo 4)

### **3. 📱 Atualizar Arquivo google-services.json**

Após criar as credenciais OAuth, baixe o novo `google-services.json`:

1. **Firebase Console** → **⚙️ Configurações do Projeto**
2. **Seus aplicativos** → Android → **google-services.json**
3. **Substitua o arquivo atual** em `app/google-services.json`

### **4. 🔧 Configurar strings.xml**

Atualize o `default_web_client_id` com o CLIENT_ID da credencial Web:

```xml
<string name="default_web_client_id">SUA_WEB_CLIENT_ID_AQUI.apps.googleusercontent.com</string>
```

### **5. 📋 Verificar Configuração do AndroidManifest.xml**

Certifique-se de que o package name está correto:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    
    <!-- ... outras configurações ... -->
    
    <application
        android:name=".PantryManagerApplication"
        <!-- ... -->
```

### **6. 🧪 Testar a Configuração**

Execute os seguintes comandos para verificar:

```bash
# Rebuild do projeto
./gradlew clean assembleDebug

# Verificar SHA-1 do keystore
keytool -list -v -keystore debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### **7. 🔍 Logs de Debug**

Para debug, adicione logs no `AuthViewModel.kt`:

```kotlin
private fun handleGoogleSignIn(account: GoogleSignInAccount) {
    Log.d("GoogleSignIn", "Account: ${account.email}")
    Log.d("GoogleSignIn", "ID Token: ${account.idToken}")
    // ... resto do código
}
```

### **8. ⚠️ Troubleshooting Comum**

#### **Erro: "Developer Error" ou "Sign in failed"**
- ✅ Verifique se o SHA-1 está correto
- ✅ Confirme que o package name é `com.pantrymanager`
- ✅ Certifique-se de que baixou o `google-services.json` após configurar OAuth

#### **Erro: "The given sign-in provider is disabled"**
- ✅ Ative o Google Sign-In no Firebase Authentication
- ✅ Configure e-mail de suporte do projeto

#### **Erro: "Network error"**
- ✅ Teste em dispositivo real (não emulador sem Google Play Services)
- ✅ Verifique conexão com internet

### **9. 📊 Status Atual**

**✅ JÁ CONFIGURADO:**
- Package name correto: `com.pantrymanager`
- SHA-1 fingerprint: `36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5`
- Código do Google Sign-In implementado
- Firebase Auth configurado no código

**❓ PRECISA CONFIGURAR:**
- Credenciais OAuth no Google Cloud Console
- Ativar Google Sign-In no Firebase Auth
- Atualizar `google-services.json` com credenciais reais
- Configurar `default_web_client_id` no strings.xml

### **10. 🎯 Próximos Passos**

1. **Acesse o Firebase Console** e ative o Google Sign-In
2. **Configure as credenciais OAuth** no Google Cloud Console
3. **Baixe o novo `google-services.json`**
4. **Atualize o `default_web_client_id`**
5. **Rebuild e teste** o aplicativo

---

**🚀 Após seguir todos os passos, o login com Google funcionará perfeitamente!**

## 📞 Links Úteis

- **Firebase Console**: https://console.firebase.google.com/project/pantrymanager-79f09
- **Google Cloud Console**: https://console.cloud.google.com/apis/credentials?project=pantrymanager-79f09
- **Documentação Firebase Auth**: https://firebase.google.com/docs/auth/android/google-signin
