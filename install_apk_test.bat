@echo off
color 0A
echo ========================================
echo   PANTRYMANAGER - APK TESTE GOOGLE SIGNIN
echo ========================================
echo.

cd /d "C:\Projetos\Faculdade\TCC1\PantryManager"

echo ✅ APK DE TESTE PRONTO!
echo.
echo 📱 Localização do APK:
echo %CD%\app\build\outputs\apk\debug\app-debug.apk
echo.

echo 📊 Detalhes do APK:
dir "app\build\outputs\apk\debug\app-debug.apk" | findstr "app-debug.apk"
echo.

echo ========================================
echo   CONFIGURAÇÕES APLICADAS
echo ========================================
echo.
echo 🔑 CLIENT_ID: 191359104593-ickuagntm0cto1emai6ok1bdnc3rk64l.apps.googleusercontent.com
echo 📦 Package: com.pantrymanager
echo 🔒 SHA-1: 364cc4f8b3b98e2b0c6d1a2308bb91c0d080faf5
echo ✅ Botão "Entrar com Google" implementado
echo.

echo ========================================
echo   OPÇÕES DE INSTALAÇÃO
echo ========================================
echo.

set /p choice=Deseja instalar no dispositivo conectado? (S/N): 

if /i "%choice%"=="S" (
    echo.
    echo 🔌 Verificando dispositivos conectados...
    adb devices
    echo.
    
    echo 📲 Instalando APK...
    adb install -r "app\build\outputs\apk\debug\app-debug.apk"
    
    if %errorlevel% equ 0 (
        echo.
        echo ✅ INSTALAÇÃO CONCLUÍDA!
        echo.
        echo 🧪 COMO TESTAR:
        echo 1. Abra o app "PantryManager" no dispositivo
        echo 2. Na tela de login, clique em "Entrar com Google"
        echo 3. Selecione sua conta Google
        echo 4. O login deve funcionar se o Firebase estiver configurado
        echo.
    ) else (
        echo.
        echo ❌ Erro na instalação! Verifique se:
        echo - Dispositivo está conectado
        echo - USB Debugging está ativo
        echo - Drivers ADB estão instalados
    )
) else (
    echo.
    echo 📋 INSTALAÇÃO MANUAL:
    echo 1. Copie o APK para o dispositivo:
    echo    %CD%\app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 2. No dispositivo Android:
    echo    - Ative "Fontes desconhecidas" nas configurações
    echo    - Abra o APK e instale
    echo    - Teste o login com Google
)

echo.
echo ========================================
echo   TROUBLESHOOTING
echo ========================================
echo.
echo ❌ Se o Google Sign-In falhar com ERRO 10:
echo 1. CAUSA: SHA-1 não configurada no Firebase Console
echo 2. SOLUÇÃO: Execute fix_google_signin_error10.bat
echo 3. Configure a SHA-1: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo.
echo ❌ Outros problemas comuns:
echo - Verifique se o Firebase Auth está ativado
echo - Confirme se baixou o google-services.json atualizado
echo - Teste em dispositivo real (não emulador sem Google Play)
echo.
echo 🔗 Links importantes:
echo Firebase: https://console.firebase.google.com/project/pantrymanager-79f09
echo Google Cloud: https://console.cloud.google.com/apis/credentials
echo.

echo ========================================
pause
