# Reto 09 - Agilismo y Jira

## Jerarquía

### Épica

**AP-E1:** Digitalizar el monitoreo y reparto hídrico de la ECI mediante una flota de drones acuáticos supervisada.

### Feature

**AP-F1:** Gestión de flota acuática.

### Historias de usuario

| Código | Historia |
|---|---|
| AP-HU1 | Como operador hídrico, quiero ver la flota con el estado, la batería y la zona de cada drone, para saber cuáles puedo usar antes de registrar una misión. |
| AP-HU2 | Como operador hídrico, quiero asignar un drone disponible a una misión de transporte, para que la muestra o el sensor llegue a la zona de destino. |
| AP-HU3 | Como operador hídrico, quiero cancelar una misión que está en estado PENDIENTE, para corregir una asignación equivocada antes de que el drone salga. |

### Subtareas de AP-HU2 (asignar drone a misión)

| Código | Subtarea |
|---|---|
| AP-ST1 | Implementar el `Mision.Builder` con validación de campos obligatorios y de disponibilidad del drone en `build()`. |
| AP-ST2 | Implementar en `ValidadorMision` la validación de batería mínima de 35% y de zona de destino válida con pruebas JUnit 5. |
| AP-ST3 | Implementar `RegistradorMisiones.registrar` que guarde la misión en `RepositorioMisiones` y muestre el mensaje de error cuando la batería no sea suficiente. |

### Criterios de aceptación de AP-HU2

1. **Dado** un drone disponible con batería mayor o igual a 35% y una zona de destino válida, **cuando** el operador lo asigna a una misión, **entonces** la misión queda registrada en estado PENDIENTE con un código generado.
2. **Dado** un drone con batería menor a 35%, **cuando** el operador intenta asignarlo, **entonces** el sistema no registra la misión y muestra el mensaje "El drone AR-XX tiene batería insuficiente (N%). Mínimo requerido: 35%."

## Tablero

| Ticket | Tipo | Estado |
|---|---|---|
| AP-E1 | Épica | En curso |
| AP-F1 | Feature | En curso |
| AP-HU1 | Historia | Finalizada |
| AP-HU2 | Historia | Finalizada |
| AP-HU3 | Historia | Por hacer |
| AP-ST1 | Subtarea | Finalizada |
| AP-ST2 | Subtarea | Finalizada |
| AP-ST3 | Subtarea | Finalizada |

## Captura

![Tablero Jira](capturas/jira-tablero.png)
