package com.homerentsolution.msprecios.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrecioRequestDTO {

    private String temporada;
    private Double multiplicador;
    private Long idPropiedad;

}
