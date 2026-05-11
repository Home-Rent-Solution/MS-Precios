package com.homerentsolution.msprecios.repository;

import com.homerentsolution.msprecios.model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrecioRepository extends JpaRepository<Precio, Long> {
    //buscar por temporada alta- media- baja
    List<Precio> findByTemporada(String temporada);

    //obtener precio por propiedad
    List<Precio> findByIdPropiedad(Long idPropiedad);

    //obtener precio por temporada ordenado por el multiplicador mas alto al mas bajo
    List<Precio> findByTemporadaOrderByMultiplicadorDesc(
            String temporada);

}
