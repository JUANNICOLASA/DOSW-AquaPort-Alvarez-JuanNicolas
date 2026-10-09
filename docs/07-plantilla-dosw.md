# Reto 07 - Plantilla DOSW

| Campo | Contenido |
|---|---|
| **Código** | AP-01 |
| **Nombre** | Registrar misión de transporte de muestra |
| **Actor** | Operador Hídrico |
| **Descripción** | Permite al operador registrar una misión de transporte entre dos zonas del campus asignando manualmente un drone disponible. |
| **Precondiciones** | 1. El operador ha ingresado al sistema. <br> 2. Debe existir al menos un drone disponible con batería mayor o igual a 35%. <br> 3. Existe una solicitud de transporte hecha por un Solicitante. |
| **Postcondiciones** | La misión queda registrada en estado PENDIENTE y el drone asignado queda reservado para esa misión. |

## Datos de entrada

| Dato | Tipo | Obligatorio | Descripción |
|---|---|---|---|
| drone | DroneAcuatico(id:String, modelo:String, bateria:int, disponible:boolean, zona:String) | Sí | Drone seleccionado por el operador |
| puntoPartida | Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, PUNTO_RIBERENO_ESTE, LABORATORIO_HIDRICO) | Sí | Zona desde donde sale el drone |
| puntoLlegada | Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, PUNTO_RIBERENO_ESTE, LABORATORIO_HIDRICO) | Sí | Zona a la que se entrega la carga |
| tipoCarga | Enum(MUESTRA_AGUA, SENSOR, PAQUETE_LIGERO) | Sí | Tipo de carga transportada |

## Datos de salida

| Dato | Tipo | Descripción |
|---|---|---|
| codigoMision | String (formato M-XXX) | Código único generado para la misión |
| estado | Enum(PENDIENTE, EN_TRANSITO, ENTREGADA, FALLIDA) | Estado inicial de la misión, siempre PENDIENTE |
| dronesDisponibles | List<DroneAcuatico(id:String, bateria:int, zona:String)> | Lista de drones que el operador puede elegir |

## Flujo básico

1. El operador selecciona la opción de registrar misión.
2. El sistema muestra la lista de drones disponibles con su batería y zona actual.
3. El operador ingresa el punto de partida, el punto de llegada, el tipo de carga y elige un drone.
4. El sistema valida la batería del drone, su disponibilidad y que la zona de destino sea válida.
5. El sistema registra la misión en estado PENDIENTE y muestra el código de misión generado.

## Flujos alternos

| Código | Condición | Respuesta del sistema |
|---|---|---|
| FA-01 | El drone elegido tiene batería menor a 35% | En el paso 4 el sistema muestra: "El drone AR-03 tiene batería insuficiente (18%). Mínimo requerido: 35%." y vuelve al paso 3 para elegir otro drone. |
| FA-02 | La zona de destino no es una zona válida del campus | En el paso 4 el sistema muestra: "La zona indicada no es una zona válida de AquaPort" y vuelve al paso 3 para corregir el punto de llegada. |

## Reglas de negocio

| Código | Regla |
|---|---|
| RN-01 | Un drone no puede tener más de una misión activa al mismo tiempo. |
| RN-02 | El punto de partida y el punto de llegada de una misión no pueden ser la misma zona. |

## Diferencia entre precondición y regla de negocio

La precondición "debe haber al menos un drone disponible con batería mayor o igual a 35%" se revisa antes de iniciar; si no se cumple, el caso de uso no puede empezar. Las reglas de negocio, en cambio, se aplican durante la ejecución, cuando el operador ya está registrando la misión.
