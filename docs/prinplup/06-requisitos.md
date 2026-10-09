# Prinplup · Reto 06 - Requisitos de la v2

## Requisitos funcionales

| Código | Requisito |
|---|---|
| AP-07 | El sistema asigna automáticamente el drone de mayor batería disponible para cualquier misión hídrica, después de descartar los drones sin batería suficiente, sin capacidad para la carga o que no pueden operar en las condiciones del agua. |
| AP-08 | Las misiones CRÍTICAS tienen prioridad absoluta: el sistema les asigna el drone técnicamente más apto para la zona destino, independientemente de la batería. |
| AP-09 | Cuando un drone entra en estado FALLO, el sistema envía una alerta al Centro de Control y al Técnico de Mantenimiento con el id del drone y su zona. |
| AP-10 | Antes de asignar, el sistema consulta las condiciones hídricas de la zona destino (agitación y profundidad requerida) y solo considera los drones que pueden operar en ellas. |
| AP-11 | El Técnico de Mantenimiento consulta las órdenes de mantenimiento generadas y marca el drone como DISPONIBLE cuando termina la reparación. |

## Requisitos no funcionales

| Código | Atributo | Requisito |
|---|---|---|
| RNF-04 | Rendimiento | La asignación automática debe seleccionar el drone óptimo en menos de 400 ms para una flota de hasta 30 drones, medido con JUnit 5 y `assertTimeout`. |
| RNF-05 | Disponibilidad de alertas | La alerta de un drone en FALLO debe llegar a todos los observadores registrados en menos de 2 segundos desde que se detecta. |
| RNF-06 | Mantenibilidad | Agregar un nuevo tipo de drone no debe requerir cambios en `AsignadorAutomatico` ni en `ValidadorMision`; se verifica en la revisión del PR. |
| RNF-07 | Calidad | La v2 debe mantener 80% o más de cobertura de líneas por clase (quality gate de JaCoCo) y deuda técnica menor a 30 minutos en SonarQube. |

## Priorización MoSCoW

| Requisito | Categoría | Justificación |
|---|---|---|
| AP-07 Asignación automática | Must Have | Es el objetivo principal de la v2: el sistema decide y el operador supervisa. |
| AP-08 Prioridad crítica | Must Have | Una muestra crítica mal asignada puede perderse; es una regla del negocio que no se puede omitir. |
| AP-10 Condiciones hídricas | Must Have | Sin esta información se podría enviar un drone superficial a aguas agitadas. |
| AP-09 Alertas de fallo | Should Have | Es muy importante para la operación, pero el sistema puede asignar misiones aunque las alertas se atiendan manualmente al inicio. |
| AP-11 Gestión por técnico | Could Have | Mejora el seguimiento del mantenimiento, pero el administrador puede cambiar el estado del drone mientras tanto. |
| RNF-04 Rendimiento | Must Have | La asignación automática pierde sentido si tarda más que una asignación manual. |
| RNF-05 Tiempo de alertas | Should Have | Una alerta tardía reduce su utilidad, pero el fallo sigue quedando registrado. |
| RNF-06 Extensibilidad | Should Have | La flota va a crecer con nuevos tipos en el nivel Enterprise. |
| RNF-07 Calidad | Must Have | Es una exigencia del curso y protege la evolución del sistema. |

**Won't Have en la v2:** rutas multi-etapa con waypoints y coordinación entre zonas (nivel Empoleon).

## Tensión entre AP-07 y AP-08

AP-07 pide el drone con **mayor batería** y AP-08 pide el drone **más apto para la zona**. No se contradicen, pero para una misión crítica pueden dar respuestas distintas. Por ejemplo, hacia el Embalse Norte el drone con más batería puede ser un superficial, mientras que el más apto es un buceador.

**Resolución:** la prioridad decide la regla.

1. Para misiones CRÍTICAS manda AP-08: se elige el tipo recomendado para la zona y, entre los de ese tipo, el de más batería.
2. Para las demás prioridades manda AP-07: se elige la mayor batería.
3. En ambos casos se mantienen las reglas mínimas (batería de 35% o más, capacidad de carga y condiciones del agua), así que AP-08 nunca asigna un drone inseguro.

Esto quedó implementado en `PrioridadCriticaStrategy`, sin cambiar el `AsignadorAutomatico`.
