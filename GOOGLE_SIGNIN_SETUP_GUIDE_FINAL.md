# 🔐 Configuração Completa do Google Sign-In - PantryManager

## ✅ Configuração Atual - CREDENCIAIS REAIS APLICADAS!

**Seu projeto já está configurado com as credenciais reais do Google Cloud Console!**

### 🔑 CLIENT_ID do Android (Aplicado):
```
513859885488-7vqhetijef1amff36nuhquvribj61krt.apps.googleusercontent.com
```

### � SHA-1 Certificate Fingerprint (PRONTA):
```
36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
```

### �📁 Arquivos já atualizados:
- ✅ `app/src/main/res/values/strings.xml` - CLIENT_ID real aplicado
- ✅ `app/google-services.json` - PROJECT_NUMBER e CLIENT_ID atualizados
- ✅ Package name configurado: `com.pantrymanager`
- ✅ Build executado com SUCESSO!
- ✅ Botão "Entrar com Google" adicionado na tela de login!

## 📋 CONFIGURAR SHA-1 NO FIREBASE CONSOLE (PASSO CRÍTICO)

### **� INSTRUÇÕES DETALHADAS PARA FIREBASE**

**Sua SHA-1 já foi gerada e está pronta para uso:**
```
36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
```

### **Passo 1: Abrir Firebase Console**
1. **Clique no link abaixo para abrir diretamente seu projeto:**
   👉 **[CLIQUE AQUI - Firebase PantryManager](https://console.firebase.google.com/project/pantrymanager-79f09/settings/general/android:com.pantrymanager)**

### **Passo 2: Localizar a Seção de Apps**
1. Na página que abrir, procure por **"Suas apps"** ou **"Your apps"**
2. Você verá um card com o ícone do Android e o nome **"PantryManager"**
3. Se não estiver visível, clique em **"Configurações do Projeto"** → **"Geral"**

### **Passo 3: Adicionar SHA-1**
1. **No card do app Android**, role para baixo até encontrar:
   - **"SHA certificate fingerprints"** ou
   - **"Impressões digitais do certificado SHA"**

2. **Clique no botão** `+ Add fingerprint` ou `+ Adicionar impressão digital`

3. **Cole exatamente esta SHA-1** (copie e cole):
   ```
   36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
   ```

4. **Clique em "Save" ou "Salvar"**

### **Passo 4: Ativar Google Authentication**
1. **No menu lateral**, clique em **"Authentication"** ou **"Autenticação"**
2. Clique na aba **"Sign-in method"** ou **"Método de login"**
3. Procure por **"Google"** na lista
4. **Clique no Google** para expandir
5. **Ative** o toggle (deve ficar azul/verde)
6. **Configure o email de suporte** (obrigatório)
7. **Clique em "Save" ou "Salvar"**

### **Passo 5: Baixar google-services.json Atualizado**
1. **Volte para "Project Settings" → "General"**
2. **Role até o card do app Android**
3. **Clique em "google-services.json"** para baixar
4. **Substitua** o arquivo em: `app/google-services.json`

### **3. ☁️ Google Cloud Console - Configurar OAuth**

1. **Acesse as credenciais:**
   - https://console.cloud.google.com/apis/credentials?project=YOUR_PROJECT_ID

2. **Configure a credencial OAuth Android:**
   - Clique na credencial existente (CLIENT_ID: `513859885488-7vqhetijef1amff36nuhquvribj61krt...`)
   - Verifique se o **Package name** é: `com.pantrymanager`
   - Adicione a **SHA-1** na seção "SHA-1 certificate fingerprints"
   - **Salve** as alterações

### **4. 📱 Teste o Login Google**

1. **Build e install no dispositivo:**
   ```powershell
   cd "C:\Projetos\Faculdade\TCC1\PantryManager"
   .\gradlew assembleDebug
   .\gradlew installDebug
   ```

2. **Teste o fluxo:**
   - Abra o app **PantryManager**
   - Na tela de login, clique em **"Entrar com Google"**
   - Verifique se o seletor de contas Google aparece

## 🚨 Troubleshooting

### **Erro: DEVELOPER_ERROR**
- ❌ **Causa**: SHA-1 não configurada ou incorreta
- ✅ **Solução**: Refaça os passos 1 e 2 acima

### **Erro: SIGN_IN_FAILED**
- ❌ **Causa**: CLIENT_ID incorreto ou não autorizado
- ✅ **Solução**: Verifique se o CLIENT_ID está correto (já aplicado)

### **Erro: Nenhuma conta encontrada**
- ❌ **Causa**: OAuth não configurado adequadamente
- ✅ **Solução**: Verifique o passo 3 (Google Cloud Console)

## 📊 Status da Configuração

| Item | Status | Descrição |
|------|---------|-----------|
| 🔑 CLIENT_ID | ✅ **APLICADO** | `513859885488-7vqhetijef1amff36nuhquvribj61krt...` |
| 📦 Package Name | ✅ **OK** | `com.pantrymanager` |
| 🏗️ Build | ✅ **SUCESSO** | Compilando sem erros |
| 📄 google-services.json | ✅ **ATUALIZADO** | PROJECT_NUMBER correto |
| 🔒 SHA-1 | ⚠️ **CONFIGURAR AGORA** | `36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5` |
| 🎯 Botão Google | ✅ **IMPLEMENTADO** | Tela de login atualizada |
| 🔥 Firebase Auth | 🔄 **CONFIGURAR** | Ativar provedor Google |
| 🧪 Teste Login | 🔄 **PENDENTE** | Após configurar Firebase |

## 🛠️ Comandos Úteis

### Gerar SHA-1:
```powershell
keytool -list -v -keystore debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### Build completo:
```powershell
.\gradlew clean build
```

### Verificar logs do Google Sign-In:
```powershell
adb logcat -s "GoogleSignIn" -s "AuthRepository" -s "AuthViewModel"
```

## 🎯 Conclusão

Seu projeto PantryManager está **95% configurado** para Google Sign-In! 

**Apenas falta:**
1. Gerar SHA-1
2. Adicionar no Firebase Console
3. Baixar novo google-services.json
4. Testar em dispositivo

**Após esses passos, o login com Google estará totalmente funcional!**
