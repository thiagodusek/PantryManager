@echo off
echo ==========================================
echo DIAGNOSTICO COMPLETO - BUILD KSP
echo ==========================================

echo.
echo 1. Verificando Java...
echo JAVA_HOME: %JAVA_HOME%
java -version

echo.
echo 2. Verificando Gradle...
call gradlew --version

echo.
echo 3. Verificando estrutura do projeto...
echo Diretorio main/java:
dir "app\src\main\java" /b /s | findstr "\.kt$" | head -10

echo.
echo Diretorio main/kotlin:
if exist "app\src\main\kotlin" (
    dir "app\src\main\kotlin" /b /s 2>nul || echo "Vazio"
) else (
    echo "Nao existe"
)

echo.
echo 4. Verificando arquivos gerados pelo KSP...
if exist "app\build\generated\ksp" (
    echo "Diretorio KSP existe:"
    dir "app\build\generated\ksp" /b
) else (
    echo "Diretorio KSP nao existe"
)

echo.
echo 5. Limpando caches problemáticos...
rmdir /s /q "app\build\generated" 2>nul
rmdir /s /q ".gradle\caches" 2>nul

echo.
echo 6. Testando compilacao basica...
call gradlew compileDebugKotlin --no-daemon --quiet
if %ERRORLEVEL% EQU 0 (
    echo "✓ Compilacao Kotlin basica: OK"
) else (
    echo "✗ Compilacao Kotlin basica: FALHOU"
)

echo.
echo 7. Testando KSP isoladamente...
call gradlew kspDebugKotlin --no-daemon --quiet
if %ERRORLEVEL% EQU 0 (
    echo "✓ KSP: OK"
) else (
    echo "✗ KSP: FALHOU"
)

echo.
echo ==========================================
echo DIAGNOSTICO CONCLUIDO
echo ==========================================

pause
