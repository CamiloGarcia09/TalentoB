package com.empresa.giros.procesamientodatosgiros.excel;

import com.empresa.giros.procesamientodatosgiros.entity.EpsEntity;
import com.empresa.giros.procesamientodatosgiros.entity.GiroEntity;
import com.empresa.giros.procesamientodatosgiros.entity.IpsEntity;
import com.empresa.giros.procesamientodatosgiros.entity.UbicacionEntity;
import com.empresa.giros.procesamientodatosgiros.repository.EpsRepository;
import com.empresa.giros.procesamientodatosgiros.repository.GiroRepository;
import com.empresa.giros.procesamientodatosgiros.repository.IpsRepository;
import com.empresa.giros.procesamientodatosgiros.repository.UbicacionRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class ExcelProcessor {

    private final EpsRepository epsRepository;
    private final IpsRepository ipsRepository;
    private final UbicacionRepository ubicacionRepository;
    private final GiroRepository giroRepository;

    public ExcelProcessor(EpsRepository epsRepository,
                          IpsRepository ipsRepository,
                          UbicacionRepository ubicacionRepository,
                          GiroRepository giroRepository) {
        this.epsRepository = epsRepository;
        this.ipsRepository = ipsRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.giroRepository = giroRepository;
    }

    public void procesarArchivoYGuardar(String rutaArchivo, String fecha) throws IOException {
        List<Map<String, String>> registros = procesarArchivo(rutaArchivo, fecha);

        for (Map<String, String> registro : registros) {

            // Guardar EPS
            EpsEntity eps = epsRepository.findByCodEps(obtenerValorOAsignarDefault(registro.get("COD_EPS")))
                    .orElseGet(() -> {
                        EpsEntity nuevaEps = new EpsEntity();
                        nuevaEps.setCodEps(obtenerValorOAsignarDefault(registro.get("COD_EPS")));
                        nuevaEps.setNombreEps(obtenerValorOAsignarDefault(registro.get("NOMBRE_EPS")));
                        return epsRepository.save(nuevaEps);
                    });

            // Guardar IPS
            IpsEntity ips = ipsRepository.findByNitIps(obtenerValorOAsignarDefault(registro.get("NIT_EPS")))
                    .orElseGet(() -> {
                        IpsEntity nuevaIps = new IpsEntity();
                        nuevaIps.setNitIps(obtenerValorOAsignarDefault(registro.get("NIT_EPS")));
                        nuevaIps.setNombreIps(obtenerValorOAsignarDefault(registro.get("NOMBRE_IPS")));
                        return ipsRepository.save(nuevaIps);
                    });

            // Guardar ubicación
            UbicacionEntity ubicacion = ubicacionRepository.findByDane(obtenerValorOAsignarDefault(registro.get("DANE")))
                    .orElseGet(() -> {
                        UbicacionEntity nuevaUbicacion = new UbicacionEntity();
                        nuevaUbicacion.setDane(obtenerValorOAsignarDefault(registro.get("DANE")));
                        nuevaUbicacion.setDepartamento(obtenerValorOAsignarDefault(registro.get("DEPARTAMENTO")));
                        nuevaUbicacion.setMunicipio(obtenerValorOAsignarDefault(registro.get("MUNICIPIO")));
                        return ubicacionRepository.save(nuevaUbicacion);
                    });

            // Guardar giro
            GiroEntity giro = new GiroEntity();
            giro.setEpsEntity(eps);
            giro.setIpsEntity(ips);
            giro.setUbicacionEntity(ubicacion);
            giro.setFormaContratacion(obtenerValorOAsignarDefault(registro.get("FORMA_CONTRATACION")));
            giro.setValorOrdenado(parseDouble(registro.get("VALOR_ORDENADO")));
            giro.setTotalGiro(parseDouble(registro.get("TOTAL_GIRO")));
            giro.setObservaciones(obtenerValorOAsignarDefault(registro.get("OBSERVACIONES")));
            giro.setFecha(fecha);

            giroRepository.save(giro);
        }
    }

    private List<Map<String, String>> procesarArchivo(String rutaArchivo, String fecha) throws IOException {
        Workbook workbook = WorkbookFactory.create(new File(rutaArchivo));
        List<Map<String, String>> registros = new ArrayList<>();

        // Procesar hojas y agregar registros
        registros.addAll(procesarHoja(workbook.getSheetAt(0), fecha, true));
        registros.addAll(procesarHoja(workbook.getSheetAt(1), fecha, false));

        workbook.close();
        return registros;
    }

    private List<Map<String, String>> procesarHoja(Sheet hoja, String fecha, boolean esHoja1) {
        List<Map<String, String>> registros = new ArrayList<>();

        for (Row fila : hoja) {
            if (fila.getRowNum() < 8) continue; // Saltar encabezados
            if (esFilaTotalONota(fila)) break; // Detener al encontrar TOTAL o NOTA

            Map<String, String> registro = new HashMap<>();
            if (esHoja1) {
                registro.put("DANE", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(0))));
                registro.put("DEPARTAMENTO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(1))));
                registro.put("MUNICIPIO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(2))));
                registro.put("COD_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(3))));
                registro.put("NOMBRE_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(4))));
                registro.put("NIT_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(5))));
                registro.put("NOMBRE_IPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(6))));
                registro.put("FORMA_CONTRATACION", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(7))));
                registro.put("VALOR_ORDENADO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(8))));
                registro.put("TOTAL_GIRO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(9))));
                registro.put("OBSERVACIONES", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(10))));
            } else {
                registro.put("COD_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(0))));
                registro.put("NOMBRE_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(1))));
                registro.put("NIT_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(2))));
                registro.put("NOMBRE_IPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(3))));
                registro.put("FORMA_CONTRATACION", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(4))));
                registro.put("VALOR_ORDENADO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(5))));
                registro.put("TOTAL_GIRO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(6))));
                registro.put("OBSERVACIONES", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(7))));
            }
            registro.put("FECHA", fecha);
            registros.add(registro);
        }

        return registros;
    }

    private boolean esFilaTotalONota(Row fila) {
        for (Cell celda : fila) {
            String valor = obtenerValorCelda(celda);
            if (valor != null && (valor.equalsIgnoreCase("TOTAL") || valor.equalsIgnoreCase("NOTA"))) {
                return true;
            }
        }
        return false;
    }

    private String obtenerValorCelda(Cell celda) {
        if (celda == null) return null;
        return switch (celda.getCellType()) {
            case STRING -> celda.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) celda.getNumericCellValue());
            case BOOLEAN -> String.valueOf(celda.getBooleanCellValue());
            default -> null;
        };
    }

    private Double parseDouble(String valor) {
        if (valor == null || valor.isBlank()) return 0.0;
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String obtenerValorOAsignarDefault(String valor) {
        return (valor == null || valor.isBlank()) ? "N/A" : valor;
    }
}
