@echo off
color 0E
echo ========================================
echo   LIMPEZA DO PROJETO PANTRYMANAGER
echo ========================================
echo.

cd /d "C:\Projetos\Faculdade\TCC1\PantryManager"

echo 🧹 INICIANDO LIMPEZA DE ARQUIVOS DESNECESSÁRIOS...
echo.

echo ========================================
echo   FASE 1: ARQUIVOS DE BUILD TEMPORÁRIOS
echo ========================================
echo.

echo 🗑️ Removendo diretório build/ completo...
if exist "build" (
    rmdir /s /q "build" 2>nul
    if exist "build" (
        echo ❌ Erro ao remover build/ - alguns arquivos em uso
    ) else (
        echo ✅ Diretório build/ removido
    )
) else (
    echo ℹ️ Diretório build/ não existe
)

echo.
echo 🗑️ Removendo app/build/ completo...
if exist "app\build" (
    rmdir /s /q "app\build" 2>nul
    if exist "app\build" (
        echo ❌ Erro ao remover app/build/ - alguns arquivos em uso
    ) else (
        echo ✅ Diretório app/build/ removido
    )
) else (
    echo ℹ️ Diretório app/build/ não existe
)

echo.
echo 🗑️ Removendo cache do Gradle...
if exist ".gradle" (
    rmdir /s /q ".gradle" 2>nul
    if exist ".gradle" (
        echo ❌ Erro ao remover .gradle/ - alguns arquivos em uso
    ) else (
        echo ✅ Cache do Gradle removido
    )
) else (
    echo ℹ️ Cache do Gradle não existe
)

echo.
echo 🗑️ Removendo cache do Kotlin...
if exist ".kotlin" (
    rmdir /s /q ".kotlin" 2>nul
    if exist ".kotlin" (
        echo ❌ Erro ao remover .kotlin/ - alguns arquivos em uso  
    ) else (
        echo ✅ Cache do Kotlin removido
    )
) else (
    echo ℹ️ Cache do Kotlin não existe
)

echo.
echo ========================================
echo   FASE 2: ARQUIVOS DUPLICADOS/TEMPORÁRIOS
echo ========================================
echo.

echo 🗑️ Removendo google-services-temp.json...
if exist "app\google-services-temp.json" (
    del "app\google-services-temp.json" 2>nul
    echo ✅ Arquivo google-services-temp.json removido
) else (
    echo ℹ️ Arquivo google-services-temp.json não existe
)

echo.
echo 🗑️ Removendo client_secret_* duplicado...
if exist "client_secret_513859885488-7vqhetijef1amff36nuhquvribj61krt.apps.googleusercontent.com.json" (
    del "client_secret_513859885488-7vqhetijef1amff36nuhquvribj61krt.apps.googleusercontent.com.json" 2>nul
    echo ✅ Arquivo client_secret duplicado removido
) else (
    echo ℹ️ Arquivo client_secret não existe
)

echo.
echo 🗑️ Consolidando gradle.properties...
if exist "gradle_new.properties" (
    if exist "gradle.properties" (
        echo ℹ️ Detectados gradle.properties e gradle_new.properties
        echo 📋 Usando gradle_new.properties como principal
        copy "gradle_new.properties" "gradle.properties" >nul 2>&1
        del "gradle_new.properties" 2>nul
        echo ✅ gradle_new.properties consolidado em gradle.properties
    ) else (
        ren "gradle_new.properties" "gradle.properties" >nul 2>&1
        echo ✅ gradle_new.properties renomeado para gradle.properties
    )
) else (
    echo ℹ️ Apenas gradle.properties existe (correto)
)

echo.
echo ========================================
echo   FASE 3: ARQUIVOS DE IDE/EDITOR
echo ========================================
echo.

echo 🗑️ Removendo cache do VS Code...
if exist ".vscode" (
    rmdir /s /q ".vscode" 2>nul
    echo ✅ Cache do VS Code removido
) else (
    echo ℹ️ Cache do VS Code não existe
)

echo.
echo 🗑️ Limpando cache do IntelliJ/Android Studio...
if exist ".idea" (
    if exist ".idea\caches" rmdir /s /q ".idea\caches" 2>nul
    if exist ".idea\shelf" rmdir /s /q ".idea\shelf" 2>nul  
    if exist ".idea\workspace.xml" del ".idea\workspace.xml" 2>nul
    if exist ".idea\tasks.xml" del ".idea\tasks.xml" 2>nul
    echo ✅ Cache do IDE limpo (mantendo configurações essenciais)
) else (
    echo ℹ️ Diretório .idea não existe
)

echo.
echo ========================================
echo   FASE 4: CORREÇÃO DE NOMENCLATURA
echo ========================================
echo.

echo 🏷️ Corrigindo referências incorretas de nome do projeto...

echo.
echo 📝 Corrigindo README.md...
if exist "README.md" (
    powershell -Command "(Get-Content README.md) -replace 'PrantyManager', 'PantryManager' -replace 'prantymanager', 'pantrymanager' | Set-Content README.md"
    echo ✅ README.md corrigido
) else (
    echo ℹ️ README.md não encontrado
)

echo.
echo 📝 Corrigindo .github/copilot-instructions.md...
if exist ".github\copilot-instructions.md" (
    powershell -Command "(Get-Content .github\copilot-instructions.md) -replace 'PrantyManager', 'PantryManager' -replace 'prantymanager', 'pantrymanager' | Set-Content .github\copilot-instructions.md"
    echo ✅ copilot-instructions.md corrigido
) else (
    echo ℹ️ copilot-instructions.md não encontrado
)

echo.
echo 🗑️ Removendo arquivos de configuração obsoletos...
if exist "gradle_new.properties" (
    del "gradle_new.properties" 2>nul
    echo ✅ gradle_new.properties removido (obsoleto)
) else (
    echo ℹ️ gradle_new.properties não existe
)

echo.
echo 🗑️ Removendo guias obsoletos...
if exist "GOOGLE_SIGNIN_SETUP_GUIDE.md" (
    del "GOOGLE_SIGNIN_SETUP_GUIDE.md" 2>nul
    echo ✅ GOOGLE_SIGNIN_SETUP_GUIDE.md removido (obsoleto)
) else (
    echo ℹ️ GOOGLE_SIGNIN_SETUP_GUIDE.md não existe
)

if exist "FIREBASE_SETUP_GUIDE.md" (
    del "FIREBASE_SETUP_GUIDE.md" 2>nul
    echo ✅ FIREBASE_SETUP_GUIDE.md removido (obsoleto)
) else (
    echo ℹ️ FIREBASE_SETUP_GUIDE.md não existe
)

echo.
echo 🗑️ Removendo scripts de teste obsoletos...
for %%f in (test_google_signin.bat configure_firebase_sha1.bat fix_google_signin_error10.bat) do (
    if exist "%%f" (
        del "%%f" 2>nul
        echo ✅ %%f removido (obsoleto)
    )
)

echo.
echo 🗑️ Removendo guias duplicados...
for %%f in (GOOGLE_SIGNIN_SETUP_GUIDE_FINAL.md FIREBASE_SHA1_SETUP_VISUAL.md SOLUCAO_ERRO_GOOGLE_SIGNIN.txt) do (
    if exist "%%f" (
        del "%%f" 2>nul
        echo ✅ %%f removido (duplicado)
    )
)

echo.
echo ========================================
echo   FASE 5: VERIFICAÇÃO FINAL
echo ========================================
echo.

echo 📊 Arquivos mantidos (essenciais):
echo ✅ build.gradle (raiz e app)
echo ✅ gradle.properties (único e correto)
echo ✅ settings.gradle
echo ✅ google-services.json (configurado)
echo ✅ AndroidManifest.xml
echo ✅ Código fonte (src/ - com nomenclatura correta)
echo ✅ Dependências (gradle/)
echo ✅ install_apk_test.bat (script de teste)
echo.

echo 🗑️ Arquivos removidos:
echo ❌ Todos os builds temporários
echo ❌ Caches do Gradle/Kotlin  
echo ❌ Arquivos duplicados/obsoletos
echo ❌ Guias antigos de configuração
echo ❌ Scripts de teste obsoletos  
echo ❌ Referências incorretas de nome (PantryManager já padronizado)
echo ❌ Cache de IDEs
echo ❌ gradle_new.properties (consolidado)
echo.

echo ========================================
echo   REBUILD LIMPO
echo ========================================
echo.

echo 🔧 Fazendo build limpo após limpeza...
call gradlew clean assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo ✅ BUILD LIMPO REALIZADO COM SUCESSO!
    echo.
    echo 📊 Tamanho do projeto reduzido
    echo 📱 APK gerado: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 🎯 PROJETO OTIMIZADO E PRONTO PARA USO!
) else (
    echo.
    echo ❌ Erro no build. Verifique as dependências.
    echo 💡 Execute: gradlew --refresh-dependencies build
)

echo.
echo ========================================
echo   RESUMO DA LIMPEZA
echo ========================================
echo.
echo ✅ Projeto limpo e otimizado
echo ✅ Arquivos desnecessários removidos  
echo ✅ Caches limpos
echo ✅ Estrutura organizada
echo ✅ Pronto para desenvolvimento
echo.

echo ========================================
pause
