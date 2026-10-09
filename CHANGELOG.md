# Changelog

Generado automáticamente desde los commits con `scripts/generar-changelog.sh`.

## v3.0.0 (2026-10-09)

### Nuevas funcionalidades

- calcular eficiencia por zona, mejor drone y carga de waypoints
- agregar collector que promedia la batería por zona en una sola pasada
- agregar flota Enterprise de 60 drones y demostrar una ruta multi-etapa
- informar al centro de control el avance y los fallos de las rutas
- ejecutar rutas con reasignación automática ante fallo o batería crítica
- planificar cada tramo con el drone apto más cercano y desviar waypoints adversos
- modelar rutas multi-etapa con tramos, waypoints y cadena de custodia
- agregar monitor de zonas y registro de telemetría en memoria
- aislar la API externa de condiciones hídricas con AdaptadorAPIHidrica
- registrar la telemetría de los drones con el decorator DroneConMonitoreo
- validar misiones con una cadena de batería, carga, zona activa y condiciones
- agregar nivel del agua y turbidez a las condiciones hídricas

### Correcciones

- excluir de la sección sin publicar los commits que ya tienen tag

### Refactorizaciones

- separar la validación de campos del método build de Mision
- obtener las condiciones del agua desde el adaptador de la API
- usar la cadena de validación en AsignadorAutomatico
- organizar el código en capas de dominio, aplicación e infraestructura

### Pruebas

- medir el tiempo de la consulta de drones disponibles del MVP
- medir con assertTimeout la asignación automática y la reasignación de tramos
- cubrir el desvío por waypoint en zona inactiva
- probar el flujo multi-etapa completo y sus cinco flujos alternos
- verificar dirección de dependencias entre capas y dominio sin librerías externas

### Documentación

- documentar Conventional Commits, hook, protección de ramas y log de la v3
- documentar roadmap Enterprise con tres sprints, capacidad, DoD y retrospectiva
- agregar prompts de los mocks del flujo Enterprise
- construir el design system Enterprise con tokens, contraste WCAG AA y mapa de flujos
- documentar la auditoría de capas y el índice de Empoleon en el README
- agregar plantilla del RF AP-15 misión multi-etapa con waypoints
- agregar requisitos Enterprise con SLA y tensiones entre RF
- documentar streams, patrones y pruebas de Enterprise
- documentar auditoría SOLID y mapa de dependencias entre capas
- agregar casos de uso Enterprise con los cinco flujos de fallo
- agregar diagrama de contenedores de AquaPort Enterprise
- documentar el quality gate Enterprise y la evolución de las métricas
- actualizar la clase principal en la guía de ejecución
- generar CHANGELOG.md inicial

### Construcción

- permitir nombrar la versión que se está preparando
- subir el quality gate de JaCoCo a 85% de líneas y 75% de ramas
- agregar ArchUnit para verificar la arquitectura por capas
- agregar script que genera CHANGELOG.md desde los commits
- agregar hook commit-msg que valida Conventional Commits

### Mantenimiento

- preparar versión 3.0.0

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

