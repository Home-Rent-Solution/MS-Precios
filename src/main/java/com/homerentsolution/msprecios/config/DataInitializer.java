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

    @Bean
    CommandLineRunner initDatabase(
            PrecioRepository repository) {

        return args -> {

            if (repository.count() == 0) {

                Precio precio1 =
                        new Precio();

                precio1.setTemporada(
                        "Alta"
                );

                precio1.setMultiplicador(
                        1.5
                );

                precio1.setIdPropiedad(
                        1L
                );

                precio1.setFechaEntrada(LocalDate.of(2026, 12, 15));
                precio1.setFechaSalida(LocalDate.of(2027, 2, 28));

                repository.save(precio1);

                Precio precio2 =
                        new Precio();

                precio2.setTemporada(
                        "Baja"
                );

                precio2.setMultiplicador(
                        0.8
                );

                precio2.setIdPropiedad(
                        2L
                );

                precio2.setFechaEntrada(LocalDate.of(2027, 3, 1));
                precio2.setFechaSalida(LocalDate.of(2027, 6, 30));

                repository.save(precio2);

                log.info("Datos iniciales de precios cargados");
            }
        };
    }
}
