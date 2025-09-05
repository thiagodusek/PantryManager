@echo off
color 0A
echo ========================================
echo   GOOGLE SIGN-IN - CONFIGURACAO FIREBASE
echo ========================================
echo.

echo ✅ DADOS PARA CONFIGURACAO:
echo.
echo 🔒 SHA-1: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo 📦 Package: com.pantrymanager
echo 🔑 CLIENT_ID: 513859885488-7vqhetijef1amff36nuhquvribj61krt.apps.googleusercontent.com
echo.

echo 📋 LINKS IMPORTANTES:
echo.
echo 🔥 Firebase Console (SHA-1):
echo https://console.firebase.google.com/project/pantrymanager-79f09/settings/general
echo.
echo 🔐 Firebase Authentication:
echo https://console.firebase.google.com/project/pantrymanager-79f09/authentication/providers
echo.

echo ========================================
echo   PASSO A PASSO
echo ========================================
echo.

echo 1️⃣ Abra o link do Firebase Console acima
echo 2️⃣ Adicione a SHA-1 no app Android
echo 3️⃣ Ative Google Authentication
echo 4️⃣ Baixe o google-services.json atualizado
echo 5️⃣ Substitua o arquivo em app/google-services.json
echo 6️⃣ Execute este script novamente para testar
echo.

echo ========================================
echo   TESTE AUTOMATICO
echo ========================================
echo.

set /p choice=Voce ja configurou o Firebase? (S/N): 

if /i "%choice%"=="S" (
    echo.
    echo 🔨 Fazendo rebuild do projeto...
    cd /d "C:\Projetos\Faculdade\TCC1\PantryManager"
    call gradlew clean assembleDebug
    
    if %errorlevel% equ 0 (
        echo.
        echo ✅ Build realizado com sucesso!
        echo.
        echo 📱 Para testar:
        echo 1. Instale o APK no dispositivo
        echo 2. Abra o app PantryManager
        echo 3. Clique em "Entrar com Google"
        echo 4. Selecione sua conta
        echo.
        echo 📍 APK Gerado: app\build\outputs\apk\debug\app-debug.apk
        echo.
    ) else (
        echo.
        echo ❌ Erro no build! Verifique os logs acima.
    )
) else (
    echo.
    echo 📝 Configure primeiro o Firebase usando os links acima!
    echo Depois execute este script novamente.
)

echo.
echo ========================================
pause
