# Empoleon · Reto 03 - Chain of Responsibility, Decorator y Adapter

## Chain of Responsibility: validación de misiones

`ValidadorEnCadena` define el eslabón: valida su regla y, si aprueba, pasa el contexto al siguiente. Si rechaza, la cadena se detiene y devuelve el motivo.

```
ValidadorBateria → ValidadorCapacidadCarga → ValidadorZonaActiva → ValidadorCondicionesHidricas
```

`ValidadorEnCadena.estandar(monitorZonas)` arma esa cadena. La usan `AsignadorAutomatico` y `PlanificadorRuta` antes de asignar cualquier drone. Agregar una regla es crear un eslabón nuevo y enlazarlo; los demás no cambian.

**Prueba con Mockito:** `ValidadorEnCadenaTest.cadena_seDetieneEnPrimerFallo` enlaza un validador simulado después de `ValidadorBateria` y verifica con `verify(siguiente, never()).validar(any())` que no se llama cuando la batería falla.

## Decorator: telemetría de los drones

`DroneDecorator` extiende `DroneAcuatico` y delega todo en el drone que envuelve. `DroneConMonitoreo` agrega el registro de telemetría al cambiar estado, moverse o consumir batería, usando el puerto `RegistroTelemetria`. No se modifica ninguna clase de drone.

`PlanificadorRuta` envuelve en `DroneConMonitoreo` cada drone que reserva para un tramo, así queda registrada la telemetría de cada misión. Los decorators se pueden apilar.

**Pruebas con Mockito (`DroneConMonitoreoTest`):**

- Registra cada evento exactamente una vez.
- El drone decorado responde igual que el original (tipo, capacidad, batería, condiciones, texto).
- Los cambios se reflejan en el drone original.
- Al apilar decorators, las consultas no generan registros.

## Adapter: API externa de condiciones hídricas

La API externa responde con campos en inglés (`waterLevel`, `turbidity`, `agitation`, `depth`), representados en `RespuestaApiHidrica`. `AdaptadorAPIHidrica` implementa el puerto del dominio `ServicioCondicionesHidricas` y convierte esa respuesta en `CondicionesHidricas` con `NivelAgua` y `Turbidez`. El dominio no sabe que existe una API externa: el adaptador y el cliente viven en la capa de infraestructura.

**Pruebas de valores límite (`AdaptadorAPIHidricaTest`):**

| Entrada | Resultado esperado |
|---|---|
| `waterLevel` 0.5 y `turbidity` 100 | Operables (exactamente en el límite) |
| `waterLevel` 0.49 y `turbidity` 100.01 | Adversas |
| Valores negativos | Se convierten en 0 |
| Campos `null` | 0 y agitación ALTA (valor más seguro) |
| Agitación desconocida (`"storm"`) | ALTA |
| Texto en minúsculas (`"medium"`) | MEDIO |
