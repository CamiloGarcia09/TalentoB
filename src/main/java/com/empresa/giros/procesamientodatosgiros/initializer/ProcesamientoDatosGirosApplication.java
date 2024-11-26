package com.empresa.giros.procesamientodatosgiros.initializer;

import com.empresa.giros.procesamientodatosgiros.excel.ExcelProcessor;
import com.empresa.giros.procesamientodatosgiros.exception.ArchivoProcesamientoException;
import com.empresa.giros.procesamientodatosgiros.service.ProcesamientoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.empresa.giros.procesamientodatosgiros"})
@EnableJpaRepositories(basePackages = "com.empresa.giros.procesamientodatosgiros.repository")
@EntityScan(basePackages = "com.empresa.giros.procesamientodatosgiros.entity")
public class ProcesamientoDatosGirosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcesamientoDatosGirosApplication.class, args);
    }
    @Component
    static
    class DataProcessorRunner implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(DataProcessorRunner.class);

        private final ExcelProcessor excelProcessor;
        private final ProcesamientoService procesamientoService;

        @Value("${excel.file.path}")
        private String rutaArchivo;

        @Value("${excel.file.date}")
        private String fecha;

        public DataProcessorRunner(final ExcelProcessor excelProcessor, final ProcesamientoService procesamientoService) {
            this.excelProcessor = excelProcessor;
            this.procesamientoService = procesamientoService;
        }

        @Override
        public void run(String... args) {
            try {
                logger.info("Iniciando el procesamiento del archivo Excel en la ruta: {}", rutaArchivo);
                List<Map<String, String>> registros = excelProcessor.procesarArchivo(rutaArchivo, fecha);
                procesamientoService.procesarRegistros(registros, fecha);
                logger.info("El archivo fue procesado y exportado correctamente.");
            } catch (ArchivoProcesamientoException exception) {
                logger.error("Error específico en el procesamiento del archivo: {}", exception.getMessage(), exception);
            } catch (Exception exception) {
                logger.error("Error general al procesar el archivo: {}", exception.getMessage(), exception);
            }
        }

    }

}
