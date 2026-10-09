# Reto 01 - Streams y lambdas

Las cinco consultas están en `ConsultorFlota`. Cada método recibe la flota como parámetro y usa un solo stream, sin ciclos ni variables externas.

| Consulta | Método | Operaciones |
|---|---|---|
| Disponibles con batería mayor o igual a 35%, de mayor a menor batería | `disponiblesConBateriaSuficiente` | `filter`, `sorted`, `toList` |
| IDs de los drones disponibles | `idsDisponibles` | `filter`, `map`, `toList` |
| ¿Existe algún disponible con batería mayor o igual a 35%? | `existeDisponibleConBateriaSuficiente` | `anyMatch` |
| Cantidad de drones disponibles | `contarDisponibles` | `filter`, `count` |
| Drone con mayor batería | `droneConMayorBateria` | `max` |

## Salida del programa

Los resultados se muestran por medio de `NotificadorOperador`, que usa `java.util.logging.Logger`:

```
INFO: Disponibles con batería >= 35%: [DroneAcuatico[id=AR-01, modelo=Aqua-Ranger 100, bateria=92, disponible=true, zona=Embalse Norte], DroneAcuatico[id=AR-04, modelo=Aqua-Ranger 100, bateria=73, disponible=true, zona=Punto Ribereño Este], DroneAcuatico[id=AR-02, modelo=Aqua-Ranger 100, bateria=45, disponible=true, zona=Canal Central]]
INFO: IDs disponibles: [AR-01, AR-02, AR-04]
INFO: ¿Existe disponible con batería >= 35%?: true
INFO: Cantidad de disponibles: 3
INFO: Drone con mayor batería: DroneAcuatico[id=AR-01, modelo=Aqua-Ranger 100, bateria=92, disponible=true, zona=Embalse Norte]
INFO: Misión M-001 registrada con el drone AR-01 hacia Laboratorio Hídrico
INFO: Error: El drone AR-03 no está disponible
```

Para ejecutarlo:

```bash
mvn compile
java -cp target/classes edu.eci.aquaport.Main
```
