# Reto 03 - Patrón Builder

`Mision` tiene un constructor privado y solo se puede crear con `Mision.Builder`, lo que evita confundir el orden de los parámetros.

```java
Mision mision = new Mision.Builder()
        .id("M-001")
        .drone(droneAsignado)
        .puntoPartida("Embalse Norte")
        .puntoLlegada("Laboratorio Hídrico")
        .tipoCarga(TipoCarga.MUESTRA_AGUA)
        .build();
```

El estado inicia en `PENDIENTE` si no se indica otro.

## Validaciones de build()

| Validación | Mensaje |
|---|---|
| `id` nulo o vacío | El id de la misión es obligatorio |
| `drone` nulo | La misión debe tener un drone asignado |
| `puntoPartida` nulo o vacío | El punto de partida es obligatorio |
| `puntoLlegada` nulo o vacío | El punto de llegada es obligatorio |
| `tipoCarga` nulo | El tipo de carga es obligatorio |
| `estado` nulo | El estado de la misión es obligatorio |
| Drone no disponible | El drone AR-XX no está disponible |

En todos los casos se lanza `IllegalStateException`.
