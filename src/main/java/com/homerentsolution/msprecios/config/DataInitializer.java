package com.homerentsolution.msprecios.config;

import com.homerentsolution.msprecios.model.Precio;

import com.homerentsolution.msprecios.repository.PrecioRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

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

                repository.save(precio2);

                System.out.println(
                        "Datos iniciales de precios cargados"
                );
            }
        };
    }
}