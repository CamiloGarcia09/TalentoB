package com.empresa.giros.procesamientodatosgiros.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelExporter {

    public void exportarAExcel(List<Map<String, String>> datos, String rutaArchivo) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet("Datos Procesados");

        // Crear encabezados
        String[] encabezados = {"DANE", "DEPARTAMENTO", "MUNICIPIO", "NIT_EPS", "NOMBRE_EPS", "FORMA_CONTRATACION", "VALOR_ORDENADO", "TOTAL_GIRO", "OBSERVACIONES", "FECHA"};
        Row filaEncabezado = hoja.createRow(0);
        for (int i = 0; i < encabezados.length; i++) {
            Cell celda = filaEncabezado.createCell(i);
            celda.setCellValue(encabezados[i]);
        }

        // Agregar datos
        int filaActual = 1;
        for (Map<String, String> registro : datos) {
            Row fila = hoja.createRow(filaActual++);
            for (int i = 0; i < encabezados.length; i++) {
                Cell celda = fila.createCell(i);
                celda.setCellValue(registro.getOrDefault(encabezados[i], "N/A"));
            }
        }

        // Guardar archivo
        FileOutputStream fileOut = new FileOutputStream(rutaArchivo);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
