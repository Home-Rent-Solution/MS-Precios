package com.homerentsolution.msprecios.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// solo los datos de MS-Propiedades que usa MS-Precios
@Getter
@Setter
public class PropiedadDTO {

    private Long id;

    private BigDecimal precio;
}