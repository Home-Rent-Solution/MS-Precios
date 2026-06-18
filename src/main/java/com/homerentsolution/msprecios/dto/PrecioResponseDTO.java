package com.homerentsolution.msprecios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "PrecioResponseDTO",
        description = "Respuesta con información del precio"
)
public class PrecioResponseDTO {

    @Schema(
            description = "ID del precio",
            example = "1"
    )
    private Long idPrecios;

    @Schema(
            description = "Temporada aplicada al precio",
            example = "Alta"
    )
    private String temporada;

    @Schema(
            description = "Multiplicador aplicado al precio",
            example = "1.5"
    )
    private Double multiplicador;

    @Schema(
            description = "ID de la propiedad asociada",
            example = "1"
    )
    private Long idPropiedad;
}
