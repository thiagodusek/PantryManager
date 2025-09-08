# Guia de Qualidade de Software — PantryManager

Este documento define o framework e o processo de qualidade para o PantryManager (Android nativo, Kotlin, Jetpack Compose, Clean Architecture, Hilt, MVVM).

## Objetivos

- Garantir código legível, testável e sustentável (SOLID + Clean Architecture).
- Prevenir regressões com testes automatizados e análise estática.
- Monitorar cobertura e débito técnico com quality gates.
- Padronizar estilo e automações locais/CI.

## Stack de Qualidade (recomendada)

- Orquestração/Quality Gate: SonarQube ou SonarCloud (métricas, cobertura, code smells, duplicações).
- Análise estática: Detekt (Kotlin) + Android Lint + Ktlint ou Spotless (formatação).
- Testes:
  - Unitários (JVM): JUnit4, MockK, Turbine (Flows/coroutines), Coroutines Test.
  - Android/JVM: Robolectric (opcional para componentes Android sem dispositivo).
  - UI: Jetpack Compose UI Testing + Espresso + Hilt Testing.
  - Screenshot (opcional): Roborazzi (JVM) ou Paparazzi.
- Cobertura: Kover (unit tests) e JaCoCo (instrumented tests) com relatório consolidado.
- Mutação (opcional, fase futura): PIT (pitest) para fortalecer a qualidade dos testes.

## Escopo por Camada (Clean Architecture)

- domain: use cases, entidades, regras de negócio — foco alto em unit tests (puros).
- data: repos, mappers, datasources (Firebase/Room/Retrofit) — mocks/stubs e testes de integração leves.
- presentation: ViewModels (coroutines/flows) — unit tests com Turbine; UI — testes de Compose e navegação.

## Padrões de Código

- Kotlin style guide; data classes para modelos; nomes: PascalCase (classes), camelCase (funções/variáveis).
- Erros e estados via sealed classes.
- Coroutines para assíncrono; evitar callbacks.
- Regras Detekt + Ktlint/Spotless como fonte da verdade.

## Metas e Indicadores

- Build sem erros de Lint/Detekt/Ktlint.
- Cobertura mínima inicial: 60% (unit); alvo: 80%+. Instrumented UI fora da meta num primeiro momento.
- Zero Code Smell crítico no Sonar; sem duplicações acima de 3%.

## Dependências sugeridas (Gradle — exemplos)

Obs.: Ajuste versões conforme o Gradle/AGP do projeto.

```kotlin
// build.gradle (root) — plugins
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply false
    id("org.jetbrains.kotlinx.kover") version "0.8.3" apply false
}
```

```kotlin
// app/build.gradle — testes & qualidade (exemplo)
dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("app.cash.turbine:turbine:1.1.0")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:<composeVersion>")
    debugImplementation("androidx.compose.ui:ui-test-manifest:<composeVersion>")

    // Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:<hiltVersion>")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:<hiltVersion>")
}
```

```kotlin
// Detekt config (root)
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("detekt.yml")) // opcional, se quiser customizar
}
```

Para formatação, escolha uma abordagem:

- Ktlint (plugin) ou
- Spotless com `ktlint` integrado (recomendado pela flexibilidade).

## Testes: Estratégia

- Unitários (rápidos, pirâmide de testes):
  - domain: use cases e validações (100% cobertos preferencialmente).
  - data: mappers, lógica em repositórios; mocks para datasources externos.
  - presentation: ViewModels — uso do Turbine para assert de Flows/States.
- Integração leve:
  - Retrofit DTO ↔ domain; mapeamento Firestore/Room; serialização.
- UI/Instrumentados:
  - Compose UI Testing: estados, navegação, acessibilidade (sem rolagem desnecessária, botões visíveis).
  - Espresso para interações clássicas; Hilt Test Rules para DI.
- Screenshot (futuro): validar regressões visuais das principais telas (Login, Home, Listas, Perfil).

## Cobertura

- Kover: relatório XML/HTML para unit tests.
- JaCoCo: gerar cobertura para `connectedAndroidTest`.
- Publicar no Sonar para quality gate; falhar PR se cobertura cair abaixo do limiar.

Comandos úteis (Windows/PowerShell):

- .\gradlew.bat detekt
- .\gradlew.bat lint
- .\gradlew.bat testDebugUnitTest koverXmlReport
- .\gradlew.bat connectedDebugAndroidTest

## Análise Estática e Lint

- Detekt: regras Kotlin (nomenclatura, complexidade, arquitetura básica).
- Android Lint: recursos, layouts, acessibilidade, desempenho.
- Ktlint/Spotless: formatação consistente (pre-commit opcional).

## SonarQube/SonarCloud

- SonarCloud recomendado para PRs.
- Criar `sonar-project.properties` (exemplo):

```properties
sonar.projectKey=pantrymanager
sonar.projectName=PantryManager
sonar.language=kotlin
sonar.sources=app/src/main/java
sonar.tests=app/src/test/java,app/src/androidTest/java
sonar.kotlin.coverage.reportPaths=build/reports/kover/xml/report.xml
sonar.junit.reportPaths=app/build/test-results/testDebugUnitTest
```

- Configurar token secreto no CI (`SONAR_TOKEN`).

## CI/CD (GitHub Actions — fluxo sugerido)

Jobs por PR em `main`:

1) setup-java + cache gradle
2) detekt + lint + spotlessCheck/ktlintCheck
3) unit tests + kover report (upload artifact)
4) instrumented tests (opcional na PR; mover para nightly ou Firebase Test Lab)
5) sonar scan (quality gate)

Regra de merge: PR só pode ser mergeado com todos os checks verdes.

## Execução local (checklist)

1) Formatação: .\gradlew.bat spotlessApply (ou ktlintFormat)
2) Análise: .\gradlew.bat detekt lint
3) Testes unitários: .\gradlew.bat testDebugUnitTest
4) Cobertura: .\gradlew.bat koverXmlReport
5) UI/instrumentados: .\gradlew.bat connectedDebugAndroidTest (emulador/dispositivo)

## Critérios de Aceite (Definition of Done)

- Código compila e passa Detekt/Lint/Ktlint/Spotless.
- Testes unitários 100% verdes; cobertura >= 60% (alvo 80%+).
- Sem Code Smell crítico; nenhum bug blocker no Sonar.
- UI crítica (Login, Forgot Password) coberta por testes de Compose básicos.
- Documentação atualizada (README.md, PROMPT_PROJETO.md, QUALITY.md).

## Roadmap de Evolução

- Adicionar screenshot testing (Roborazzi/Paparazzi) para telas chave.
- Ativar PIT (mutação) nos módulos de domínio.
- Matriz de dispositivos (Firebase Test Lab) para testes UI em nightly.
- Acessibilidade: lint + testes que validem contentDescription e contrastes.
- Métricas de performance de inicialização (Benchmarking, macro/microbenchmarks).

## Referências internas

- README.md — visão geral e como rodar.
- PROMPT_PROJETO.md — diretrizes e backlog.
- Este QUALITY.md — política de qualidade e prática recomendada.
