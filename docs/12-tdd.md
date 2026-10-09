# Reto 12 - TDD del ValidadorMision

El `ValidadorMision` se construyó siguiendo el ciclo Red, Green y Refactor. El historial de la rama `feature/tdd-validador-mision` muestra que cada grupo de pruebas se subió antes que el código que las hace pasar:

| Orden | Commit | Fase |
|---|---|---|
| 1 | `test: agregar pruebas de batería para ValidadorMision` | Red |
| 2 | `feat: implementar validación de batería en ValidadorMision` | Green |
| 3 | `test: agregar pruebas de punto de llegada, zona y disponibilidad` | Red |
| 4 | `feat: implementar validación de punto de llegada y disponibilidad` | Green |
| 5 | `refactor: extraer batería mínima y zonas válidas a constantes en ValidadorMision` | Refactor |

## Pruebas

| Caso | Prueba |
|---|---|
| Batería suficiente | `droneBateriaSuficiente_puedeAsignarse` |
| Batería crítica | `droneBateriaCritica_noAsignable` |
| Batería en el límite (35%) | `droneBateriaEnLimite_puedeAsignarse` |
| Punto de llegada nulo | `puntoLlegadaNulo_lanzaExcepcion` |
| Punto de llegada vacío | `puntoLlegadaVacio_lanzaExcepcion` |
| Zona inválida | `zonaInvalida_lanzaExcepcion` |
| Zona válida | `zonaValida_noLanzaExcepcion` |
| Drone no disponible | `droneNoDisponible_noAsignable` |
| Drone disponible | `droneDisponible_puedeAsignarse` |

Todas las pruebas usan `@DisplayName`, siguen el patrón Arrange, Act, Assert y verifican una sola cosa.
