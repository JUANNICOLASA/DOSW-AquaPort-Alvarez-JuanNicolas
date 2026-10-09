# Empoleon · Reto 01 - Streams para rutas multi-etapa

| # | Requisito | Implementación | Operaciones |
|---|---|---|---|
| 1 | Eficiencia de cada zona (entregadas / totales) | `EstadisticasFlota.eficienciaPorZona` | `groupingBy` + `averagingDouble` (1 si se entregó, 0 si no) |
| 2 | Drone con mejor historial de entregas | `EstadisticasFlota.droneConMejorHistorial` | `filter`, `groupingBy` + `counting`, `max` sobre las entradas |
| 3 | Ruta óptima: drone disponible más cercano por tramo | `PlanificadorRuta.planificar` y `buscarDroneMasCercano` | `IntStream.range` para los tramos, `filter` con la cadena de validación, `min` por distancia y luego por batería |
| 4 | Collector personalizado: drones por zona con batería promedio en una pasada | `PromedioBateriaPorZona` (implementa `Collector`) | `supplier`, `accumulator` (suma y conteo por zona), `combiner`, `finisher` |
| Extra | Carga de trabajo por zona | `EstadisticasFlota.cargaPorZona` | `filter` (EN_TRANSITO), `flatMap` sobre waypoints, `groupingBy` + `counting` |

## Collector personalizado

`PromedioBateriaPorZona` guarda por zona un arreglo con la suma de baterías y la cantidad de drones. Al terminar divide ambos valores. Así se agrupa y se promedia recorriendo la flota una sola vez, sin un segundo stream por zona.

## ¿Cuándo usar un stream paralelo?

Un stream paralelo divide el trabajo en varios hilos y luego une los resultados. Solo conviene cuando:

1. Hay muchos elementos (miles o más) y el trabajo por elemento es costoso.
2. Las operaciones no tienen efectos secundarios ni dependen del orden.
3. La fuente se divide fácil (por ejemplo, `ArrayList`) y la unión de resultados es barata.

**Dónde sí aplica en AquaPort:** calcular la batería promedio por zona sobre el **historial completo de telemetría** (cientos de miles de lecturas). Para ese caso el collector tiene un `combiner` que une resultados parciales. La prueba `promedioBateriaPorZona_paraleloIgualSecuencial` comprueba que con `parallelStream()` se obtiene el mismo resultado.

**Dónde no aplica:**

- Las consultas sobre la flota de 60 drones: crear y coordinar hilos cuesta más que recorrer 60 elementos.
- La planificación de ruta: cada tramo **reserva** un drone (cambia su estado a EN_MISION), así que el tramo siguiente depende del anterior. En paralelo dos tramos podrían reservar el mismo drone.
- `droneConMejorHistorial` y `eficienciaPorZona`: trabajan con las misiones del día (decenas), y el agrupamiento paralelo tendría que unir mapas sin obtener una mejora real.
