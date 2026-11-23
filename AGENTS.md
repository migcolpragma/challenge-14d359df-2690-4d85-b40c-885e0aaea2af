# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Implementación de BDD en un Sistema de Gestión de Cuentas Bancarias**.

| | |
|---|---|
| Tema | Desarrollador con Sólida Experiencia en BDD y Frameworks de Automatización |
| Nivel | senior-l2 |
| Chapter | Backend |
| Especialidad | Java |
| Stack | Java / Spring Boot 3.5 |
| Patron arquitectonico | capas estándar con separación de dominio y adaptadores |
| Tiempo estimado | 10 horas |

## Herramientas

- **Karate Framework**: Tecnologia pedida para este reto: forma parte del stack con el que se resuelve el ejercicio.

## Receta del stack

Esqueleto obligatorio:

- `pom.xml en la raiz`
- `clase con @SpringBootApplication`
- `application.yml en src/main/resources`
- `capa de dominio con entidades y puertos`
- `capa de aplicacion con casos de uso`
- `capa de infraestructura con adaptadores y @RestController`

Trampas conocidas:

- TODA `<version>` del pom va con tres segmentos: la del parent (ej. `3.5.6`) y la de cada dependencia que la lleve (ej. Resilience4j `2.2.0`). `3.4` y `2.0` no existen como artefacto y el build muere resolviendo dependencias.
- Las dependencias que el parent POM gestiona van SIN `<version>`: `spring-boot-starter-web`, `-data-jpa`, `-validation`, `-test`, etc.
- Resilience4j publica un artefacto por linea de Spring Boot. Con Spring Boot 3 va `resilience4j-spring-boot3` con version de tres segmentos (ej. `2.2.0`, no `2.0`). `resilience4j-spring-boot2` es de Spring Boot 2 y rompe el arranque.
- Si usas anotaciones de validacion (`@NotNull`, `@Size`, `@Positive`) declara `spring-boot-starter-validation`: el starter web no las trae.
- El `spring-boot-maven-plugin` tiene que estar en `<build><plugins>` o no se empaqueta ejecutable.
- Spring Boot 3 usa `jakarta.*`, nunca `javax.*`.
- Cada archivo empieza con su `package` y con un `import` por cada clase del proyecto que viva en otro paquete. Usar `PaymentService` desde `infrastructure` sin `import com.x.application.PaymentService` no compila.

Dependencias:

- org.springframework.boot:spring-boot-starter-web
- org.springframework.boot:spring-boot-starter-test
- com.intuit.karate:karate-apache 0.9.6
- com.intuit.karate:karate-junit5 1.4.1
- io.cucumber:cucumber-java 7.34.9
- io.cucumber:cucumber-junit 7.34.9
- io.cucumber:cucumber-spring 7.34.9
- org.mapstruct:mapstruct 1.6.3
- org.postgresql:postgresql
- org.projectlombok:lombok
- org.springframework.boot:spring-boot-starter-data-jpa
- org.springframework.boot:spring-boot-starter-validation

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `mvn -q -B -DskipTests -Dspring-boot.repackage.skip=true package` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `mvn -q -B -DskipTests -Dspring-boot.repackage.skip=true package` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Definición de Casos de Uso**: Documento de casos de uso transformado en piezas de lógica de negocio en la capa core.
- **Fase 2 — Implementación de Pruebas con Cucumber**: Escenarios de prueba en Gherkin y pasos de prueba implementados en Java.
- **Fase 3 — Integración con Karate Framework**: Pruebas de API escritas en Karate y configuradas en el proyecto.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Superficie de practica (NO completes)

Estos archivos SON el ejercicio de la persona. No los implementes; deja stubs. No toques la logica que el reto pide completar.

- [ ] `src/main/java/bdd/cucumber/AccountStepDefinitions.java` — La pieza p2 (Implementación de pasos de prueba para escenarios de Cucumber en Gherkin, fase f2) vive aqui: este archivo es su unico dueno (blueprint v2).
- [ ] `src/main/java/bdd/cucumber/AccountTestContext.java` — La pieza p3 (Gestión del contexto de prueba para escenarios de Cucumber, fase f2) vive aqui: este archivo es su unico dueno (blueprint v2).
- [ ] `src/main/java/bdd/karate/AccountApiClient.java` — La pieza p4 (Implementación de un cliente de API para pruebas con Karate, fase f3) vive aqui: este archivo es su unico dueno (blueprint v2).

## Mapa de fases

La **Fase 0** es clonar el repositorio, instalar dependencias y correr las pruebas de aceptacion: todas fallan porque la logica de cada pieza esta pendiente.

| Fase | Pieza (skill) | Archivo:simbolo | Criterios | Prueba | Comando |
|------|---------------|-----------------|-----------|--------|---------|
| f1 — Definición de Casos de Uso | Validación de reglas de negocio para creación de cuentas bancarias | — | Existe una pieza de lógica en la capa `core` que implementa las reglas de negocio y validaciones de los casos de uso.; La pieza tiene casos de aceptación que cubren al menos 2 escenarios principales y 1 escenario de error.; La pieza es probada de forma aislada mediante inyección de puertos. | — | — |
| f2 — Implementación de Pruebas con Cucumber | Implementación de pasos de prueba para escenarios de Cucumber en Gherkin | `src/main/java/bdd/cucumber/AccountStepDefinitions.java:AccountStepDefinitions` | Los escenarios de prueba en Gherkin cubren al menos 2 casos principales y 1 caso de error.; Los pasos de prueba en Java están implementados y vinculados a los escenarios de Gherkin.; Las pruebas se ejecutan correctamente y validan el comportamiento esperado. | — | — |
| f2 — Implementación de Pruebas con Cucumber | Gestión del contexto de prueba para escenarios de Cucumber | `src/main/java/bdd/cucumber/AccountTestContext.java:AccountTestContext` | Los escenarios de prueba en Gherkin cubren al menos 2 casos principales y 1 caso de error.; Los pasos de prueba en Java están implementados y vinculados a los escenarios de Gherkin.; Las pruebas se ejecutan correctamente y validan el comportamiento esperado. | — | — |
| f3 — Integración con Karate Framework | Implementación de un cliente de API para pruebas con Karate | `src/main/java/bdd/karate/AccountApiClient.java:AccountApiClient` | Las pruebas de API escritas en Karate cubren al menos 2 escenarios principales y 1 escenario de error.; Las pruebas están configuradas en el proyecto y se ejecutan correctamente.; Las pruebas validan el comportamiento esperado de la API. | — | — |

## Pruebas de aceptacion y ubicacion de la logica

Las pruebas de aceptacion (columna "Prueba" de la tabla) son de **solo lectura**: definen que significa "listo". No las edites, no las borres y no les agregues `skip`; lo que se resuelve es el codigo de cada pieza.

La logica de cada pieza va en su archivo; no la muevas a otro.

## Lo que falta y tenes que completar

No se detectaron huecos: estan los archivos declarados, el boilerplate del stack y ninguna referencia quedo colgando. Igual corre el comando de verificacion — que los archivos existan no garantiza que compilen.

### Presentes (12)

- `src/test/java/AppTest.java`
- `pom.xml`
- `src/main/java/domain/model/Account.java`
- `src/main/java/domain/port/AccountRepository.java`
- `src/main/java/domain/service/AccountValidationService.java`
- `src/main/java/infrastructure/repository/AccountJpaRepository.java`
- `src/main/java/bdd/cucumber/AccountStepDefinitions.java`
- `src/main/java/bdd/cucumber/AccountTestContext.java`
- `src/main/java/bdd/karate/AccountApiClient.java`
- `src/main/java/application.yml`
- `src/main/java/Application.java`
- `src/main/java/infrastructure/repository/AccountEntity.java`

### Capas del patron declarado

Son las capas que la propuesta arquitectonica plantea, marcadas segun existan hoy en el repo. Orientan donde poner el codigo nuevo; no son una condicion para compilar.

- `src/main/java/com/bank/account/domain` (ausente)
- `src/main/java/com/bank/account/application` (ausente)
- `src/main/java/com/bank/account/infrastructure` (ausente)
- `src/main/java/com/bank/account/config` (ausente)
- `src/test/java/com/bank/account/bdd/cucumber` (ausente)
- `src/test/java/com/bank/account/bdd/karate` (ausente)
- `src/test/resources/bdd/cucumber/features` (ausente)

## Verificacion

```bash
mvn -q -B -DskipTests -Dspring-boot.repackage.skip=true package
```

El comando tiene que pasar SIN implementar los archivos de la superficie de practica: solo andamiaje.

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **capas estándar con separación de dominio y adaptadores**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Perfil: Chapter Backend, Especialidad Desarrollador, Tecnología Java, Senior L2
- Brecha que el reto ataca: Aplica metodologías de desarrollo basadas en comportamiento como BDD (Desarrollo Guiado por Comportamiento) y trabaja bajo herramientas de automatización como Cucumber, el framework Karate, etc.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
