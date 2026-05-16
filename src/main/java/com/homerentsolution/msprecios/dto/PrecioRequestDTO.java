package com.homerentsolution.msprecios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrecioRequestDTO {

    @NotBlank(message = "La temporada es obligatoria")
    private String temporada;

    @NotNull(message = "El multiplicador es obligatorio")
    @Positive(message = "El multiplicador debe ser positivo")
    private Double multiplicador;

    @NotNull(message = "El id de propiedad es obligatorio")
    @Positive(message = "El id de propiedad debe ser positivo")
    private Long idPropiedad;

}
