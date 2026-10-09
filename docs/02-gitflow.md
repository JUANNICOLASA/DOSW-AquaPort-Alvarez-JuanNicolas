# Reto 02 - GitHub y GitFlow

## Ramas

| Rama | Uso |
|---|---|
| `main` | Código estable del MVP |
| `develop` | Integración del trabajo de cada reto |
| `feature/streams-consultor-flota` | Reto 01 |
| `feature/builder-mision` | Reto 03 |
| `feature/tdd-validador-mision` | Reto 12 |
| `feature/solid-registro-misiones` | Reto 04 |
| `feature/jacoco-cobertura` | Reto 13 |
| `feature/documentacion-requisitos` | Retos 05, 06, 07 y 10 |
| `feature/identidad-mocks` | Retos 08 y 11 |
| `feature/planeacion-jira` | Reto 09 |
| `feature/sonarqube-analisis` | Reto 14 |

Cada rama `feature/` sale de `develop` y vuelve a ella mediante un Pull Request. Al terminar el nivel, `develop` se integra en `main`.

## Commits

Los commits son pequeños, describen una sola cosa y siguen el formato `tipo: descripción` en español (`feat`, `test`, `refactor`, `docs`, `build`, `chore`).

## .gitignore

Se excluyen `target/`, `*.class`, `.idea/`, `*.iml` y archivos del sistema operativo para no subir archivos generados ni configuración del IDE.
