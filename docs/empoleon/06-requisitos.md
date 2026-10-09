# Empoleon · Reto 06 - Requisitos Enterprise

## Requisitos funcionales

| Código | Requisito |
|---|---|
| AP-15 | El sistema ejecuta misiones multi-etapa: divide la ruta (origen, waypoints, destino) en tramos y asigna a cada tramo el drone apto más cercano a su origen. |
| AP-16 | Cuando el drone de un tramo está en FALLO o tiene batería menor a 20% en un waypoint, el sistema reasigna automáticamente el tramo al drone apto más cercano y notifica al centro de control. |
| AP-17 | El sistema registra la cadena de custodia completa de cada muestra: en cada traspaso guarda el waypoint, el drone que entrega y el que recibe, desde el solicitante hasta el receptor en destino. |
| AP-18 | El sistema coordina las 4 zonas hídricas: no planifica rutas hacia zonas inactivas y desvía la ruta cuando un waypoint tiene condiciones adversas. |
| AP-19 | El Administrador ECI consulta reportes de eficiencia por zona (entregadas / totales), el drone con mejor historial y la batería promedio por zona. |

## Requisitos no funcionales

| Código | Atributo | Requisito |
|---|---|---|
| RNF-08 | Disponibilidad (SLA) | El servicio de planificación y ejecución de rutas debe estar disponible el 99.5% del tiempo mensual (máximo 3 h 39 min de caída al mes), medido con el monitor de salud del despliegue. |
| RNF-09 | Tiempo de reasignación | Ante un fallo en un waypoint, la reasignación debe completarse en menos de 2 segundos en el percentil 95 con la flota de 60 drones, medido con `assertTimeout` en la prueba de integración y con métricas en producción. |
| RNF-10 | Throughput | El sistema debe coordinar al menos 20 misiones multi-etapa simultáneas con 60 drones sin que la planificación supere 500 ms por misión. |
| RNF-11 | Trazabilidad | El 100% de los traspasos debe quedar en la cadena de custodia antes de que el tramo inicie; ningún tramo puede ejecutarse sin su registro. |
| RNF-12 | Mantenibilidad | JaCoCo de 85% o más en líneas y 75% o más en ramas; SonarQube con 0 bugs, 0 vulnerabilidades, deuda menor a 15 min y duplicación menor a 3%; métodos de 15 líneas o menos y clases de 150 líneas o menos. |

## Priorización MoSCoW

| Requisito | Categoría | Justificación |
|---|---|---|
| AP-15 Misión multi-etapa | Must Have | Es la funcionalidad que define el nivel Enterprise. |
| AP-16 Reasignación automática | Must Have | Sin ella, un fallo en un waypoint deja la muestra abandonada. |
| AP-17 Cadena de custodia | Must Have | El laboratorio no acepta muestras sin trazabilidad. |
| AP-18 Coordinación de zonas | Should Have | Evita rutas imposibles; al inicio el operador podría bloquear zonas a mano. |
| AP-19 Reportes de eficiencia | Could Have | Apoya la planeación, pero no afecta la operación diaria. |
| RNF-08 SLA | Must Have | Las muestras tienen tiempo de vida limitado; una caída larga las pierde. |
| RNF-09 Reasignación | Must Have | El drone en fallo puede estar a la deriva con la muestra. |
| RNF-10 Throughput | Should Have | Se necesita en temporada de muestreo; el resto del año la carga es menor. |
| RNF-11 Trazabilidad | Must Have | Es condición para la validez de la muestra. |
| RNF-12 Mantenibilidad | Must Have | Es el estándar de calidad del nivel. |

## Tensiones entre requisitos

| Requisitos | Tensión | Resolución |
|---|---|---|
| AP-16 vs AP-17 | Reasignar rápido puede llevar a mover la muestra antes de dejar el registro. | El traspaso se registra al inicio de cada tramo, después de la reasignación y antes de recorrerlo (`CoordinadorRuta.avanzar`). Así la custodia siempre refleja al drone que realmente tiene la muestra. |
| AP-15 vs AP-18 | El drone "más cercano" puede pertenecer a otra zona y cruzar zonas ocupadas, mientras que la coordinación busca usar los drones de cada zona. | La cercanía se mide desde el origen del tramo, por lo que normalmente gana un drone de la misma zona. La cadena de validación descarta los drones que no pueden operar en la zona destino. |
| AP-16 vs prioridad de otras misiones | Un reemplazo toma un drone que podría servir a otra misión. | Se toma el más cercano disponible y apto. Las misiones CRÍTICAS siguen teniendo precedencia en la asignación (regla AP-08 de la v2). |
| AP-18 (desvío) vs RNF-09/SLA | Desviar una ruta alarga el recorrido y el tiempo de entrega. | El desvío se decide en la planificación, no durante la ejecución, y se informa en `RutaMultiEtapa.getDesvios()` para que el centro de control vea la demora. |
| AP-19 vs RNF-10 | Los reportes sobre el historial completo consumen recursos en horas de operación. | Los reportes se calculan con streams en una sola pasada (collector propio) y, para el historial completo, en paralelo y fuera de las horas pico. |
