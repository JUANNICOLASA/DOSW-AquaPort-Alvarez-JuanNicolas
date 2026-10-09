# Empoleon · Reto 12 - Pruebas en tres capas

## 1. Unitarias del dominio y la aplicación

| Clase | Pruebas | Enfoque |
|---|---|---|
| `PlanificadorRuta` | `PlanificadorRutaTest` (6) | Cada tramo recibe el drone más cercano, desvío por waypoint adverso o inactivo, destino inactivo, tramo sin drone, reemplazo monitoreado. Repositorio, API y telemetría simulados con Mockito. |
| `CoordinadorRuta` | `CoordinadorRutaTest` (5) | Entrega con custodia completa, reasignación por fallo, por batería crítica, custodia interrumpida y ruta terminada. `PlanificadorRuta` y el observador simulados con Mockito. |
| Modelo de rutas | `RutaMultiEtapaTest` (8) | Puntos, waypoints, consumo por tramo, cadena de custodia y estados. |
| Patrones Enterprise | `ValidadorEnCadenaTest`, `DroneConMonitoreoTest`, `AdaptadorAPIHidricaTest` | La cadena se detiene, el decorator no altera el drone, el adapter maneja valores límite. |

## 2. Integración entre capas

`FlujoMultiEtapaIntegracionTest` usa las implementaciones reales: flota Enterprise de 60 drones, `RepositorioDronesMemoria`, `AdaptadorAPIHidrica` con su cliente simulado, `MonitorZonasEnMemoria`, `TelemetriaEnMemoria` y `CentroControlObserver`.

| Prueba | Flujo |
|---|---|
| `flujoCompleto_entregaLaMuestra` | Solicitud → planificación → asignación por tramos → notificación de cada waypoint → entrega |
| `falloDeDroneEnWaypoint` | Fallo de drone en waypoint intermedio (reasignación) |
| `condicionAdversaEnTramo` | Condición hídrica adversa en un tramo (desvío de ruta) |
| `zonaDestinoInactiva` | Zona destino temporalmente inactiva |
| `cadenaCustodiaInterrumpida` | Cadena de custodia interrumpida (sin drone receptor) |
| `bateriaCriticaEnMitadDeRuta` | Batería crítica en mitad de la ruta (reasignación) |

## 3. Cobertura

`mvn clean verify` aplica el quality gate de JaCoCo: 85% de líneas por clase y 75% de ramas. El resultado es 100% de líneas y 100% de ramas (ver [reto 13](13-jacoco-sonarqube.md)).

## Arquitectura

`ArquitecturaCapasTest` (ArchUnit) completa la suite: falla si una clase rompe la dirección de dependencias entre capas.
