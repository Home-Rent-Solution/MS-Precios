package com.homerentsolution.msprecios.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrecios;

    //temporada no puede quedar vacia
    @Column(nullable = false)
    private String temporada;


    //multiplicador es obligatorio y debe ser un número positivo
    @Column(nullable = false)
    private Double multiplicador;

    //id de propiedad es obligatorio y debe ser positivo
    @Column(nullable = false)
    private Long idPropiedad;
    @Column(nullable = false)
    private LocalDate fechaEntrada;
    @Column(nullable = false)
    private LocalDate fechaSalida;
}
