package com.empresa.giros.procesamientodatosgiros.initializer;

import com.empresa.giros.procesamientodatosgiros.excel.ExcelProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.empresa.giros.procesamientodatosgiros"})
@EnableJpaRepositories(basePackages = "com.empresa.giros.procesamientodatosgiros.repository")
@EntityScan(basePackages = "com.empresa.giros.procesamientodatosgiros.entity")
public class ProcesamientoDatosGirosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcesamientoDatosGirosApplication.class, args);
    }

    @Component
    class DataProcessorRunner implements CommandLineRunner {

        private final ExcelProcessor processor;

        public DataProcessorRunner(ExcelProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void run(String... args) {
            try {
                // Ruta al archivo Excel de entrada y salida
                String rutaArchivo = "C:/Bloques de Construccion/giro-directo-discriminado-capita-y-evento-noviembre-2023.xlsx";
                String fecha = "2023-11-30";

                // Procesar el archivo Excel
                processor.procesarArchivoYGuardar(rutaArchivo, fecha);

                // Exportar los datos procesados a un nuevo archivo Excel

                System.out.println("El archivo fue procesado y exportado correctamente.");

            } catch (IOException e) {
                System.err.println("Error al procesar el archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
