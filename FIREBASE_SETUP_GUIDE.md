# 🔥 Guia de Configuração do Firebase - PantryManager

## ❗ Erro: "API Key not valid"

Este erro ocorre quando as chaves do Firebase/Google não estão configuradas corretamente. Siga este guia para resolver:

## 📋 Passo a Passo

### 1. **Configure o Firebase Console**

1. Acesse: https://console.firebase.google.com/
2. Selecione o projeto `pantrymanager-469816` (ou crie um novo)
3. Vá em **⚙️ Configurações do Projeto**

### 2. **Configure o Aplicativo Android**

1. Na aba **"Seus aplicativos"**
2. Clique em **"Adicionar app"** → **Android**
3. Configure:
   - **Nome do pacote**: `com.pantrymanager`
   - **Apelido do app**: `PantryManager`
   - **SHA-1**: (opcional para desenvolvimento)

### 3. **Baixe o arquivo google-services.json**

1. Após registrar o app, baixe o arquivo `google-services.json`
2. **SUBSTITUA** o arquivo atual em:
   ```
   app/google-services.json
   ```

### 4. **Configure as APIs necessárias**

No **Google Cloud Console** (https://console.cloud.google.com/):

#### Firebase Authentication:
1. **Firebase Console** → **Authentication** → **Sign-in method**
2. Habilite:
   - ✅ **Email/Password**
   - ✅ **Google Sign-In**

#### Google Maps API:
1. **Google Cloud Console** → **APIs & Services** → **Credentials**
2. Crie uma **API Key** para Google Maps
3. Atualize em `local.properties`:
   ```properties
   GOOGLE_MAPS_API_KEY=SUA_CHAVE_REAL_AQUI
   ```

### 5. **Verificar dependências (já configuradas)**

✅ Firebase Auth
✅ Firebase Firestore
✅ Google Sign-In
✅ Google Play Services

## 🔧 Arquivos que precisam ser atualizados:

### 📁 `app/google-services.json`
```json
{
  "project_info": {
    "project_number": "SEU_NUMERO_DO_PROJETO",
    "project_id": "SEU_PROJECT_ID",
    "storage_bucket": "SEU_PROJECT_ID.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "SEU_MOBILE_SDK_APP_ID",
        "android_client_info": {
          "package_name": "com.pantrymanager"
        }
      },
      "oauth_client": [...],
      "api_key": [
        {
          "current_key": "SUA_API_KEY_REAL"
        }
      ],
      // ... resto da configuração
    }
  ]
}
```

### 📁 `local.properties`
```properties
# Google Maps API Key
GOOGLE_MAPS_API_KEY=SUA_CHAVE_GOOGLE_MAPS_AQUI

# Firebase Web API Key (opcional)
FIREBASE_API_KEY=SUA_CHAVE_FIREBASE_AQUI
```

## 🚀 Testando a configuração

1. **Rebuild do projeto**:
   ```bash
   ./gradlew clean assembleDebug
   ```

2. **Verificar logs**:
   - Procure por logs de "AuthModule" no Logcat
   - Deve aparecer: "Firebase Auth inicializado com sucesso"

3. **Teste de login**:
   - Tente fazer login/registro
   - O erro "API Key not valid" não deve mais aparecer

## ⚠️ Troubleshooting

### Se o erro persistir:

1. **Verifique o package name**:
   - Deve ser exatamente: `com.pantrymanager`

2. **SHA-1 Fingerprint** (para produção):
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
   
3. **Limpe e rebuild**:
   ```bash
   ./gradlew clean
   ./gradlew assembleDebug
   ```

4. **Verifique as regras do Firestore**:
   ```javascript
   // Firestore Rules (temporária para desenvolvimento)
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /{document=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```

## 📱 Status do Projeto

✅ **Compilação**: Java 19 + Kotlin 2.0.21  
✅ **Jetpack Compose**: 1.7.3  
✅ **Dependências**: Todas atualizadas  
❓ **Firebase**: Necessita configuração das chaves reais  

## 📞 Próximos Passos

1. Configure o Firebase Console
2. Baixe o `google-services.json` real
3. Configure a Google Maps API Key
4. Rebuild o projeto
5. Teste o login/registro

**Após seguir este guia, o erro "API Key not valid" deve ser resolvido! 🎉**
