# 📱 APK de Teste - PantryManager

## ✅ APK Pronto para Teste!

**Localização do APK:**
```
C:\Projetos\Faculdade\TCC1\PantryManager\app\build\outputs\apk\debug\app-debug.apk
```

**Tamanho:** ~26 MB  
**Data de Build:** 05/09/2025 00:00:10  
**Versão:** Debug  

---

## 🔧 Configurações Aplicadas

| Item | Valor |
|------|-------|
| 🔑 **CLIENT_ID** | `191359104593-ickuagntm0cto1emai6ok1bdnc3rk64l.apps.googleusercontent.com` |
| 📦 **Package Name** | `com.pantrymanager` |
| 🔒 **SHA-1** | `364cc4f8b3b98e2b0c6d1a2308bb91c0d080faf5` |
| 🎯 **Google Button** | ✅ Implementado na tela de login |
| 🏗️ **Build Status** | ✅ Sucesso (42 tasks executadas) |

---

## 📲 Como Instalar

### **Opção 1: Via ADB (Recomendado)**
```powershell
# Execute o script automático
.\install_apk_test.bat

# OU manualmente:
adb install -r "app\build\outputs\apk\debug\app-debug.apk"
```

### **Opção 2: Instalação Manual**
1. **Copie o APK** para o dispositivo Android
2. **Ative "Fontes desconhecidas"** nas configurações
3. **Abra o arquivo APK** no dispositivo
4. **Instale** o aplicativo

---

## 🧪 Como Testar o Google Sign-In

### **Passo 1: Abrir o App**
- Localize o app **"PantryManager"** no dispositivo
- Abra o aplicativo

### **Passo 2: Testar Login**
- Na tela de login, clique em **"Entrar com Google"**
- Uma tela do Google deve aparecer
- Selecione sua conta Google
- O login deve ser realizado

### **Passo 3: Verificar Funcionamento**
- ✅ **Sucesso:** Login realizado, acesso ao app
- ❌ **Erro:** Verifique as configurações do Firebase

---

## 🚨 Troubleshooting

### **Google Sign-In não aparece:**
- Verifique conexão com internet
- Confirme se o Firebase Auth está ativado
- Verifique se a SHA-1 está configurada

### **Erro "DEVELOPER_ERROR":**
- SHA-1 não configurada no Firebase Console
- Package name incorreto

### **Erro "Sign-in failed":**
- CLIENT_ID incorreto
- Credenciais OAuth não configuradas

---

## 📋 Checklist de Teste

- [ ] APK instalado com sucesso
- [ ] App abre normalmente
- [ ] Tela de login carrega
- [ ] Botão "Entrar com Google" visível
- [ ] Clique no botão abre seletor do Google
- [ ] Login com conta Google funciona
- [ ] Acesso ao app após login

---

## 🔗 Links Úteis

- **Firebase Console:** https://console.firebase.google.com/project/pantrymanager-79f09
- **Google Cloud Console:** https://console.cloud.google.com/apis/credentials
- **Guia de Configuração:** `FIREBASE_SHA1_SETUP_VISUAL.md`

---

## 📞 Próximos Passos

1. **Instale e teste o APK**
2. **Se funcionar:** ✅ Configuração está correta!
3. **Se não funcionar:** Configure Firebase seguindo o guia
4. **Rebuild:** Gere novo APK após configurar Firebase

**🚀 O APK está pronto para teste! Basta instalar e verificar o Google Sign-In.**
