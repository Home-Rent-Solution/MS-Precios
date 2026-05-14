package com.homerentsolution.msprecios.service;

import com.homerentsolution.msprecios.client.PagoClient;
import com.homerentsolution.msprecios.client.PropiedadClient;
import com.homerentsolution.msprecios.client.ReservaClient;

import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;

import com.homerentsolution.msprecios.model.Precio;

import com.homerentsolution.msprecios.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository repository;

    // Feign propiedades
    @Autowired
    private PropiedadClient propiedadesClient;

    // Feign reservas
    @Autowired
    private ReservaClient reservaClient;

    // Feign pagos
    @Autowired
    private PagoClient pagoClient;

    // LISTAR
    public List<PrecioResponseDTO> listar() {

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR POR ID
    public PrecioResponseDTO buscarPorId(Long id) {

        Precio precio = repository.findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Precio no encontrado"
                        ));

        return convertirDTO(precio);
    }

    // GUARDAR
    public PrecioResponseDTO guardar(
            PrecioRequestDTO dto) {

        // validar propiedad
        try {

            propiedadesClient.buscarPorId(
                    dto.getIdPropiedad()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "La propiedad no existe"
            );
        }

        // validar reserva
        try {

            reservaClient.buscarReserva(1);

        } catch (Exception e) {

            throw new RuntimeException(
                    "No existe una reserva válida asociada"
            );
        }

        // convertir DTO → Entity
        Precio precio = new Precio();

        precio.setTemporada(
                dto.getTemporada()
        );

        precio.setMultiplicador(
                dto.getMultiplicador()
        );

        precio.setIdPropiedad(
                dto.getIdPropiedad()
        );

        // guardar
        Precio guardado =
                repository.save(precio);
        // validar pago antes de generar tarifas
        try {

            pagoClient.buscarPago(1L);

        } catch (Exception e) {

            throw new RuntimeException(
                    "No existe un pago válido asociado"
            );
        }

        // convertir Entity → DTO
        return convertirDTO(guardado);
    }

    // ACTUALIZAR
    public PrecioResponseDTO actualizar(
            Long id,
            PrecioRequestDTO dto) {

        Precio precio = repository.findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Precio no encontrado"
                        ));

        precio.setTemporada(
                dto.getTemporada()
        );

        precio.setMultiplicador(
                dto.getMultiplicador()
        );

        precio.setIdPropiedad(
                dto.getIdPropiedad()
        );

        Precio actualizado =
                repository.save(precio);

        return convertirDTO(actualizado);
    }

    // ELIMINAR
    public void eliminar(Long id) {

        Precio precio = repository.findById(id)
                .orElseThrow(() ->

                        new RuntimeException(
                                "Precio no encontrado"
                        ));

        repository.delete(precio);
    }

    // CONVERTIR ENTITY → DTO
    private PrecioResponseDTO convertirDTO(
            Precio precio) {

        PrecioResponseDTO dto =
                new PrecioResponseDTO();

        dto.setIdPrecios(
                precio.getIdPrecios()
        );

        dto.setTemporada(
                precio.getTemporada()
        );

        dto.setMultiplicador(
                precio.getMultiplicador()
        );

        dto.setIdPropiedad(
                precio.getIdPropiedad()
        );

        return dto;
    }

    // Buscar precios por temporada
    public List<Precio> buscarPorTemporada(
            String temporada) {

        List<Precio> precios =
                repository.findByTemporada(
                        temporada
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta temporada"
            );
        }

        return precios;
    }

    // Buscar precios por propiedad
    public List<Precio> buscarPorPropiedad(
            Long idPropiedad) {

        List<Precio> precios =
                repository.findByIdPropiedad(
                        idPropiedad
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta propiedad"
            );
        }

        return precios;
    }

    // Buscar precios ordenados
    public List<Precio> buscarPorTemporadaOrdenado(
            String temporada) {

        List<Precio> precios =
                repository
                        .findByTemporadaOrderByMultiplicadorDesc(
                                temporada
                        );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta temporada"
            );
        }

        return precios;
    }
}