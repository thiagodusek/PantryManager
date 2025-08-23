@echo off
echo ============================================
echo SCRIPT DE INSTALACAO - PANTRYMANAGER
echo Versao: 1.0
echo Data: 23/08/2025
echo ============================================
echo.

REM Verificar se o MySQL está instalado
where mysql >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERRO] MySQL nao encontrado no PATH do sistema!
    echo.
    echo Instale o MySQL Server ou adicione ao PATH:
    echo - MySQL Server: https://dev.mysql.com/downloads/mysql/
    echo - MySQL Workbench: https://dev.mysql.com/downloads/workbench/
    echo.
    pause
    exit /b 1
)

echo [INFO] MySQL encontrado no sistema.
echo.

REM Solicitar credenciais
set /p MYSQL_USER=Digite o usuario MySQL (default: root): 
if "%MYSQL_USER%"=="" set MYSQL_USER=root

set /p MYSQL_HOST=Digite o host MySQL (default: localhost): 
if "%MYSQL_HOST%"=="" set MYSQL_HOST=localhost

set /p MYSQL_PORT=Digite a porta MySQL (default: 3306): 
if "%MYSQL_PORT%"=="" set MYSQL_PORT=3306

echo.
echo ============================================
echo EXECUTANDO SCRIPT SQL
echo ============================================
echo Host: %MYSQL_HOST%
echo Porta: %MYSQL_PORT%
echo Usuario: %MYSQL_USER%
echo Arquivo: database_schema.sql
echo ============================================
echo.

REM Executar o script SQL
mysql -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p < database_schema.sql

if %errorlevel% equ 0 (
    echo.
    echo ============================================
    echo [SUCESSO] Banco de dados criado com sucesso!
    echo ============================================
    echo.
    echo Estrutura criada:
    echo - Banco: pantry_manager
    echo - Tabelas: 18 tabelas principais
    echo - Views: 3 views otimizadas  
    echo - Procedures: 3 procedures
    echo - Triggers: 2 triggers
    echo - Dados iniciais: Categorias e Unidades
    echo.
    echo Proximo passo:
    echo - Configure a conexao no aplicativo Android
    echo - Teste as funcionalidades basicas
    echo.
) else (
    echo.
    echo ============================================
    echo [ERRO] Falha na execucao do script!
    echo ============================================
    echo.
    echo Verificacoes:
    echo 1. Usuario e senha corretos
    echo 2. MySQL Server esta rodando
    echo 3. Permissoes para criar banco de dados
    echo 4. Arquivo database_schema.sql existe
    echo.
)

echo.
echo Deseja executar o script de teste? (s/n)
set /p EXECUTE_TEST=
if /i "%EXECUTE_TEST%"=="s" (
    echo.
    echo Executando script de teste...
    mysql -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p < mysql_test.sql
)

echo.
pause
