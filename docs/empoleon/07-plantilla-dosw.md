# Empoleon · Reto 07 - Plantilla DOSW AP-15

| Campo | Contenido |
|---|---|
| **Código** | AP-15 |
| **Nombre** | Ejecutar misión multi-etapa con waypoints |
| **Actor** | Solicitante (inicia), Operador Hídrico (supervisa), Centro de Control ECI y Laboratorio Hídrico (reciben información) |
| **Descripción** | El sistema planifica una ruta con waypoints intermedios, asigna un drone por tramo, coordina los traspasos de la muestra y la entrega en el destino manteniendo la cadena de custodia. |
| **Precondiciones** | 1. La zona destino está activa. <br> 2. Existe al menos un drone disponible y apto para cada tramo. <br> 3. La API de condiciones hídricas responde. |
| **Postcondiciones** | La ruta queda ENTREGADA con la cadena de custodia completa, o FALLIDA con la custodia interrumpida y el motivo registrado. |

## Datos de entrada

| Nombre | Tipo de campo | Reglas | Oblig. |
|---|---|---|---|
| solicitud | — | — | Sí |
| solicitud.id | String | Formato S-XXX | Sí |
| solicitud.origen | Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, RIBERA_ESTE, LAB_HIDRICO) | — | Sí |
| solicitud.waypoints | List<Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, RIBERA_ESTE, LAB_HIDRICO)> | En orden de paso; puede estar vacía | Sí |
| solicitud.destino | Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, RIBERA_ESTE, LAB_HIDRICO) | Zona activa | Sí |
| carga | — | — | Sí |
| carga.peso | Integer | Entre 1 y 1500 gramos | Sí |
| carga.tipo | Enum(MUESTRA_AGUA, SENSOR, PAQUETE_LIGERO, EQUIPO_MEDICION) | — | Sí |
| carga.prioridad | Enum(CRITICA, ALTA, NORMAL, BAJA) | — | Sí |

## Datos de salida

| Nombre | Tipo de campo | Reglas | Oblig. |
|---|---|---|---|
| ruta | — | — | No (salida) |
| ruta.id | String | Formato R-XXX | No (salida) |
| ruta.estado | Enum(PENDIENTE, EN_TRANSITO, ENTREGADA, FALLIDA) | — | No (salida) |
| ruta.tramos | List<Tramo(orden:Integer, origen:ZonaHidrica, destino:ZonaHidrica, drone:DroneAcuatico(id:String, tipo:TipoDrone, bateria:Integer), estado:EstadoMision)> | Un drone por tramo | No (salida) |
| ruta.desvios | List<ZonaHidrica> | Waypoints evitados | No (salida) |
| ruta.custodia | List<RegistroCustodia(punto:ZonaHidrica, droneEntrega:String, droneRecibe:String)> | Desde "Solicitante" hasta "Receptor en destino" | No (salida) |

## Flujo básico

1. El solicitante envía la solicitud con origen, waypoints, destino y carga.
2. El sistema verifica que la zona destino esté activa.
3. El sistema consulta las condiciones hídricas de cada waypoint y descarta los que sean adversos o estén inactivos.
4. El sistema divide la ruta en tramos consecutivos.
5. Para cada tramo, el sistema valida los drones disponibles con la cadena (batería, carga, zona activa, condiciones) y asigna el más cercano al origen del tramo.
6. El sistema reserva cada drone (EN_MISION) y lo envuelve en el monitoreo de telemetría.
7. Al iniciar cada tramo, el sistema registra el traspaso de la muestra en la cadena de custodia.
8. El drone recorre el tramo, consume batería y llega al waypoint; el sistema notifica la llegada al centro de control.
9. El drone que terminó su tramo queda disponible (o en recarga si su batería es menor a 20%).
10. Al completar el último tramo, el sistema registra la entrega al receptor en destino, marca la ruta como ENTREGADA y envía la cadena de custodia al laboratorio.

## Flujos alternos

| Código | Condición | Respuesta del sistema |
|---|---|---|
| FA-01 | Fallo de drone en waypoint intermedio (paso 7) | El drone del siguiente tramo está en FALLO. El sistema busca el drone apto más cercano al waypoint, reasigna el tramo, notifica la reasignación con el motivo y continúa desde el paso 7. |
| FA-02 | Condición hídrica adversa en un tramo específico (paso 3) | El waypoint tiene turbidez mayor a 100 NTU o nivel de agua menor a 0.5 m. El sistema lo excluye, une el tramo anterior con el siguiente (desvío) y lo registra en `ruta.desvios`. |
| FA-03 | Zona de destino temporalmente inactiva (paso 2) | El sistema rechaza la solicitud con el mensaje "La zona destino X está inactiva" y no reserva drones. |
| FA-04 | Batería crítica en mitad de la ruta (paso 7) | El drone del tramo tiene menos de 20% de batería. Se reasigna como en FA-01 y el drone agotado pasa a RECARGANDO. |
| FA-05 | Cadena de custodia interrumpida (pasos 7 y FA-01) | No hay drone receptor para el traspaso. La ruta pasa a FALLIDA, la custodia queda interrumpida con el motivo, la muestra permanece con el último poseedor y se envía una alerta. |

## Reglas de negocio

| Código | Regla |
|---|---|
| RN-07 | Cadena de custodia: ningún tramo puede iniciar sin registrar el traspaso desde el poseedor actual; el primer registro sale del "Solicitante" y el último llega al "Receptor en destino". |
| RN-08 | Umbral de reasignación por batería: un drone con menos de 20% de batería no puede iniciar un tramo. |
| RN-09 | Umbral de reasignación por estado: un drone en FALLO se reemplaza siempre, sin importar su batería. |
| RN-10 | El reemplazo es el drone disponible y apto más cercano al waypoint donde ocurre el traspaso; si hay empate, el de mayor batería. |
| RN-11 | Un waypoint es adverso si la turbidez supera 100 NTU o el nivel del agua es menor a 0.5 m. |
| RN-12 | Cada drone asignado a un tramo debe tener al menos 35% de batería al momento de la planificación. |
