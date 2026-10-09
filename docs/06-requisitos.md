# Reto 06 - Requisitos funcionales y no funcionales

## Requisitos funcionales

| Código | Requisito |
|---|---|
| RF-01 | El Operador Hídrico consulta la flota y el sistema muestra los drones disponibles con batería mayor o igual a 35%, ordenados de mayor a menor batería. |
| RF-02 | El Operador Hídrico registra una misión de transporte indicando punto de partida, punto de llegada, tipo de carga y drone asignado, y el sistema genera un código de misión en estado PENDIENTE. |
| RF-03 | El Operador Hídrico confirma la entrega de una misión y el sistema cambia su estado a ENTREGADA y deja el drone disponible de nuevo. |

## Requisitos no funcionales

| Código | Atributo | Requisito |
|---|---|---|
| RNF-01 | Rendimiento | La consulta de drones disponibles debe retornar resultados en menos de 200 ms con una flota de hasta 10 drones, medido con `assertTimeout` de JUnit 5. |
| RNF-02 | Mantenibilidad | El código del MVP debe tener una cobertura de líneas de al menos 80% medida con JaCoCo y 0 bugs y 0 vulnerabilidades en SonarQube. |
| RNF-03 | Usabilidad | Un operador nuevo debe poder registrar una misión completa en menos de 1 minuto y en máximo 4 pasos, verificado con 3 usuarios de prueba. |

## Priorización MoSCoW

| Requisito | Categoría | Justificación |
|---|---|---|
| RF-01 Consultar flota disponible | Must Have | Sin ver qué drones están disponibles y con batería el operador no puede asignar ninguna misión. |
| RF-02 Registrar misión | Must Have | Es la función principal del MVP; sin ella el sistema no cumple su propósito. |
| RF-03 Confirmar entrega | Should Have | Es importante para cerrar el ciclo de la misión, pero el MVP puede operar al inicio registrando las entregas de forma manual. |
| RNF-01 Rendimiento de consulta | Should Have | Con solo 4 drones la consulta ya es rápida, pero conviene garantizar el tiempo de respuesta para cuando crezca la flota. |
| RNF-02 Calidad del código | Must Have | El curso exige cobertura y análisis estático, y el sistema debe poder crecer en los siguientes niveles sin acumular deuda técnica. |
| RNF-03 Usabilidad del registro | Could Have | Mejora la experiencia del operador, pero no impide que el sistema funcione si el registro toma un poco más de tiempo. |

Quedan como **Won't Have** en este nivel la asignación automática de drones y las rutas con varias etapas, que corresponden al nivel Prinplup.
