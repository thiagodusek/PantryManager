# 🔥 Guia Visual - Configurar SHA-1 no Firebase Console

## 🚀 CONFIGURAÇÃO RÁPIDA - PASSO A PASSO

### ✅ **Dados Prontos para Uso:**
- **SHA-1:** `36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5`
- **Package:** `com.pantrymanager`
- **CLIENT_ID:** `513859885488-7vqhetijef1amff36nuhquvribj61krt.apps.googleusercontent.com`

---

## 📝 **PASSO 1: ABRIR FIREBASE CONSOLE**

**Clique no link abaixo (abre automaticamente seu projeto):**

👉 **[FIREBASE - PANTRYMANAGER (CLIQUE AQUI)](https://console.firebase.google.com/project/pantrymanager-79f09/settings/general)**

---

## 📱 **PASSO 2: LOCALIZAR APP ANDROID**

Quando a página abrir, você verá:

```
🔽 Suas apps / Your apps
┌─────────────────────────────┐
│  📱 Android                │
│  PantryManager             │
│  com.pantrymanager         │
│                            │
│  [google-services.json]    │
└─────────────────────────────┘
```

**Se não aparecer, clique em "Adicionar app" → Android**

---

## 🔒 **PASSO 3: ADICIONAR SHA-1**

1. **Role para baixo** no card do Android até ver:
   ```
   SHA certificate fingerprints
   └── + Add fingerprint
   ```

2. **Clique em "+ Add fingerprint"**

3. **Cole EXATAMENTE esta SHA-1:**
   ```
   36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
   ```

4. **Clique em "Save"**

---

## 🔐 **PASSO 4: ATIVAR GOOGLE AUTHENTICATION**

1. **No menu lateral esquerdo**, clique em:
   ```
   🔐 Authentication
   ```

2. **Clique na aba "Sign-in method"**

3. **Procure "Google" na lista** e clique nele

4. **Ative o toggle** (deve ficar azul/verde)

5. **Configure:**
   - Email de suporte: seu-email@gmail.com
   - Nome público do projeto: PantryManager

6. **Clique em "Save"**

---

## 📄 **PASSO 5: BAIXAR GOOGLE-SERVICES.JSON**

1. **Volte para Project Settings** (⚙️ ícone no canto superior esquerdo)

2. **Clique em "General"**

3. **No card do Android**, clique em **"google-services.json"**

4. **Salve o arquivo** e substitua em:
   ```
   C:\Projetos\Faculdade\TCC1\PantryManager\app\google-services.json
   ```

---

## 🧪 **PASSO 6: TESTAR O APP**

1. **Rebuild o projeto:**
   ```powershell
   cd "C:\Projetos\Faculdade\TCC1\PantryManager"
   .\gradlew clean assembleDebug
   ```

2. **Instale no dispositivo:**
   ```powershell
   .\gradlew installDebug
   ```

3. **Teste:**
   - Abra o app PantryManager
   - Clique em "Entrar com Google"
   - Selecione sua conta Google
   - ✅ **LOGIN DEVE FUNCIONAR!**

---

## 🚨 **TROUBLESHOOTING**

### ❌ **Se der erro "DEVELOPER_ERROR":**
- Verifique se a SHA-1 foi adicionada corretamente
- Confirme o package name: `com.pantrymanager`

### ❌ **Se não aparecer a tela do Google:**
- Verifique se o Google Authentication foi ativado
- Confirme se baixou o google-services.json atualizado

### ❌ **Se der "Sign-in failed":**
- Verifique se o CLIENT_ID está correto no strings.xml
- Confirme se o projeto Firebase está ativo

---

## ✅ **CHECKLIST FINAL**

- [ ] SHA-1 adicionada no Firebase Console
- [ ] Google Authentication ativado
- [ ] google-services.json baixado e substituído
- [ ] App rebuilded e instalado
- [ ] Teste de login realizado

**🎉 Após completar todos os passos, o Google Sign-In funcionará perfeitamente!**
