package com.empresa.giros.procesamientodatosgiros.excel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class ExcelProcessor {

    public List<Map<String, String>> procesarArchivo(String rutaArchivo, String fecha) throws IOException {
        Workbook workbook = WorkbookFactory.create(new File(rutaArchivo));
        List<Map<String, String>> registros = new ArrayList<>();

        // Leer la hoja 1 y mapear (NIT, COD_EPS) con datos adicionales
        Sheet hoja1 = workbook.getSheetAt(0);
        Map<String, Map<String, String>> mapaDatosHoja1 = new HashMap<>();
        for (Row fila : hoja1) {
            if (fila.getRowNum() < 8) continue; // Saltar encabezados
            if (esFilaTotalONota(fila)) continue; // Excluir TOTAL y NOTA

            String nit = obtenerValorCelda(fila.getCell(5)); // NIT
            String codEps = obtenerValorCelda(fila.getCell(3)); // COD_EPS

            // Validar y normalizar valores
            if (nit == null || nit.isBlank() || codEps == null || codEps.isBlank()) {
                System.out.println("Fila ignorada en hoja 1 por NIT o COD_EPS vacío. Fila: " + fila.getRowNum());
                continue;
            }

            nit = nit.trim();
            codEps = codEps.trim();
            String clave = nit + "-" + codEps; // Crear clave única para NIT y COD_EPS

            Map<String, String> datosFila = new HashMap<>();
            datosFila.put("DANE", obtenerValorCelda(fila.getCell(0)));
            datosFila.put("DEPARTAMENTO", obtenerValorCelda(fila.getCell(1)));
            datosFila.put("MUNICIPIO", obtenerValorCelda(fila.getCell(2)));
            mapaDatosHoja1.put(clave, datosFila);

            System.out.println("Clave creada en hoja 1: " + clave + " -> " + datosFila);
        }

        // Procesar hoja 1
        registros.addAll(procesarHoja(hoja1, fecha, mapaDatosHoja1, true));

        // Procesar hoja 2
        Sheet hoja2 = workbook.getSheetAt(1);
        registros.addAll(procesarHoja(hoja2, fecha, mapaDatosHoja1, false));

        workbook.close();
        return registros;
    }

    private List<Map<String, String>> procesarHoja(Sheet hoja, String fecha, Map<String, Map<String, String>> mapaHoja1, boolean esHoja1) {
        List<Map<String, String>> registros = new ArrayList<>();

        for (Row fila : hoja) {
            if (fila.getRowNum() < 8) continue; // Saltar encabezados
            if (esFilaTotalONota(fila)) break; // Detener al encontrar TOTAL o NOTA

            Map<String, String> registro = new HashMap<>();
            String nit, codEps, clave;

            if (esHoja1) {
                // Para la hoja 1, las claves se generan con las columnas 5 (NIT) y 3 (COD_EPS)
                nit = obtenerValorCelda(fila.getCell(5)); // NIT
                codEps = obtenerValorCelda(fila.getCell(3)); // COD_EPS
            } else {
                // Para la hoja 2, las claves se generan con las columnas 2 (NIT_IPS) y 0 (COD_EPS)
                nit = obtenerValorCelda(fila.getCell(2)); // NIT_IPS
                codEps = obtenerValorCelda(fila.getCell(0)); // COD_EPS
            }

            // Validar y normalizar valores
            if (nit != null) nit = nit.trim();
            if (codEps != null) codEps = codEps.trim();
            clave = (nit != null && codEps != null) ? nit + "-" + codEps : null;

            if (clave != null && !esHoja1 && mapaHoja1.containsKey(clave)) {
                // Si el NIT y el COD_EPS están en la hoja 1, rellenar con datos faltantes
                Map<String, String> datosHoja1 = mapaHoja1.get(clave);
                registro.put("DANE", datosHoja1.getOrDefault("DANE", "N/A"));
                registro.put("DEPARTAMENTO", datosHoja1.getOrDefault("DEPARTAMENTO", "N/A"));
                registro.put("MUNICIPIO", datosHoja1.getOrDefault("MUNICIPIO", "N/A"));

            } else if (!esHoja1) {
                // Si el NIT o el COD_EPS no están en la hoja 1, rellenar con N/A
                registro.put("DANE", "N/A");
                registro.put("DEPARTAMENTO", "N/A");
                registro.put("MUNICIPIO", "N/A");

            } else {
                // Si es hoja 1, rellenar directamente
                registro.put("DANE", obtenerValorCelda(fila.getCell(0)));
                registro.put("DEPARTAMENTO", obtenerValorCelda(fila.getCell(1)));
                registro.put("MUNICIPIO", obtenerValorCelda(fila.getCell(2)));
            }

            // Ajustar las columnas para hoja 2
            if (!esHoja1) {
                registro.put("COD_EPS", obtenerValorCelda(fila.getCell(0))); // COD EPS
                registro.put("NOMBRE_EPS", obtenerValorCelda(fila.getCell(1))); // NOMBRE EPS
                registro.put("NIT_EPS", obtenerValorCelda(fila.getCell(2))); // NIT IPS
                registro.put("NOMBRE_IPS", obtenerValorCelda(fila.getCell(3))); // NOMBRE IPS
                registro.put("FORMA_CONTRATACION", obtenerValorCelda(fila.getCell(4))); // FORMA CONTRATACIÓN
                registro.put("VALOR_ORDENADO", obtenerValorCelda(fila.getCell(5))); // VALOR ORDENADO
                registro.put("TOTAL_GIRO", obtenerValorCelda(fila.getCell(6))); // TOTAL GIRO
            } else {
                registro.put("COD_EPS", obtenerValorCelda(fila.getCell(3))); // COD EPS
                registro.put("NOMBRE_EPS", obtenerValorCelda(fila.getCell(4))); // NOMBRE EPS
                registro.put("NIT_EPS", nit != null ? nit : "N/A"); // NIT
                registro.put("NOMBRE_IPS", obtenerValorCelda(fila.getCell(6))); // NOMBRE IPS
                registro.put("FORMA_CONTRATACION", obtenerValorCelda(fila.getCell(7))); // FORMA CONTRATACIÓN
                registro.put("VALOR_ORDENADO", obtenerValorCelda(fila.getCell(8))); // VALOR ORDENADO
                registro.put("TOTAL_GIRO", obtenerValorCelda(fila.getCell(9))); // TOTAL GIRO
            }

            registro.put("OBSERVACIONES", obtenerValorCelda(fila.getCell(10)) != null ? obtenerValorCelda(fila.getCell(10)) : "N/A");
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
}
