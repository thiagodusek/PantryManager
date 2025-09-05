@echo off
color 0C
echo ========================================
echo   DIAGNÓSTICO - GOOGLE SIGN-IN ERRO 10
echo ========================================
echo.

cd /d "C:\Projetos\Faculdade\TCC1\PantryManager"

echo ❌ ERRO DETECTADO: Google Sign-In falhou com código 10
echo.
echo 📋 Este erro geralmente indica problemas de configuração:
echo - SHA-1 não configurada no Firebase Console
echo - CLIENT_ID incorreto ou não autorizado  
echo - Credenciais OAuth não configuradas
echo.

echo ========================================
echo   CONFIGURAÇÃO ATUAL DETECTADA
echo ========================================
echo.

echo 🔑 CLIENT_ID atual: 191359104593-ickuagntm0cto1emai6ok1bdnc3rk64l.apps.googleusercontent.com
echo 📦 Package name: com.pantrymanager
echo 🔒 SHA-1 real: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo.

echo ========================================
echo   SOLUÇÃO - CONFIGURAR FIREBASE CONSOLE
echo ========================================
echo.

echo 🔥 PASSO 1: Acesse o Firebase Console
echo Link direto: https://console.firebase.google.com/project/pantrymanager-79f09/settings/general
echo.

echo 🔒 PASSO 2: Adicionar SHA-1 (CRÍTICO!)
echo 1. Procure pelo app Android "PantryManager"
echo 2. Na seção "SHA certificate fingerprints"
echo 3. Clique em "+ Add fingerprint"
echo 4. Cole EXATAMENTE: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo 5. Clique em "Save"
echo.

echo 🔐 PASSO 3: Ativar Google Authentication
echo 1. Menu lateral → Authentication → Sign-in method
echo 2. Clique em "Google"
echo 3. Ative o toggle
echo 4. Configure email de suporte
echo 5. Clique em "Save"
echo.

echo ☁️ PASSO 4: Google Cloud Console
echo 1. Acesse: https://console.cloud.google.com/apis/credentials?project=pantrymanager-79f09
echo 2. Clique na credencial Android existente
echo 3. Adicione a SHA-1: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo 4. Salve
echo.

echo 📄 PASSO 5: Atualizar google-services.json
echo 1. Volte ao Firebase Console
echo 2. Baixe o google-services.json atualizado
echo 3. Substitua o arquivo em app/google-services.json
echo.

echo ========================================
echo   TESTE AUTOMÁTICO
echo ========================================
echo.

set /p firebase_config=Você já configurou a SHA-1 no Firebase Console? (S/N): 

if /i "%firebase_config%"=="S" (
    echo.
    echo 🔨 Fazendo rebuild com novas configurações...
    call gradlew clean assembleDebug
    
    if %errorlevel% equ 0 (
        echo.
        echo ✅ Build realizado com sucesso!
        echo.
        set /p install_test=Deseja instalar e testar agora? (S/N): 
        
        if /i "!install_test!"=="S" (
            echo.
            echo 📲 Instalando APK...
            adb install -r "app\build\outputs\apk\debug\app-debug.apk"
            
            if !errorlevel! equ 0 (
                echo.
                echo ✅ INSTALAÇÃO CONCLUÍDA!
                echo.
                echo 🧪 TESTE O LOGIN COM GOOGLE AGORA:
                echo 1. Abra o PantryManager
                echo 2. Clique em "Entrar com Google"
                echo 3. Deve aparecer a tela de seleção de conta
                echo.
            ) else (
                echo.
                echo ❌ Erro na instalação. Conecte o dispositivo via USB.
            )
        )
    ) else (
        echo.
        echo ❌ Erro no build. Verifique os logs acima.
    )
) else (
    echo.
    echo 📋 CONFIGURE PRIMEIRO:
    echo 1. Acesse os links fornecidos acima
    echo 2. Configure a SHA-1 no Firebase Console
    echo 3. Ative o Google Authentication
    echo 4. Execute este script novamente
)

echo.
echo ========================================
echo   VERIFICAÇÃO FINAL
echo ========================================
echo.
echo ✅ Checklist para o Google Sign-In funcionar:
echo [ ] SHA-1 adicionada no Firebase Console
echo [ ] Google Authentication ativado
echo [ ] Credencial OAuth configurada no Google Cloud
echo [ ] google-services.json atualizado
echo [ ] App reinstalado
echo.
echo 💡 DICA: Se ainda não funcionar, aguarde até 10 minutos
echo    para as configurações se propagarem nos servidores Google.
echo.

echo ========================================
pause
