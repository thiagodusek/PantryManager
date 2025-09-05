@echo off
echo ============================================
echo   🚀 PantryManager - Configuracao Rapida
echo ============================================
echo.

:: Verificar se o arquivo google-services.json existe
if not exist "app\google-services.json" (
    echo ❌ ERRO: arquivo google-services.json nao encontrado!
    echo.
    echo 📋 ACOES NECESSARIAS:
    echo 1. Baixe o google-services.json do Firebase
    echo 2. Coloque o arquivo em app\google-services.json
    echo 3. Execute este script novamente
    echo.
    echo 📖 Guia completo: CONFIGURACAO_LOGIN_GOOGLE_COMPLETO.md
    pause
    exit /b 1
)

echo ✅ Arquivo google-services.json encontrado!

:: Verificar se o default_web_client_id foi configurado
findstr /C:"123456789012-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" "app\src\main\res\values\strings.xml" >nul
if %errorlevel%==0 (
    echo ⚠️  ATENCAO: default_web_client_id ainda nao foi configurado!
    echo.
    echo 📋 ACOES NECESSARIAS:
    echo 1. Abra app\src\main\res\values\strings.xml
    echo 2. Substitua o default_web_client_id pelo CLIENT_ID do Google Cloud Console
    echo 3. Execute este script novamente
    echo.
    echo 📖 Guia completo: CONFIGURACAO_LOGIN_GOOGLE_COMPLETO.md
    pause
)

echo.
echo ============================================
echo   🔨 Iniciando Build do Projeto
echo ============================================

:: Limpar build anterior
echo 🧹 Limpando build anterior...
call gradlew clean
if %errorlevel% neq 0 (
    echo ❌ Erro na limpeza do projeto!
    pause
    exit /b 1
)

:: Build debug
echo 🔨 Fazendo build debug...
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo ❌ Erro no build debug!
    echo.
    echo 📋 POSSIVEISCAUSAS:
    echo - google-services.json incorreto
    echo - default_web_client_id nao configurado
    echo - Dependencias desatualizadas
    echo.
    echo 📖 Consulte: CONFIGURACAO_LOGIN_GOOGLE_COMPLETO.md
    pause
    exit /b 1
)

echo.
echo ============================================
echo   ✅ Build Concluido com Sucesso!
echo ============================================
echo.

:: Verificar se existe dispositivo conectado
echo 📱 Verificando dispositivos conectados...
adb devices | findstr /R "device$" >nul
if %errorlevel%==0 (
    echo ✅ Dispositivo encontrado!
    echo.
    set /p install="🚀 Deseja instalar o app no dispositivo? (s/n): "
    if /i "%install%"=="s" (
        echo 📱 Instalando no dispositivo...
        call gradlew installDebug
        if %errorlevel%==0 (
            echo ✅ App instalado com sucesso!
            echo.
            echo 🎯 TESTE O LOGIN:
            echo 1. Abra o PantryManager no dispositivo
            echo 2. Clique em "Entrar com Google"
            echo 3. Verifique se aparece sua conta Google
        ) else (
            echo ❌ Erro na instalacao!
        )
    )
) else (
    echo ⚠️  Nenhum dispositivo Android conectado
    echo.
    echo 📋 OPCOES:
    echo 1. Conecte um dispositivo via USB e ative Depuracao USB
    echo 2. Inicie um emulador Android
    echo 3. Instale manualmente o APK: app\build\outputs\apk\debug\app-debug.apk
)

echo.
echo ============================================
echo   📁 Arquivos Gerados
echo ============================================
echo.
echo ✅ APK Debug: app\build\outputs\apk\debug\app-debug.apk

:: Verificar se foi gerado APK de release
if exist "app\build\outputs\apk\release\app-release-unsigned.apk" (
    echo ✅ APK Release: app\build\outputs\apk\release\app-release-unsigned.apk
)

echo.
echo ============================================
echo   🎯 Proximo Passo
echo ============================================
echo.
echo 🔐 CONFIGURAR LOGIN COM GOOGLE:
echo.
echo 1. 🔥 Firebase Console - Ativar Google Sign-In
echo    https://console.firebase.google.com/
echo.
echo 2. 🔧 Google Cloud Console - Criar credenciais OAuth
echo    https://console.cloud.google.com/apis/credentials
echo.
echo 3. 📱 Baixar novo google-services.json
echo.
echo 4. 🔄 Atualizar default_web_client_id em strings.xml
echo.
echo 📖 Guia completo: CONFIGURACAO_LOGIN_GOOGLE_COMPLETO.md
echo.

pause
