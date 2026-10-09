# Prinplup · Reto 07 - Plantilla DOSW AP-07

| Campo | Contenido |
|---|---|
| **Código** | AP-07 |
| **Nombre** | Asignar automáticamente drone a misión hídrica |
| **Actor** | Solicitante (inicia), Sistema AquaPort (ejecuta), Centro de Control ECI (recibe notificación) |
| **Descripción** | El sistema elige y asigna, sin intervención del operador, el drone más adecuado para una solicitud de transporte según la zona, la carga, la prioridad y las condiciones del agua. |
| **Precondiciones** | 1. Existe una solicitud de transporte registrada. <br> 2. La flota tiene al menos un drone registrado. <br> 3. Hay una estrategia de selección activa. |
| **Postcondiciones** | La misión queda en estado PENDIENTE con un drone asignado en estado EN_MISION, o la solicitud queda sin asignar y se notifica a los observadores. |

## Datos de entrada

| Nombre | Tipo de campo | Reglas | Oblig. |
|---|---|---|---|
| solicitud | — | — | Sí |
| solicitud.id | String | Formato S-XXX | Sí |
| solicitud.origen | Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, RIBERA_ESTE, LAB_HIDRICO) | — | Sí |
| carga | — | — | Sí |
| carga.peso | Integer | Entre 1 y 1500 gramos | Sí |
| carga.tipo | Enum(MUESTRA_AGUA, SENSOR, PAQUETE_LIGERO, EQUIPO_MEDICION) | — | Sí |
| carga.prioridad | Enum(CRITICA, ALTA, NORMAL, BAJA) | — | Sí |
| zonaDestino | Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, RIBERA_ESTE, LAB_HIDRICO) | Distinta del origen | Sí |

## Datos de salida

| Nombre | Tipo de campo | Reglas | Oblig. |
|---|---|---|---|
| mision | — | — | No (salida) |
| mision.id | String | Formato M-XXX | No (salida) |
| mision.estado | Enum(PENDIENTE, EN_TRANSITO, ENTREGADA, FALLIDA) | Inicia en PENDIENTE | No (salida) |
| droneAsignado | — | Calculado por el sistema según la estrategia activa | No (salida) |
| droneAsignado.id | String | Formato AR-XX, SS-XX o BU-XX | No (salida) |
| droneAsignado.tipo | Enum(SUPERFICIAL, SEMISUMERGIDO, BUCEADOR) | — | No (salida) |
| droneAsignado.bateria | Integer | Entre 35 y 100 | No (salida) |
| condiciones | — | Consultadas a la API | No (salida) |
| condiciones.agitacion | Enum(BAJO, MEDIO, ALTO) | — | No (salida) |
| condiciones.profundidadMetros | Integer | 0 o más | No (salida) |

## Flujo básico

1. El sistema recibe la solicitud de transporte.
2. El sistema consulta las condiciones hídricas de la zona destino.
3. El sistema filtra los drones aptos para la zona: disponibles y capaces de operar en esas condiciones.
4. El sistema aplica la estrategia de selección activa sobre los drones aptos.
5. El sistema valida el drone elegido: batería, capacidad de carga y tipo compatible.
6. El sistema asigna el drone, crea la misión en estado PENDIENTE y cambia el drone a EN_MISION.
7. El sistema notifica la asignación a los observadores (Centro de Control y Técnico).

## Flujos alternos

| Código | Condición | Respuesta del sistema |
|---|---|---|
| FA-01 | Sin drones disponibles (paso 3) | La lista de candidatos queda vacía, no se crea misión y se notifica `notificarFalloAsignacion` a todos los observadores. Si la solicitud es CRÍTICA, el Centro de Control la recibe como alerta. |
| FA-02 | Condiciones hídricas adversas (paso 3) | Ningún drone puede operar con la agitación o profundidad reportada. Se descartan todos y se sigue el FA-01. |
| FA-03 | La carga supera la capacidad del tipo (paso 5) | Se descartan los drones cuyo tipo no soporta el peso (por ejemplo, buceadores para más de 300 g). Si no queda ninguno, se sigue el FA-01. |
| FA-04 | El drone elegido entra en FALLO (paso 5) | Se notifica `notificarFalloDrone` a todos los observadores, se descarta ese drone y se vuelve al paso 4. |

## Reglas de negocio

| Código | Regla |
|---|---|
| RN-03 | Un drone solo se asigna si tiene batería de 35% o más. |
| RN-04 | El drone buceador no se asigna para cargas mayores a 300 g. |
| RN-05 | Las misiones CRÍTICAS reciben el tipo de drone recomendado para la zona destino antes que el de mayor batería. |
| RN-06 | Un drone en estado distinto a DISPONIBLE no puede recibir una misión. |
