# REVISIÓN GENERAL — AquaPort (Niveles Piplup, Prinplup y Empoleon)

**Proyecto:** AquaPort (Escuela Colombiana de Ingeniería)

**Estudiante:** Juan Nicolás Álvarez Muñoz

**Estado:** APROBADO ✅

**Revisión realizada con:** Gemini (IA asistente)

### 1. Nivel PIPLUP (MVP)

*4 drones Aqua-Ranger 100 | Asignación manual | Zonas fijas campus ECI*

| Punto / Reto | Estado | Observación Principal | 
| ----- | ----- | ----- | 
| **01. Streams & Lambdas** | PASS | Uso de `Optional` y lambdas concisas sobre un Stream por método. | 
| **02. GitHub & GitFlow** | PASS | GitFlow básico (`develop` y tag `v1.0.0`). `.gitignore` configurado. | 
| **03. Patrones (Builder)** | PASS | `Mision.Builder` valida nulos/vacíos y lanza `IllegalStateException`. | 
| **04. Principios SOLID** | PASS | Separación SRP (Registrador/Validador) e inyección DIP. | 
| **05. Diagrama C4 Contexto** | PASS | C4 Nivel 1 delimitando caja central y los 3 actores principales. | 
| **06. RF, RNF & MoSCoW** | PASS | 3 RF con actor/acción/resultado y 3 RNF medibles. | 
| **07. Plantilla DOSW** | PASS | Especificación AP-01 con tipos reales y flujos alternos. | 
| **08. Identidad / UX** | PASS | Paleta hídrica oscura y 5 heurísticas de Nielsen aplicadas. | 
| **09. Agilismo & Jira** | PASS | Jerarquía completa (Épica, Feature, HUs, Subtareas, Gherkin). | 
| **10. Diagramas CU** | PASS | Diagrama UML con `<<include>>` y `<<extend>>` condicionales. | 
| **11. Mocks con IA** | PASS | 3 estados del panel (Normal, Alerta, Vacío) con consistencia visual. | 
| **12. TDD** | PASS | Ciclo Red-Green-Refactor en commits y pruebas AAA. | 
| **13. JaCoCo** | PASS | Cobertura del 100% en `ValidadorMision` y capa de servicios. | 
| **14. SonarQube** | PASS | 0 Bugs, 0 Vulnerabilidades, 0 Code Smells y 0 min Deuda Técnica. | 

> **Conclusión Nivel PIPLUP:**
>
> El MVP establece una base arquitectónica sólida al implementar un dominio limpio mediante patrones de diseño creacionales (*Builder*) y el cumplimiento estricto de principios SOLID (SRP/DIP). Se consolidó un pipeline de calidad automatizado alcanzando el 100% de cobertura en componentes críticos mediante TDD y logrando un reporte impecable en SonarQube sin deuda técnica.

### 2. Nivel PRINPLUP (Flota Autónoma)

*15 drones (3 tipos) | Asignación automática | Alertas en tiempo real*

| Punto / Reto | Estado | Observación Principal | 
| ----- | ----- | ----- | 
| **01. Streams Avanzados** | PASS | Operadores `groupingBy`, `averagingInt` y `partitioningBy`. | 
| **02. GitHub & GitFlow** | PASS | Release `v2.0`, `hotfix` y tag `v2.0.1` trazables mediante PRs. | 
| **03. Patrones Integrados** | PASS | *Strategy*, *Observer* y *Factory Method* integrados en asignación. | 
| **04. Auditoría SOLID** | PASS | Cumplimiento OCP, LSP, ISP y DIP sin acoplamiento a clases concretas. | 
| **05. Diagrama C4 Contexto** | PASS | Integración de APIs externas (Hídrica, Alertas, Centro Control). | 
| **06. RF, RNF & MoSCoW** | PASS | Requisitos AP-07 a AP-11 y balance de tensión Batería vs Prioridad. | 
| **07. Plantilla DOSW** | PASS | Asignación automática detallada con sub-objetos y fallos. | 
| **08. Identidad & Leyes UX** | PASS | Tarjeta de 6 estados, Ley de Fitts y Ley de Hick aplicadas. | 
| **09. Agilismo & Jira** | PASS | Sprint 2 planificado con Sprint Goal, 29 SP (Fibonacci) y DoD. | 
| **10. Diagrama CU Extendido** | PASS | Generalización de actores e inclusión de actor Técnico. | 
| **11. Mocks con IA** | PASS | Flujo en 4 pantallas con manejo de alertas por misión CRÍTICA. | 
| **12. TDD con Mockito** | PASS | Simulación de dependencias con `@Mock` y verificación `verify()`. | 
| **13. Quality Gate JaCoCo** | PASS | Regla en Maven/XML cumplida al 100% tras cubrir casos borde. | 
| **14. SonarQube** | PASS | 0 Bugs (tras corregir precisión de coordenadas), 0 Smells. | 

> **Conclusión Nivel PRINPLUP:**
>
> La transición hacia una flota autónoma demuestra madurez en la gestión de complejidad mediante patrones de comportamiento (*Strategy*, *Observer*). Se resolvió con éxito el acoplamiento algorítmico, permitiendo la asignación dinámica de drones según su tipo y capacidad sin alterar la lógica central del cliente, manteniendo estándares de calidad rigurosos.

### 3. Nivel EMPOLEON (Enterprise)

*60 drones | 4 zonas hídricas | Rutas multi-etapa | Arquitectura por capas*

| Punto / Reto | Estado | Observación Principal | 
| ----- | ----- | ----- | 
| **01. Streams Enterprise** | PASS | Collector personalizado `PromedioBateriaPorZona` y paralelismo. | 
| **02. Git y Hooks** | PASS | Hook `commit-msg` (Conventional Commits) y CHANGELOG.md. | 
| **03. Patrones Enterprise** | PASS | *Chain of Responsibility*, *Decorator* (telemetría) y *Adapter* (API). | 
| **04. Auditoría ArchUnit** | PASS | Aislamiento del dominio verificado mediante reglas de arquitectura. | 
| **05. Diagrama C4 Contenedores** | PASS | C4 Nivel 2 con componentes y protocolos (JDBC, HTTPS, AMQP). | 
| **06. Requisitos Enterprise** | PASS | SLA (99.5%), throughput y análisis de 5 tensiones sistémicas. | 
| **07. Plantilla DOSW Compleja** | PASS | Ejecución multi-etapa con cadena de custodia y 5 flujos alternos. | 
| **08. Design System & UX** | PASS | Tokens CSS, contraste WCAG AA validado y 6 pantallas completas. | 
| **09. Roadmap & Jira** | PASS | Planificación a 3 Sprints (E1, E2, E3) y Retrospectiva real. | 
| **10. Diagrama CU Fallos** | PASS | 5 extensiones de fallo condicionales formalizadas en UML. | 
| **11. Mocks de Flujo Completo** | PASS | Mapeo visual de zonas, telemetría y traspaso de custodia. | 
| **12. TDD Multi-capa** | PASS | Pruebas unitarias, de integración (`FlujoMultiEtapa`) y ArchUnit. | 
| **13. Quality Gate Producción** | PASS | Umbrales estrictos cumplidos tras refactorizar `Mision.Builder`. | 
| **14. Auditoría Capas Final** | PASS | Dirección de dependencias, dominio sin librerías externas e inyección por constructor verificados con `grep` y ArchUnit. | 

> **Conclusión Nivel EMPOLEON:**
>
> La solución alcanza el nivel Enterprise al consolidar una arquitectura por capas (dominio, aplicación e infraestructura) con puertos y adaptadores, verificada mediante ArchUnit. La incorporación de patrones como *Chain of Responsibility* y *Adapter* garantiza escalabilidad operacional y mantenibilidad ante requerimientos complejos como trayectos multi-etapa y auditoría estricta de cadena de custodia.

### CONCLUSIÓN TÉCNICA Y RESOLUCIÓN DE INCIDENTES

El desarrollo de la plataforma **AquaPort** evidencia una evolución arquitectónica sólida a lo largo de los tres niveles evaluados:

1. **Evolución del Dominio y Patrones:** Se pasó de un MVP (Piplup) enfocado en la validación básica con un patrón *Builder* a una arquitectura por capas (Empoleon) en la que el dominio no depende de librerías externas y se comunica con la infraestructura mediante puertos (interfaces) y adaptadores. Los patrones de diseño fueron incorporados para resolver problemas concretos de dominio: *Strategy* para decisiones de selección de drones, *Observer* para la emisión de alertas, *Chain of Responsibility* para la validación encadenada de reglas de negocio, *Decorator* para inyectar telemetría de forma transparente y *Adapter* para aislar librerías o APIs de terceros.

2. **Proceso de Corrección de Errores durante el Desarrollo:**

   * **Incidente 1 — Pérdida de Precisión en Distancias (Nivel Prinplup):** Durante el análisis con SonarQube, se detectó un bug (`java:S2184`) en `ZonaHidrica.distanciaA` debido al uso de operaciones aritméticas sobre enteros antes de convertir a `double`. La falla fue corregida modificando los tipos de las coordenadas a `double` directamente en el `enum`, lo que previno desbordamientos y errores de redondeo al calcular distancias euclidianas entre zonas hídricas.

   * **Incidente 2 — Falla de Cobertura de Ramas (Nivel Prinplup / JaCoCo):** En la fase de verificación con JaCoCo, la clase `DroneSuperficial` presentó una rama no cubierta (*rama amarilla*) en el método `puedeOperarEn` cuando el agua estaba en calma pero se requería inmersión. El proceso de corrección consistió en añadir la prueba unitaria explícita `superficial_noOperaConInmersion`, asegurando que un drone de superficie jamás sea asignado a misiones profundas.

   * **Incidente 3 — Violación de Líneas por Método (Nivel Empoleon / SonarQube):** Al activar el perfil restringido de calidad en SonarQube (máximo 15 líneas por método), la clase `Mision.Builder.build()` sobrepasó el umbral al incorporar la validación de waypoints multi-etapa. Se corrigió aplicando el refactor *Extract Method*, aislando la comprobación de integridad en un método privado `validarCampos`, reduciendo el tamaño del método `build` sin alterar su comportamiento.

   * **Incidente 4 — JaCoCo no podía instrumentar las clases generadas por Mockito (Nivel Prinplup):** Al agregar Mockito, el agente de JaCoCo intentaba instrumentar también las clases que Mockito genera en tiempo de ejecución y las del JDK, y mostraba el error `Unsupported class file major version 71`. Se corrigió limitando la instrumentación a las clases del proyecto (`<include>edu/eci/aquaport/**</include>` en el `pom.xml`), de modo que el reporte solo mide el código de AquaPort.

   * **Incidente 5 — La cadena de custodia registraba al drone equivocado (Nivel Empoleon):** En la primera versión de `CoordinadorRuta`, el traspaso de la muestra se registraba al terminar cada tramo usando el drone planificado para el siguiente. Si ese drone fallaba en el waypoint, la custodia quedaba a nombre de un drone que nunca recibió la muestra. Se corrigió moviendo el registro al inicio de cada tramo, después de una posible reasignación, con el método `poseedorActual()` de `CadenaCustodia`. La prueba `bateriaCriticaEnRuta_reasigna` verifica que el traspaso quede a nombre del drone de reemplazo.

   * **Incidente 6 — El CHANGELOG repetía los commits del hotfix (Nivel Empoleon):** El script `generar-changelog.sh` calculaba la sección "Sin publicar" desde el último tag alcanzable en `develop`. Como el tag `v2.0.1` quedó en `main`, los commits del hotfix aparecían dos veces. Se corrigió calculando los commits pendientes con `git log HEAD --not --tags`, que excluye todo lo que ya pertenece a cualquier versión publicada.

   * **Incidente 7 — Conflicto al integrar `develop` en `main` (Nivel Empoleon):** Al usar *rebase merge* para llevar la release `v3.0` a `develop`, los mismos cambios quedaron en `main` y en `develop` con identificadores distintos, y el Pull Request siguiente de `develop` a `main` mostró conflictos. Se resolvió con una rama de sincronización (`chore/sincronizar-main`) que integró `develop` conservando su versión de los archivos, y se integró a `main` por Pull Request, respetando la protección de ramas.

3. **Resultado final:** Los tres niveles quedan entregados con las versiones `v1.0.0`, `v2.0.0`/`v2.0.1` y `v3.0.0`, cobertura del 100% en líneas y ramas, quality gate de SonarQube en verde en cada nivel y la planeación completa en Jira.
