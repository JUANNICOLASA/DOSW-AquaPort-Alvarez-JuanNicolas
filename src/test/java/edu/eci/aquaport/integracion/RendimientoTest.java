package edu.eci.aquaport.integracion;

import edu.eci.aquaport.aplicacion.AsignadorAutomatico;
import edu.eci.aquaport.aplicacion.ConsultorFlota;
import edu.eci.aquaport.aplicacion.CoordinadorRuta;
import edu.eci.aquaport.aplicacion.PlanificadorRuta;
import edu.eci.aquaport.dominio.estrategia.PrioridadCriticaStrategy;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.validacion.ValidadorEnCadena;
import edu.eci.aquaport.infraestructura.configuracion.FlotaEjemplo;
import edu.eci.aquaport.infraestructura.hidrica.AdaptadorAPIHidrica;
import edu.eci.aquaport.infraestructura.hidrica.ClienteApiHidricaSimulado;
import edu.eci.aquaport.infraestructura.persistencia.RepositorioDronesMemoria;
import edu.eci.aquaport.infraestructura.persistencia.RepositorioMisionesMemoria;
import edu.eci.aquaport.infraestructura.telemetria.TelemetriaEnMemoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RendimientoTest {

    @Test
    @DisplayName("RNF-01: la consulta de drones disponibles responde en menos de 200 ms con 10 drones")
    void consultaFlota_menosDe200ms() {
        List<DroneAcuatico> flota = FlotaEjemplo.crearEnterprise().subList(0, 10);
        ConsultorFlota consultor = new ConsultorFlota();

        assertTimeout(Duration.ofMillis(200), () -> assertTrue(consultor.existeDisponibleConBateriaSuficiente(flota)));
    }

    @Test
    @DisplayName("RNF-04: la asignación automática elige drone en menos de 400 ms con 30 drones")
    void asignacionAutomatica_menosDe400ms() {
        List<DroneAcuatico> flota = FlotaEjemplo.crearEnterprise().subList(0, 30);
        AsignadorAutomatico asignador = new AsignadorAutomatico(new RepositorioDronesMemoria(flota),
                new AdaptadorAPIHidrica(new ClienteApiHidricaSimulado()), new PrioridadCriticaStrategy(),
                ValidadorEnCadena.estandar(zona -> true), new RepositorioMisionesMemoria());
        SolicitudTransporte solicitud = new SolicitudTransporte("S-900", ZonaHidrica.CANAL_CENTRAL,
                ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 200, Prioridad.CRITICA);

        assertTimeout(Duration.ofMillis(400), () -> assertTrue(asignador.asignar(solicitud).isPresent()));
    }

    @Test
    @DisplayName("RNF-09: la reasignación de un tramo con 60 drones tarda menos de 2 segundos")
    void reasignacion_menosDe2Segundos() {
        List<DroneAcuatico> flota = FlotaEjemplo.crearEnterprise();
        PlanificadorRuta planificador = new PlanificadorRuta(new RepositorioDronesMemoria(flota),
                new AdaptadorAPIHidrica(new ClienteApiHidricaSimulado()), ValidadorEnCadena.estandar(zona -> true),
                zona -> true, new TelemetriaEnMemoria());
        CoordinadorRuta coordinador = new CoordinadorRuta(planificador);
        RutaMultiEtapa ruta = planificador.planificar(new SolicitudMultiEtapa("S-901", ZonaHidrica.EMBALSE_NORTE,
                List.of(ZonaHidrica.CANAL_CENTRAL), ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA, 200, Prioridad.ALTA));
        coordinador.avanzar(ruta);
        ruta.getTramos().get(1).getDrone().cambiarEstado(EstadoDrone.FALLO);

        assertTimeout(Duration.ofSeconds(2), () -> coordinador.avanzar(ruta));
        assertTrue(ruta.estaTerminada());
    }
}
