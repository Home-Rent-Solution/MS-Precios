package com.homerentsolution.msprecios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
// Define la documentación del DTO en Swagger
@Schema(
        name = "PrecioRequestDTO",
        description = "Datos necesarios para registrar un precio"
)
public class PrecioRequestDTO {

    @Schema(
            description = "Temporada aplicada al precio",
            example = "Alta"
    )
    @NotBlank(message = "La temporada es obligatoria")
    private String temporada;

    @Schema(
            description = "Multiplicador aplicado al precio",
            example = "1.5"
    )
    @NotNull(message = "El multiplicador es obligatorio")
    @Positive(message = "El multiplicador debe ser positivo")
    private Double multiplicador;


    @Schema(
            description = "ID de la propiedad asociada",
            example = "1"
    )
    @NotNull(message = "El id de propiedad es obligatorio")
    @Positive(message = "El id de propiedad debe ser positivo")
    private Long idPropiedad;

}
