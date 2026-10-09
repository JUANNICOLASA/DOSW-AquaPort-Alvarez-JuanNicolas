package edu.eci.aquaport.servicio;

import edu.eci.aquaport.estrategia.EstrategiaSeleccion;
import edu.eci.aquaport.modelo.CondicionesHidricas;
import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.DroneSemisumergido;
import edu.eci.aquaport.modelo.DroneSuperficial;
import edu.eci.aquaport.modelo.EstadoDrone;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.Prioridad;
import edu.eci.aquaport.modelo.SolicitudTransporte;
import edu.eci.aquaport.modelo.TipoCarga;
import edu.eci.aquaport.modelo.ZonaHidrica;
import edu.eci.aquaport.observador.ObservadorMision;
import edu.eci.aquaport.repositorio.RepositorioDrones;
import edu.eci.aquaport.repositorio.RepositorioMisiones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignadorAutomaticoTest {

    @Mock
    private RepositorioDrones repositorioDrones;
    @Mock
    private ServicioCondicionesHidricas servicioCondiciones;
    @Mock
    private EstrategiaSeleccion estrategiaMock;
    @Mock
    private RepositorioMisiones repositorioMisiones;
    @Mock
    private ObservadorMision observadorMock;
    @Mock
    private ObservadorMision otroObservadorMock;

    private AsignadorAutomatico asignador;
    private SolicitudTransporte solicitud;

    @BeforeEach
    void setUp() {
        asignador = new AsignadorAutomatico(repositorioDrones, servicioCondiciones, estrategiaMock,
                new ValidadorMision(), repositorioMisiones);
        asignador.registrarObservador(observadorMock);
        solicitud = new SolicitudTransporte("S-001", ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAB_HIDRICO,
                TipoCarga.SENSOR, 200, Prioridad.NORMAL);
        when(servicioCondiciones.consultar(any())).thenReturn(CondicionesHidricas.calmas());
    }

    @Test
    @DisplayName("Asignación exitosa: el drone elegido queda EN_MISION y se notifica la asignación")
    void asignacionExitosa_creaMision() {
        DroneAcuatico drone = new DroneSuperficial("AR-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        when(repositorioDrones.listarTodos()).thenReturn(List.of(drone));
        when(estrategiaMock.seleccionar(anyList(), eq(solicitud))).thenReturn(Optional.of(drone));

        Optional<Mision> mision = asignador.asignar(solicitud);

        assertEquals("AR-01", mision.orElseThrow().getDrone().getId());
        assertEquals(EstadoDrone.EN_MISION, drone.getEstado());
        verify(repositorioMisiones).guardar(mision.get());
        verify(observadorMock, times(1)).notificarAsignacion(mision.get());
    }

    @Test
    @DisplayName("Misión CRÍTICA sin drones disponibles notifica a todos los observadores")
    void misionCritica_sinDrones_notificaObservadores() {
        SolicitudTransporte critica = new SolicitudTransporte("S-002", ZonaHidrica.CANAL_CENTRAL,
                ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA, 100, Prioridad.CRITICA);
        asignador.registrarObservador(otroObservadorMock);
        when(repositorioDrones.listarTodos()).thenReturn(List.of());
        when(estrategiaMock.seleccionar(any(), any())).thenReturn(Optional.empty());

        Optional<Mision> mision = asignador.asignar(critica);

        assertTrue(mision.isEmpty());
        verify(observadorMock, times(1)).notificarFalloAsignacion(critica);
        verify(otroObservadorMock, times(1)).notificarFalloAsignacion(critica);
    }

    @Test
    @DisplayName("Drone que entra en FALLO al ser seleccionado notifica a todos los observadores una vez y se busca otro")
    void droneEnFallo_notificaYBuscaAlternativo() {
        DroneAcuatico enFallo = new DroneSuperficial("AR-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        DroneAcuatico alternativo = new DroneSemisumergido("SS-01", 70, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        asignador.registrarObservador(otroObservadorMock);
        when(repositorioDrones.listarTodos()).thenReturn(List.of(enFallo, alternativo));
        when(estrategiaMock.seleccionar(anyList(), eq(solicitud)))
                .thenAnswer(invocacion -> {
                    enFallo.cambiarEstado(EstadoDrone.FALLO);
                    return Optional.of(enFallo);
                })
                .thenReturn(Optional.of(alternativo));

        Optional<Mision> mision = asignador.asignar(solicitud);

        assertEquals("SS-01", mision.orElseThrow().getDrone().getId());
        verify(observadorMock, times(1)).notificarFalloDrone(enFallo);
        verify(otroObservadorMock, times(1)).notificarFalloDrone(enFallo);
    }

    @Test
    @DisplayName("Con la flota vacía no se asigna misión y no se guarda nada")
    void flotaVacia_noAsigna() {
        when(repositorioDrones.listarTodos()).thenReturn(List.of());
        when(estrategiaMock.seleccionar(List.of(), solicitud)).thenReturn(Optional.empty());

        Optional<Mision> mision = asignador.asignar(solicitud);

        assertTrue(mision.isEmpty());
        verify(repositorioMisiones, never()).guardar(any());
    }

    @Test
    @DisplayName("Un drone con batería exactamente en 35% es candidato para la asignación")
    void bateriaEnUmbral_esCandidato() {
        DroneAcuatico limite = new DroneSuperficial("AR-35", 35, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        DroneAcuatico bajo = new DroneSuperficial("AR-34", 34, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        when(repositorioDrones.listarTodos()).thenReturn(List.of(limite, bajo));
        when(estrategiaMock.seleccionar(List.of(limite), solicitud)).thenReturn(Optional.of(limite));

        Optional<Mision> mision = asignador.asignar(solicitud);

        assertEquals("AR-35", mision.orElseThrow().getDrone().getId());
    }

    @Test
    @DisplayName("Un observador eliminado ya no recibe notificaciones")
    void observadorEliminado_noRecibeNotificaciones() {
        asignador.eliminarObservador(observadorMock);
        when(repositorioDrones.listarTodos()).thenReturn(List.of());
        when(estrategiaMock.seleccionar(any(), any())).thenReturn(Optional.empty());

        asignador.asignar(solicitud);

        verify(observadorMock, never()).notificarFalloAsignacion(any());
    }

    @Test
    @DisplayName("Se puede cambiar la estrategia activa sin modificar el asignador")
    void cambiarEstrategia_usaLaNueva() {
        EstrategiaSeleccion nueva = (candidatos, s) -> Optional.empty();
        when(repositorioDrones.listarTodos()).thenReturn(List.of());

        asignador.cambiarEstrategia(nueva);

        assertTrue(asignador.asignar(solicitud).isEmpty());
        verify(estrategiaMock, never()).seleccionar(any(), any());
    }
}
