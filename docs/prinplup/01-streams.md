# Prinplup · Reto 01 - Streams avanzados

Las consultas están en `ConsultorFlota` y se prueban en `ConsultorFlotaAvanzadoTest` con la flota de 15 drones de `FlotaEjemplo`.

| # | Consulta | Método | Operaciones |
|---|---|---|---|
| 1 | Drones disponibles agrupados por tipo | `disponiblesPorTipo` | `filter`, `groupingBy` |
| 2 | Drone óptimo para una zona (tipo recomendado, mayor batería) | `droneOptimoParaZona` | `filter`, `max` con `Comparator.comparingInt` |
| 3 | Promedio de batería por tipo | `promedioBateriaPorTipo` | `groupingBy` con `averagingInt` |
| 4 | Separar misiones CRÍTICAS de las demás | `separarCriticas` | `partitioningBy` |
| 5 | Zonas únicas que cubre la flota activa | `zonasFlotaActiva` | `filter`, `map`, `distinct`, `sorted` |
| Extra | Misiones por prioridad y luego por batería del drone | `ordenarPorPrioridad` | `Comparator.comparing().thenComparing(..., reverseOrder())` |

La flota activa son los drones en estado `DISPONIBLE`, `EN_MISION` o `SUMERGIDO`; los que están en `RECARGANDO`, `MANTENIMIENTO` o `FALLO` no cubren zonas en ese momento.

## Resultados con la flota de ejemplo

| Consulta | Resultado |
|---|---|
| Disponibles por tipo | SUPERFICIAL: AR-01, AR-02, AR-05 · SEMISUMERGIDO: SS-01, SS-02, SS-05 · BUCEADOR: BU-01, BU-03 |
| Óptimo para Embalse Norte | BU-03 (buceador, 95%) |
| Promedio de batería | SUPERFICIAL 54.0 · SEMISUMERGIDO 55.4 · BUCEADOR 62.25 |
| Zonas de la flota activa | Embalse Norte, Canal Central, Laguna Sur, Punto Ribereño Este, Laboratorio Hídrico |
