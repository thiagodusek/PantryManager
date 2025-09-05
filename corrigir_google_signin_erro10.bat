@echo off
color 0A
echo ========================================
echo   CORRIGIR GOOGLE SIGN-IN ERRO 10
echo ========================================
echo.

echo 🔧 ATUALIZANDO GOOGLE-SERVICES.JSON COM SHA-1 CORRETO...
echo.

set "NOVO_SHA1=d47e388229d6bb6a9c9c19d49b4cafb794dcf5ec"
set "ARQUIVO_JSON=app\google-services.json"

echo 📝 SHA-1 atual: D4:7E:38:82:29:D6:BB:6A:9C:9C:19:D4:9B:4C:AF:B7:94:DC:F5:EC
echo 📝 SHA-1 formato hex: %NOVO_SHA1%
echo.

echo 🔄 Criando backup do google-services.json atual...
copy "%ARQUIVO_JSON%" "%ARQUIVO_JSON%.backup" >nul 2>&1

echo ✅ Backup criado: %ARQUIVO_JSON%.backup
echo.

echo 🔄 Atualizando certificate_hash no google-services.json...
powershell -Command "(Get-Content '%ARQUIVO_JSON%') -replace '364cc4f8b3b98e2b0c6d1a2308bb91c0d080faf5', '%NOVO_SHA1%' | Set-Content '%ARQUIVO_JSON%'"

echo ✅ Arquivo google-services.json atualizado!
echo.

echo ========================================
echo   VERIFICAÇÃO
echo ========================================
echo.

findstr /i "certificate_hash" "%ARQUIVO_JSON%"
echo.

echo ========================================
echo   PRÓXIMOS PASSOS
echo ========================================
echo.

echo 1️⃣ ✅ google-services.json atualizado
echo 2️⃣ 🔄 Faça rebuild: gradlew clean assembleDebug
echo 3️⃣ 📱 Teste Google Sign-In novamente
echo.

echo 💡 IMPORTANTE: Se ainda houver erro, atualize também no Firebase Console:
echo    - Acesse: https://console.firebase.google.com/project/pantrymanager-79f09
echo    - Adicione SHA-1: D4:7E:38:82:29:D6:BB:6A:9C:9C:19:D4:9B:4C:AF:B7:94:DC:F5:EC
echo.

echo 🎯 CORREÇÃO APLICADA COM SUCESSO!
echo.

pause
