package com.homerentsolution.msprecios.config;

import com.homerentsolution.msprecios.model.Precio;
import com.homerentsolution.msprecios.repository.PrecioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    private static final Logger log =
            LoggerFactory.getLogger(DataInitializer.class);

    // cantidad de propiedades que carga el DataInitializer de MS-Propiedades
    private static final long TOTAL_PROPIEDADES = 20;

    @Bean
    CommandLineRunner initDatabase(
            PrecioRepository repository) {

        return args -> {

            if (repository.count() == 0) {

                // cada propiedad recibe una regla por temporada.
                // MS-Reservas y /calcular eligen la regla segun el mes de llegada
                // (alta: dic-feb, baja: jun-ago, media: el resto); las fechas son referenciales
                for (long idPropiedad = 1; idPropiedad <= TOTAL_PROPIEDADES; idPropiedad++) {
                    repository.save(regla("Alta", 1.5, idPropiedad,
                            LocalDate.of(2026, 12, 1), LocalDate.of(2027, 2, 28)));
                    repository.save(regla("Media", 1.0, idPropiedad,
                            LocalDate.of(2027, 3, 1), LocalDate.of(2027, 5, 31)));
                    repository.save(regla("Baja", 0.8, idPropiedad,
                            LocalDate.of(2027, 6, 1), LocalDate.of(2027, 8, 31)));
                }

                log.info("Datos iniciales de precios cargados: {} reglas", repository.count());
            }
        };
    }

    private Precio regla(String temporada, Double multiplicador, Long idPropiedad,
                         LocalDate fechaEntrada, LocalDate fechaSalida) {
        Precio precio = new Precio();
        precio.setTemporada(temporada);
        precio.setMultiplicador(multiplicador);
        precio.setIdPropiedad(idPropiedad);
        precio.setFechaEntrada(fechaEntrada);
        precio.setFechaSalida(fechaSalida);
        return precio;
    }
}