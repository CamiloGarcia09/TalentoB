package com.empresa.giros.procesamientodatosgiros.excel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public final class ExcelProcessor {

    public List<Map<String, String>> procesarArchivo(final String rutaArchivo, final String fecha) throws IOException {
        Workbook workbook = WorkbookFactory.create(new File(rutaArchivo));
        List<Map<String, String>> registros = new ArrayList<>();

        registros.addAll(procesarHoja(workbook.getSheetAt(0), fecha, true));
        registros.addAll(procesarHoja(workbook.getSheetAt(1), fecha, false));

        workbook.close();
        return registros;
    }

    private List<Map<String, String>> procesarHoja(final Sheet hoja, final String fecha, final boolean esHoja1) {
        List<Map<String, String>> registros = new ArrayList<>();

        for (Row fila : hoja) {
            if (fila.getRowNum() < 8) continue;
            if (esFilaTotalONota(fila)) break;

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
                registro.put("TOTAL_GIRO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(8))));
                registro.put("OBSERVACIONES", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(9))));
            } else {
                registro.put("COD_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(0))));
                registro.put("NOMBRE_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(1))));
                registro.put("NIT_EPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(2))));
                registro.put("NOMBRE_IPS", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(3))));
                registro.put("FORMA_CONTRATACION", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(4))));
                registro.put("TOTAL_GIRO", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(5))));
                registro.put("OBSERVACIONES", obtenerValorOAsignarDefault(obtenerValorCelda(fila.getCell(6))));
            }
            registro.put("FECHA", fecha);
            registros.add(registro);
        }

        return registros;
    }


    private boolean esFilaTotalONota(final Row fila) {
        for (Cell celda : fila) {
            String valor = obtenerValorCelda(celda);
            if (valor != null && (valor.equalsIgnoreCase("TOTAL") || valor.equalsIgnoreCase("NOTA"))) {
                return true;
            }
        }
        return false;
    }

    private String obtenerValorCelda(final Cell celda) {
        if (celda == null) return null;

        FormulaEvaluator evaluator = celda.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();

        return switch (celda.getCellType()) {
            case STRING -> celda.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(celda.getNumericCellValue());
            case BOOLEAN -> String.valueOf(celda.getBooleanCellValue());
            case FORMULA -> {
                CellValue evaluado = evaluator.evaluate(celda);
                yield switch (evaluado.getCellType()) {
                    case STRING -> evaluado.getStringValue().trim();
                    case NUMERIC -> String.valueOf(evaluado.getNumberValue());
                    case BOOLEAN -> String.valueOf(evaluado.getBooleanValue());
                    default -> null;
                };
            }
            default -> null;
        };
    }

    private String obtenerValorOAsignarDefault(final String valor) {
        return (valor == null || valor.isBlank()) ? "N/A" : valor;
    }
}


