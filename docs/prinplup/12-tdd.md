# Prinplup · Reto 12 - TDD con Mockito del AsignadorAutomatico

En la rama `feature/asignacion-automatica` las pruebas se subieron antes que el código:

| Orden | Commit | Fase |
|---|---|---|
| 1 | `test: agregar pruebas con Mockito para AsignadorAutomatico` | Red |
| 2 | `feat: implementar AsignadorAutomatico con estrategia y observadores` | Green |
| 3 | `test: agregar pruebas de las estrategias de selección de drone` | Red |
| 4 | `feat: implementar estrategias MayorBateria, ZonaCercana y PrioridadCritica` | Green |
| 5 | `refactor: generar el código de misión a partir del número de la solicitud` | Refactor |

## Pruebas obligatorias

| Caso | Prueba | Qué verifica |
|---|---|---|
| Asignación exitosa | `asignacionExitosa_creaMision` | Se crea la misión, el drone pasa a `EN_MISION`, se guarda y se notifica una vez |
| Misión CRÍTICA sin drones | `misionCritica_sinDrones_notificaObservadores` | Cada observador recibe `notificarFalloAsignacion` una vez |
| Drone en FALLO durante la asignación | `droneEnFallo_notificaYBuscaAlternativo` | Se notifica el fallo y se asigna otro drone |
| Flota vacía | `flotaVacia_noAsigna` | No se crea misión y nunca se llama a `guardar` |
| Batería en el umbral (35%) | `bateriaEnUmbral_esCandidato` | El drone con 35% entra a la lista de candidatos y el de 34% no |

Pruebas adicionales: observador eliminado y cambio de estrategia.

Las dependencias (`RepositorioDrones`, `ServicioCondicionesHidricas`, `EstrategiaSeleccion`, `RepositorioMisiones` y los observadores) se simulan con `@Mock` y `MockitoExtension`. Así la prueba solo evalúa la lógica del asignador.

Cobertura del `AsignadorAutomatico`: 100% de líneas y ramas (ver [reto 13](13-jacoco.md)).
