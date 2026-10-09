# Empoleon · Reto 02 - Historial de git limpio y trazable

## Conventional Commits

Todos los commits de la v3 siguen el formato `tipo(alcance): descripción` con los tipos `feat`, `fix`, `refactor`, `test`, `docs`, `build`, `chore`, `ci`, `perf`, `style` y `revert`. Cuando un commit corrige un issue de calidad, el cuerpo explica la causa raíz, la corrección y por qué es la adecuada.

## Hook de validación

El hook está en `.githooks/commit-msg`. El script `scripts/instalar-hooks.sh` configura `core.hooksPath` para que git use la carpeta `.githooks` del repositorio, así el hook se comparte con todo el equipo. Se usa la etapa `commit-msg` y no `pre-commit` porque el mensaje solo está disponible en esa etapa: `pre-commit` se ejecuta antes de escribirlo. El hook revisa la primera línea con esta expresión regular y, si no coincide, cancela el commit y muestra el formato esperado:

```
^(feat|fix|refactor|test|docs|build|chore|ci|perf|style|revert)(\([a-z0-9-]+\))?!?: .{1,100}$
```

Prueba hecha al instalarlo:

```
$ git commit -m "malo"
Mensaje de commit inválido: malo
Formato esperado: tipo(alcance opcional): descripción
```

## Protección de ramas

`main` y `develop` están protegidas en GitHub (Settings → Branches):

| Regla | Valor |
|---|---|
| Requiere Pull Request para integrar | Sí |
| Aplica también a administradores | Sí |
| Permite push forzado | No |
| Permite borrar la rama | No |

Con estas reglas no es posible hacer push directo a `main` ni a `develop`; todo entra por PR.

## Estrategia de integración

| Integración | Estrategia | Motivo |
|---|---|---|
| `feature/*` → `develop` (v3) | Rebase merge | El historial queda lineal, sin commits de merge, y cada commit conserva su mensaje descriptivo. |
| `release/v3.0` → `main` | Merge con mensaje convencional | Deja un punto claro de integración de la versión, donde se pone el tag. |
| Tags | Versionado semántico | `v1.0.0` (MVP), `v2.0.0` (v2), `v2.0.1` (hotfix), `v3.0.0` (Enterprise). |

## CHANGELOG automático

`scripts/generar-changelog.sh` recorre los tags y agrupa los commits por tipo (nuevas funcionalidades, correcciones, refactorizaciones, pruebas, documentación, construcción y mantenimiento). Para la release se generó con el nombre de versión `v3.0.0`. Resultado: [CHANGELOG.md](../../CHANGELOG.md)

## Log de git de la v3

`git log --no-merges --oneline` desde el primer commit de Enterprise:

```
1c17674 chore(release): preparar versión 3.0.0
c6a611b build(changelog): permitir nombrar la versión que se está preparando
cbd61a1 docs(jira): documentar roadmap Enterprise con tres sprints, capacidad, DoD y retrospectiva
992e271 docs(ux): agregar prompts de los mocks del flujo Enterprise
ee2c51d docs(ux): construir el design system Enterprise con tokens, contraste WCAG AA y mapa de flujos
69ddae0 docs(arquitectura): documentar la auditoría de capas y el índice de Empoleon en el README
906e97a test(rendimiento): medir el tiempo de la consulta de drones disponibles del MVP
4449225 test(rendimiento): medir con assertTimeout la asignación automática y la reasignación de tramos
a233207 docs(dosw): agregar plantilla del RF AP-15 misión multi-etapa con waypoints
c5c15f7 docs(requisitos): agregar requisitos Enterprise con SLA y tensiones entre RF
740db19 docs: documentar streams, patrones y pruebas de Enterprise
d877300 docs(arquitectura): documentar auditoría SOLID y mapa de dependencias entre capas
74ecf09 docs(casos-de-uso): agregar casos de uso Enterprise con los cinco flujos de fallo
7aa1292 docs(c4): agregar diagrama de contenedores de AquaPort Enterprise
f298562 docs(calidad): documentar el quality gate Enterprise y la evolución de las métricas
5207ec8 refactor(dominio): separar la validación de campos del método build de Mision
559c516 test(rutas): cubrir el desvío por waypoint en zona inactiva
4ba8d50 build(calidad): subir el quality gate de JaCoCo a 85% de líneas y 75% de ramas
e02fc5c feat(estadisticas): calcular eficiencia por zona, mejor drone y carga de waypoints
165c34e feat(estadisticas): agregar collector que promedia la batería por zona en una sola pasada
1c90751 test(integracion): probar el flujo multi-etapa completo y sus cinco flujos alternos
2eab5f8 feat(configuracion): agregar flota Enterprise de 60 drones y demostrar una ruta multi-etapa
95b8320 feat(notificacion): informar al centro de control el avance y los fallos de las rutas
389e6cd feat(rutas): ejecutar rutas con reasignación automática ante fallo o batería crítica
c363b46 feat(rutas): planificar cada tramo con el drone apto más cercano y desviar waypoints adversos
5965a22 feat(rutas): modelar rutas multi-etapa con tramos, waypoints y cadena de custodia
bee4c6f refactor(hidrica): obtener las condiciones del agua desde el adaptador de la API
0a25397 feat(infraestructura): agregar monitor de zonas y registro de telemetría en memoria
55f08a5 feat(hidrica): aislar la API externa de condiciones hídricas con AdaptadorAPIHidrica
4a63931 feat(telemetria): registrar la telemetría de los drones con el decorator DroneConMonitoreo
d33c5be refactor(asignacion): usar la cadena de validación en AsignadorAutomatico
3351699 feat(validacion): validar misiones con una cadena de batería, carga, zona activa y condiciones
700162e feat(dominio): agregar nivel del agua y turbidez a las condiciones hídricas
0e11381 test(arquitectura): verificar dirección de dependencias entre capas y dominio sin librerías externas
fbb80ec build: agregar ArchUnit para verificar la arquitectura por capas
529e2a7 docs: actualizar la clase principal en la guía de ejecución
aa025c0 refactor(arquitectura): organizar el código en capas de dominio, aplicación e infraestructura
1f85d76 docs: generar CHANGELOG.md inicial
24e453e fix(changelog): excluir de la sección sin publicar los commits que ya tienen tag
8192e8b ci: agregar script que genera CHANGELOG.md desde los commits
e73a645 ci: agregar hook commit-msg que valida Conventional Commits
```
