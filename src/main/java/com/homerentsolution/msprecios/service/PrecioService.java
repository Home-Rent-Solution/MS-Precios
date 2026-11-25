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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrecioService {

    private static final Logger log =
            LoggerFactory.getLogger(PrecioService.class);
    private static final BigDecimal RECARGO_TEMPORADA_ALTA = new BigDecimal("0.20");
    private static final BigDecimal DESCUENTO_LARGA_ESTADIA = new BigDecimal("0.10");
    private static final BigDecimal IMPUESTO_IVA = new BigDecimal("0.19");
    private static final int NOCHES_PARA_DESCUENTO = 7;

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

    public PrecioResponseDTO calcular(PrecioRequestDTO request) {
        // 1. Validar fechas y calcular noches
        long noches = ChronoUnit.DAYS.between(request.getFechaEntrada(), request.getFechaSalida());
        if (noches <= 0) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        // 2. Precio base (precio por noche * noches)
        BigDecimal subtotalBase = request.getPrecioBasePorNoche()
                .multiply(BigDecimal.valueOf(noches));

        // 3. Recargo por temporada alta (evaluando noche por noche)
        BigDecimal recargoTemporada = calcularRecargoTemporada(
                request.getFechaEntrada(),
                request.getFechaSalida(),
                request.getPrecioBasePorNoche()
        );

        BigDecimal subtotalConTemporada = subtotalBase.add(recargoTemporada);

        // 4. Descuentos
        BigDecimal descuento = calcularDescuento(subtotalConTemporada, noches, request.getCodigoDescuento());
        BigDecimal subtotalNeto = subtotalConTemporada.subtract(descuento);

        // 5. Impuestos
        BigDecimal impuestos = subtotalNeto.multiply(IMPUESTO_IVA);

        // 6. Total final
        BigDecimal total = subtotalNeto.add(impuestos);

        // Construir respuesta
        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setCantidadNoches(2);
        response.setSubtotalBase((subtotalBase));
        response.setRecargoTemporadaAlta((recargoTemporada));
        response.setDescuentosAplicados((descuento));
        response.setImpuestos((impuestos));
        response.setTotalFinal((total));

        return response;
    }

    private BigDecimal calcularRecargoTemporada(LocalDate entrada, LocalDate salida, BigDecimal precioNoche) {
        BigDecimal recargoTotal = BigDecimal.ZERO;
        for (LocalDate fecha = entrada; fecha.isBefore(salida); fecha = fecha.plusDays(1)) {
            if (esTemporadaAlta(fecha)) {
                recargoTotal = recargoTotal.add(precioNoche.multiply(RECARGO_TEMPORADA_ALTA));
            }
        }
        return recargoTotal;
    }

    private boolean esTemporadaAlta(LocalDate fecha) {
        Month mes = fecha.getMonth();
        return mes == Month.JANUARY || mes == Month.FEBRUARY || mes == Month.JULY;
    }

    private BigDecimal calcularDescuento(BigDecimal subtotal, long noches, String codigoDescuento) {
        BigDecimal porcentajeDescuento = BigDecimal.ZERO;

        if (noches >= NOCHES_PARA_DESCUENTO) {
            porcentajeDescuento = porcentajeDescuento.add(DESCUENTO_LARGA_ESTADIA);
        }

        if ("VERANO2026".equalsIgnoreCase(codigoDescuento)) {
            porcentajeDescuento = porcentajeDescuento.add(new BigDecimal("0.05"));
        }

        return subtotal.multiply(porcentajeDescuento);
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }


}