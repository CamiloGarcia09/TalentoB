package com.empresa.giros.procesamientodatosgiros.initializer;

import com.empresa.giros.procesamientodatosgiros.excel.ExcelExporter;
import com.empresa.giros.procesamientodatosgiros.excel.ExcelProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.empresa.giros.procesamientodatosgiros"})
//@EnableJpaRepositories(basePackages = "com.empresa.giros.procesamientodatosgiros.repository")
//@EntityScan(basePackages = "com.empresa.giros.procesamientodatosgiros.entity")
public class ProcesamientoDatosGirosApplication {

    private final ExcelProcessor processor;

    public ProcesamientoDatosGirosApplication(ExcelProcessor processor) {
        this.processor = processor;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcesamientoDatosGirosApplication.class, args).getBean(ProcesamientoDatosGirosApplication.class).iniciar();
    }

    public void iniciar() {
        try {
            String rutaArchivo = "C:\\Bloques de Construccion\\giro-directo-discriminado-capita-y-evento-noviembre-2024.xlsx";
            String fecha = "2024-11-01";
            String rutaArchivoSalida = "C:/Bloques de Construccion/archivo_procesado69.xlsx";

            ProcesamientoDatosGirosApplication app = new ProcesamientoDatosGirosApplication(new ExcelProcessor());
            List<Map<String, String>> datosProcesados = app.processor.procesarArchivo(rutaArchivo, fecha);

            // Exportar a Excel
            ExcelExporter excel = new ExcelExporter();
            excel.exportarAExcel(datosProcesados, rutaArchivoSalida);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
