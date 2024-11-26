package com.empresa.giros.procesamientodatosgiros.service;

import com.empresa.giros.procesamientodatosgiros.entity.EpsEntity;
import com.empresa.giros.procesamientodatosgiros.entity.GiroEntity;
import com.empresa.giros.procesamientodatosgiros.entity.IpsEntity;
import com.empresa.giros.procesamientodatosgiros.entity.UbicacionEntity;
import com.empresa.giros.procesamientodatosgiros.repository.EpsRepository;
import com.empresa.giros.procesamientodatosgiros.repository.GiroRepository;
import com.empresa.giros.procesamientodatosgiros.repository.IpsRepository;
import com.empresa.giros.procesamientodatosgiros.repository.UbicacionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public final class ProcesamientoService {

    private final EpsRepository epsRepository;
    private final IpsRepository ipsRepository;
    private final UbicacionRepository ubicacionRepository;
    private final GiroRepository giroRepository;

    public ProcesamientoService(final EpsRepository epsRepository, final IpsRepository ipsRepository,
                                final UbicacionRepository ubicacionRepository, final GiroRepository giroRepository) {
        this.epsRepository = epsRepository;
        this.ipsRepository = ipsRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.giroRepository = giroRepository;
    }

    public void procesarRegistros(final List<Map<String, String>> registros, final String fecha) {
        for (Map<String, String> registro : registros) {
            // Guardar EPS
            EpsEntity eps = guardarEps(registro);

            // Guardar IPS
            IpsEntity ips = guardarIps(registro);

            // Guardar ubicación
            UbicacionEntity ubicacion = guardarUbicacion(registro);

            // Guardar giro
            guardarGiro(registro, fecha, eps, ips, ubicacion);
        }
    }

    private EpsEntity guardarEps(final Map<String, String> registro) {
        String codEps = obtenerValorOAsignarDefault(registro.get("COD_EPS"));
        String nombreEps = obtenerValorOAsignarDefault(registro.get("NOMBRE_EPS"));

        return epsRepository.findByCodEps(codEps)
                .orElseGet(() -> {
                    EpsEntity nuevaEps = new EpsEntity();
                    nuevaEps.setCodEps(codEps);
                    nuevaEps.setNombreEps(nombreEps);
                    return epsRepository.save(nuevaEps);
                });
    }

    private IpsEntity guardarIps(final Map<String, String> registro) {
        String nitIps = obtenerValorOAsignarDefault(registro.get("NIT_EPS"));
        String nombreIps = obtenerValorOAsignarDefault(registro.get("NOMBRE_IPS"));

        return ipsRepository.findByNitIps(nitIps)
                .orElseGet(() -> {
                    IpsEntity nuevaIps = new IpsEntity();
                    nuevaIps.setNitIps(nitIps);
                    nuevaIps.setNombreIps(nombreIps);
                    return ipsRepository.save(nuevaIps);
                });
    }

    private UbicacionEntity guardarUbicacion(final Map<String, String> registro) {
        String dane = obtenerValorOAsignarDefault(registro.get("DANE"));
        String departamento = obtenerValorOAsignarDefault(registro.get("DEPARTAMENTO"));
        String municipio = obtenerValorOAsignarDefault(registro.get("MUNICIPIO"));

        return ubicacionRepository.findByDane(dane)
                .orElseGet(() -> {
                    UbicacionEntity nuevaUbicacion = new UbicacionEntity();
                    nuevaUbicacion.setDane(dane);
                    nuevaUbicacion.setDepartamento(departamento);
                    nuevaUbicacion.setMunicipio(municipio);
                    return ubicacionRepository.save(nuevaUbicacion);
                });
    }

    private void guardarGiro(final Map<String, String> registro, final String fecha,
                             EpsEntity eps, IpsEntity ips, UbicacionEntity ubicacion) {
        String formaContratacion = obtenerValorOAsignarDefault(registro.get("FORMA_CONTRATACION"));
        Double totalGiro = parseDouble(registro.get("TOTAL_GIRO"));
        String observaciones = obtenerValorOAsignarDefault(registro.get("OBSERVACIONES"));

        GiroEntity giro = new GiroEntity();
        giro.setEpsEntity(eps);
        giro.setIpsEntity(ips);
        giro.setUbicacionEntity(ubicacion);
        giro.setFormaContratacion(formaContratacion);
        giro.setTotalGiro(totalGiro);
        giro.setObservaciones(observaciones);
        giro.setFecha(fecha);

        giroRepository.save(giro);
    }

    private Double parseDouble(final String valor) {
        if (valor == null || valor.isBlank()) return 0.0;
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String obtenerValorOAsignarDefault(final String valor) {
        return (valor == null || valor.isBlank()) ? "N/A" : valor;
    }
}
