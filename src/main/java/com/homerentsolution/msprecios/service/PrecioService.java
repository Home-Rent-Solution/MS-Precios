package com.homerentsolution.msprecios.service;

import com.homerentsolution.msprecios.client.PagoClient;
import com.homerentsolution.msprecios.client.PropiedadClient;
import com.homerentsolution.msprecios.client.ReservaClient;
import com.homerentsolution.msprecios.dto.PropiedadDTO;
import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;

import com.homerentsolution.msprecios.model.Precio;

import com.homerentsolution.msprecios.repository.PrecioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrecioService {

    private static final Logger log =
            LoggerFactory.getLogger(PrecioService.class);

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

        log.info("Listando todos los precios");

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR POR ID
    public PrecioResponseDTO buscarPorId(Long id) {

        log.info("Buscando precio con ID {}", id);

        Precio precio = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Precio con ID {} no encontrado",
                            id
                    );

                    return new RuntimeException(
                            "Precio no encontrado"
                    );
                });

        return convertirDTO(precio);
    }

    // GUARDAR
    public PrecioResponseDTO guardar(
            PrecioRequestDTO dto) {

        log.info(
                "Creando precio para propiedad {} en temporada {}",
                dto.getIdPropiedad(),
                dto.getTemporada()
        );

        // validar propiedad
        try {

            propiedadesClient.buscarPorId(
                    dto.getIdPropiedad()
            );

        } catch (Exception e) {

            log.error(
                    "La propiedad {} no existe",
                    dto.getIdPropiedad()
            );

            throw new RuntimeException(
                    "La propiedad no existe"
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
        precio.setFechaEntrada(dto.getFechaEntrada());
        precio.setFechaSalida(dto.getFechaSalida());

        // guardar después de validar todo
        Precio guardado =
                repository.save(precio);

        log.info(
                "Precio guardado correctamente con ID {}",
                guardado.getIdPrecios()
        );

        return convertirDTO(guardado);
    }

    // ACTUALIZAR
    public PrecioResponseDTO actualizar(
            Long id,
            PrecioRequestDTO dto) {

        log.info(
                "Actualizando precio con ID {}",
                id
        );

        Precio precio = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Precio con ID {} no encontrado",
                            id
                    );

                    return new RuntimeException(
                            "Precio no encontrado"
                    );
                });

        precio.setTemporada(
                dto.getTemporada()
        );

        precio.setMultiplicador(
                dto.getMultiplicador()
        );

        precio.setIdPropiedad(
                dto.getIdPropiedad()
        );
        precio.setFechaEntrada(dto.getFechaEntrada());
        precio.setFechaSalida(dto.getFechaSalida());

        Precio actualizado =
                repository.save(precio);

        log.info(
                "Precio {} actualizado correctamente",
                id
        );

        return convertirDTO(actualizado);
    }

    // ELIMINAR
    public void eliminar(Long id) {

        log.warn(
                "Eliminando precio con ID {}",
                id
        );

        Precio precio = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Precio con ID {} no encontrado",
                            id
                    );

                    return new RuntimeException(
                            "Precio no encontrado"
                    );
                });

        repository.delete(precio);

        log.info(
                "Precio {} eliminado correctamente",
                id
        );
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
    public List<PrecioResponseDTO> buscarPorTemporada(
            String temporada) {

        log.info(
                "Buscando precios para temporada {}",
                temporada
        );

        List<Precio> precios =
                repository.findByTemporada(
                        temporada
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta temporada"
            );
        }

        return precios.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    // Buscar precios por propiedad
    public List<PrecioResponseDTO> buscarPorPropiedad(
            Long idPropiedad) {

        log.info(
                "Buscando precios para propiedad {}",
                idPropiedad
        );

        List<Precio> precios =
                repository.findByIdPropiedad(
                        idPropiedad
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta propiedad"
            );
        }

        return precios.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    // Buscar precios ordenados
    public List<PrecioResponseDTO> buscarPorTemporadaOrdenado(
            String temporada) {

        log.info(
                "Buscando precios ordenados para temporada {}",
                temporada
        );

        List<Precio> precios =
                repository.findByTemporadaOrderByMultiplicadorDesc(
                        temporada
                );

        if (precios.isEmpty()) {

            throw new RuntimeException(
                    "No existen precios para esta temporada"
            );
        }

        return precios.stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    // Calcula el total con la misma regla que usa MS-Reservas al cobrar:
    // precio por noche x multiplicador de la temporada x noches
    public PrecioResponseDTO calcular(PrecioRequestDTO request) {
        if (request.getIdPropiedad() == null
                || request.getFechaEntrada() == null || request.getFechaSalida() == null) {
            throw new IllegalArgumentException(
                    "Para calcular se necesitan idPropiedad, fechaEntrada y fechaSalida.");
        }
        long noches = ChronoUnit.DAYS.between(request.getFechaEntrada(), request.getFechaSalida());
        if (noches <= 0) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        // igual que MS-Reservas: la regla de la temporada de llegada;
        // si la propiedad no tiene esa temporada, se usa su primera regla
        List<Precio> reglas = repository.findByIdPropiedad(request.getIdPropiedad());
        if (reglas.isEmpty()) {
            throw new RuntimeException("No existen precios para esta propiedad");
        }
        String temporada = temporadaSegunMes(request.getFechaEntrada());
        Precio regla = reglas.stream()
                .filter(p -> temporada.equalsIgnoreCase(p.getTemporada()))
                .findFirst()
                .orElse(reglas.get(0));

        // el precio por noche se toma de MS-Propiedades, igual que hace MS-Reservas
        PropiedadDTO propiedad;
        try {
            propiedad = propiedadesClient.obtenerPropiedad(request.getIdPropiedad());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener la propiedad desde MS-Propiedades");
        }

        BigDecimal subtotalBase = propiedad.getPrecio()
                .multiply(BigDecimal.valueOf(noches));
        BigDecimal total = subtotalBase
                .multiply(BigDecimal.valueOf(regla.getMultiplicador()));

        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPropiedad(request.getIdPropiedad());
        response.setTemporada(regla.getTemporada());
        response.setMultiplicador(regla.getMultiplicador());
        response.setCantidadNoches((int) noches);
        response.setSubtotalBase(subtotalBase);
        response.setTotalFinal(total);
        return response;
    }

    // alta: diciembre a febrero; baja: junio a agosto; media: el resto (igual que MS-Reservas)
    private String temporadaSegunMes(LocalDate fecha) {
        int mes = fecha.getMonthValue();
        if (mes == 12 || mes == 1 || mes == 2) {
            return "Alta";
        }
        if (mes >= 6 && mes <= 8) {
            return "Baja";
        }
        return "Media";
    }
}