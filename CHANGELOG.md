# Changelog

Generado automáticamente desde los commits con `scripts/generar-changelog.sh`.

## Sin publicar

### Construcción

- agregar script que genera CHANGELOG.md desde los commits
- agregar hook commit-msg que valida Conventional Commits

## v2.0.1 (2026-10-09)

### Correcciones

- rechazar drones con batería fuera del rango 0-100

### Pruebas

- agregar pruebas de batería fuera de rango en drones

### Mantenimiento

- preparar versión 2.0.1

## v2.0.0 (2026-10-09)

### Nuevas funcionalidades

- mostrar las consultas avanzadas de la flota en Main
- agregar consultas avanzadas con groupingBy, partitioningBy y comparadores compuestos
- demostrar la asignación automática con una flota de 15 drones en Main
- agregar repositorio de drones en memoria y condiciones hídricas simuladas
- implementar estrategias MayorBateria, ZonaCercana y PrioridadCritica
- implementar AsignadorAutomatico con estrategia y observadores
- implementar observadores del centro de control y del técnico de mantenimiento
- definir interfaz ObservadorMision para eventos de asignación y fallo
- agregar SolicitudTransporte para las misiones automáticas
- validar capacidad de carga y condiciones hídricas en ValidadorMision
- implementar FabricaDrones para crear cada tipo de drone
- agregar tipos y estados de drone, prioridad, zonas hídricas y condiciones del agua

### Correcciones

- calcular la distancia entre zonas con coordenadas double

### Refactorizaciones

- eliminar import sin uso en NotificadorOperadorTest
- generar el código de misión a partir del número de la solicitud
- convertir DroneAcuatico en clase abstracta con drones superficial, semisumergido y buceador

### Pruebas

- cubrir la rama de inmersión del drone superficial
- agregar pruebas de las consultas avanzadas de la flota
- agregar pruebas de las estrategias de selección de drone
- agregar pruebas con Mockito para AsignadorAutomatico
- agregar pruebas de los observadores de alertas

### Documentación

- documentar GitFlow de la v2 y auditoría SOLID en el README
- documentar la planeación en Jira del MVP y del sprint de la v2
- agregar prompts y mocks del flujo de asignación automática
- diseñar tarjeta de drone con sus estados y flujo de asignación aplicando Fitts y Hick
- agregar plantilla DOSW del RF AP-07
- agregar requisitos de la v2 y resolver la tensión entre AP-07 y AP-08
- agregar diagrama de casos de uso de la asignación automática
- agregar diagrama de contexto de la v2 y comparación con el MVP
- documentar streams avanzados, patrones y TDD de la v2
- documentar análisis de SonarQube de la v2
- documentar quality gate y reporte de cobertura de la v2

### Construcción

- configurar quality gate de JaCoCo con 80% de líneas por clase y 70% de ramas
- agregar Mockito y limitar JaCoCo a las clases del proyecto

### Mantenimiento

- preparar versión 2.0.0

## v1.0.0 (2026-10-09)

### Nuevas funcionalidades

- registrar misión de ejemplo desde Main usando el Builder
- agregar NotificadorOperador para mostrar mensajes al operador
- implementar RegistradorMisiones con inyección por constructor
- definir interfaz RepositorioMisiones con implementación en memoria
- implementar validación de punto de llegada y disponibilidad
- implementar validación de batería en ValidadorMision
- implementar Mision con Builder y validación en build()
- agregar enums TipoCarga y EstadoMision
- agregar Main que imprime las consultas de la flota
- implementar ConsultorFlota con 5 consultas Stream
- crear proyecto Maven y modelo DroneAcuatico

### Refactorizaciones

- reemplazar System.out por Logger en NotificadorOperador y Main
- reemplazar literal duplicado del modelo por constante en Main
- registrar una misión válida y una inválida desde Main
- extraer batería mínima y zonas válidas a constantes en ValidadorMision

### Pruebas

- agregar pruebas de NotificadorOperador y Main
- agregar pruebas del Builder de Mision
- agregar pruebas de ConsultorFlota
- agregar pruebas de RegistradorMisiones
- agregar pruebas de punto de llegada, zona y disponibilidad
- agregar pruebas de batería para ValidadorMision

### Documentación

- agregar índice de retos y guía de ejecución al README
- crear carpeta para capturas del uso de IA
- agregar resultados del análisis de SonarQube
- actualizar diagrama de clases y salida del programa tras usar Logger
- definir épica, feature, historias y subtareas para Jira
- agregar mocks del panel de monitoreo con sus tres estados
- definir manual de identidad de AquaPort
- documentar estructura de ramas GitFlow
- documentar retos de streams, builder y TDD
- agregar diagrama de casos de uso del RF AP-01
- agregar plantilla DOSW del RF AP-01
- agregar requisitos funcionales, no funcionales y priorización MoSCoW
- agregar diagrama de contexto C4 del MVP
- agregar reporte de cobertura JaCoCo
- agregar diagrama de clases y explicación de SRP y DIP

### Construcción

- configurar análisis con SonarQube
- configurar JaCoCo para medir la cobertura de pruebas
- agregar JUnit 5 y plugin surefire

### Mantenimiento

- configurar repositorio con README y .gitignore para Java

