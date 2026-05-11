package com.homerentsolution.msprecios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrecioResponseDTO {

    private Long idPrecios;
    private String temporada;
    private Double multiplicador;
    private Long idPropiedad;
}
