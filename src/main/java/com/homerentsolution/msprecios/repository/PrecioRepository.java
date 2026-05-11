package com.homerentsolution.msprecios.repository;

import com.homerentsolution.msprecios.model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecioRepository extends JpaRepository<Precio, Long> {
}
