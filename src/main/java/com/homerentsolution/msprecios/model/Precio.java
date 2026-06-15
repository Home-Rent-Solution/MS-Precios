package com.homerentsolution.msprecios.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrecios;

    //temporada no puede quedar vacia
    @Column(nullable = false)
    @NotBlank(message = "La temporada es obligatoria")
    private String temporada;


    //multiplicador es obligatorio y debe ser un número positivo
    @Column(nullable = false)
    @NotNull(message = "El multiplicador es obligatorio")
    @Positive(message = "El multiplicador debe ser positivo")
    private Double multiplicador;

    //id de propiedad es obligatorio y debe ser positivo
    @Column(nullable = false)
    @NotNull(message = "El id de propiedad es obligatorio")
    @Positive(message = "El id de propiedad debe ser positivo")
    private Long idPropiedad;
}
