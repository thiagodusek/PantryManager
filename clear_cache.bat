@echo off
echo ============================================
echo LIMPEZA COMPLETA DO CACHE - PANTRYMANAGER
echo ============================================

echo [INFO] Parando processos do VS Code...
taskkill /f /im "Code.exe" >nul 2>&1

echo [INFO] Limpando cache do Gradle...
if exist .gradle rmdir /s /q .gradle
if exist build rmdir /s /q build
if exist app\build rmdir /s /q app\build

echo [INFO] Limpando cache do VS Code...
if exist .vscode\.ropeproject rmdir /s /q .vscode\.ropeproject
if exist .vscode\extensions rmdir /s /q .vscode\extensions

echo [INFO] Limpando cache do Android Studio...
if exist .idea rmdir /s /q .idea
if exist *.iml del /q *.iml
if exist app\*.iml del /q app\*.iml

echo [INFO] Limpando arquivos temporarios...
if exist *.tmp del /q *.tmp
if exist *.log del /q *.log

echo [INFO] Limpando cache do Git (se necessario)...
git clean -fd >nul 2>&1

echo.
echo ============================================
echo [SUCESSO] Cache limpo completamente!
echo ============================================
echo.
echo IMPORTANTE:
echo 1. Feche TODOS os editores (VS Code, Android Studio)
echo 2. Execute gradlew clean
echo 3. Execute gradlew assembleDebug
echo 4. Reabra o projeto
echo.
pause
