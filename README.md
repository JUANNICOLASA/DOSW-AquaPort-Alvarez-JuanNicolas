# AquaPort

Sistema de gestión de drones acuáticos de la Escuela Colombiana de Ingeniería (nivel Piplup - MVP).

**Autor:** Juan Nicolás Álvarez Muñoz
**Curso:** DOSW 2026-2

## Requisitos

- Java 21 o superior
- Maven 3.9

## Ejecución

```bash
mvn clean test
mvn compile
java -cp target/classes edu.eci.aquaport.Main
```

El reporte de cobertura queda en `target/site/jacoco/index.html`.

## Estructura

```
src/main/java/edu/eci/aquaport
├── Main.java
├── modelo        DroneAcuatico, Mision (Builder), TipoCarga, EstadoMision
├── repositorio   RepositorioMisiones, RepositorioMisionesMemoria
└── servicio      ConsultorFlota, ValidadorMision, RegistradorMisiones, NotificadorOperador
```

## Retos Piplup

| # | Reto | Entrega |
|---|---|---|
| 01 | Streams y lambdas | [docs/01-streams.md](docs/01-streams.md) |
| 02 | GitHub y GitFlow | [docs/02-gitflow.md](docs/02-gitflow.md) |
| 03 | Patrón Builder | [docs/03-builder.md](docs/03-builder.md) |
| 04 | SOLID (SRP y DIP) | [docs/04-solid.md](docs/04-solid.md) |
| 05 | Diagrama de contexto C4 | [docs/05-c4-contexto.md](docs/05-c4-contexto.md) |
| 06 | RF, RNF y MoSCoW | [docs/06-requisitos.md](docs/06-requisitos.md) |
| 07 | Plantilla DOSW | [docs/07-plantilla-dosw.md](docs/07-plantilla-dosw.md) |
| 08 | Manual de identidad y UX/UI | [docs/08-manual-identidad.md](docs/08-manual-identidad.md) |
| 09 | Agilismo y Jira | [docs/09-jira.md](docs/09-jira.md) |
| 10 | Diagrama de casos de uso | [docs/10-casos-de-uso.md](docs/10-casos-de-uso.md) |
| 11 | Mocks con IA | [docs/11-mocks-ia.md](docs/11-mocks-ia.md) |
| 12 | TDD | [docs/12-tdd.md](docs/12-tdd.md) |
| 13 | JaCoCo | [docs/13-jacoco.md](docs/13-jacoco.md) |
| 14 | SonarQube | [docs/14-sonarqube.md](docs/14-sonarqube.md) |

Las capturas del uso de la IA asistente están en [docs/capturas-ia](docs/capturas-ia).
