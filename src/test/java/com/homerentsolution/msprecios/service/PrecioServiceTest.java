package com.homerentsolution.msprecios.service;

import com.homerentsolution.msprecios.client.PagoClient;
import com.homerentsolution.msprecios.client.PropiedadClient;
import com.homerentsolution.msprecios.client.ReservaClient;
import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import com.homerentsolution.msprecios.model.Precio;
import com.homerentsolution.msprecios.repository.PrecioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Indica que este test usará Mockito
@ExtendWith(MockitoExtension.class)
class PrecioServiceTest {

    // Simula el repositorio, no usa base de datos real
    @Mock
    private PrecioRepository repository;

    // Simula el cliente Feign de Propiedades
    @Mock
    private PropiedadClient propiedadesClient;

    // Simula el cliente Feign de Reservas
    @Mock
    private ReservaClient reservaClient;

    // Simula el cliente Feign de Pagos
    @Mock
    private PagoClient pagoClient;

    // Crea el service real, pero con las dependencias falsas de arriba
    @InjectMocks
    private PrecioService service;

    @Test
    void listar_debeRetornarListaDePrecios() {

        // GIVEN: datos preparados para la prueba
        Precio precio = new Precio();
        precio.setIdPrecios(1L);
        precio.setTemporada("Alta");
        precio.setMultiplicador(1.5);
        precio.setIdPropiedad(10L);

        when(repository.findAll()).thenReturn(List.of(precio));

        // WHEN: se ejecuta el método real del service
        List<PrecioResponseDTO> resultado = service.listar();

        // THEN: se verifica el resultado esperado
        assertEquals(1, resultado.size());
        assertEquals("Alta", resultado.get(0).getTemporada());
        assertEquals(1.5, resultado.get(0).getMultiplicador());
        assertEquals(10L, resultado.get(0).getIdPropiedad());

        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarPrecio() {

        // GIVEN
        Precio precio = new Precio();
        precio.setIdPrecios(1L);
        precio.setTemporada("Media");
        precio.setMultiplicador(1.2);
        precio.setIdPropiedad(20L);

        when(repository.findById(1L)).thenReturn(Optional.of(precio));

        // WHEN
        PrecioResponseDTO resultado = service.buscarPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPrecios());
        assertEquals("Media", resultado.getTemporada());
        assertEquals(1.2, resultado.getMultiplicador());
        assertEquals(20L, resultado.getIdPropiedad());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {

        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.buscarPorId(99L)
        );

        assertEquals("Precio no encontrado", exception.getMessage());

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void guardar_cuandoDatosSonValidos_debeGuardarPrecio() {

        // GIVEN
        PrecioRequestDTO dto = new PrecioRequestDTO();
        dto.setTemporada("Alta");
        dto.setMultiplicador(1.5);
        dto.setIdPropiedad(10L);

        // Simulamos que las validaciones Feign funcionan correctamente
        when(propiedadesClient.buscarPorId(10L)).thenReturn(new Object());
        when(reservaClient.buscarReserva(1)).thenReturn(new Object());
        when(pagoClient.buscarPago(1L)).thenReturn(new Object());

        Precio precioGuardado = new Precio();
        precioGuardado.setIdPrecios(1L);
        precioGuardado.setTemporada("Alta");
        precioGuardado.setMultiplicador(1.5);
        precioGuardado.setIdPropiedad(10L);

        when(repository.save(any(Precio.class))).thenReturn(precioGuardado);

        // WHEN
        PrecioResponseDTO resultado = service.guardar(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPrecios());
        assertEquals("Alta", resultado.getTemporada());
        assertEquals(1.5, resultado.getMultiplicador());
        assertEquals(10L, resultado.getIdPropiedad());

        verify(propiedadesClient, times(1)).buscarPorId(10L);
        verify(reservaClient, times(1)).buscarReserva(1);
        verify(pagoClient, times(1)).buscarPago(1L);
        verify(repository, times(1)).save(any(Precio.class));
    }

    private PrecioRequestDTO precioValido() {
        PrecioRequestDTO dto = new PrecioRequestDTO();
        dto.setTemporada("Alta");
        dto.setMultiplicador(1.5);
        dto.setIdPropiedad(10L);
        return dto;
    }

    @Test
    void guardar_cuandoPropiedadNoExiste_debeLanzarExcepcion() {
        PrecioRequestDTO dto = precioValido();
        when(propiedadesClient.buscarPorId(10L)).thenThrow(new RuntimeException("sin propiedad"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.guardar(dto));

        assertEquals("La propiedad no existe", exception.getMessage());
    }

    @Test
    void guardar_cuandoReservaNoExiste_debeLanzarExcepcion() {
        PrecioRequestDTO dto = precioValido();
        when(propiedadesClient.buscarPorId(10L)).thenReturn(new Object());
        when(reservaClient.buscarReserva(1)).thenThrow(new RuntimeException("sin reserva"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.guardar(dto));

        assertEquals("No existe una reserva válida asociada", exception.getMessage());
    }

    @Test
    void guardar_cuandoPagoNoExiste_debeLanzarExcepcion() {
        PrecioRequestDTO dto = precioValido();
        when(propiedadesClient.buscarPorId(10L)).thenReturn(new Object());
        when(reservaClient.buscarReserva(1)).thenReturn(new Object());
        when(pagoClient.buscarPago(1L)).thenThrow(new RuntimeException("sin pago"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.guardar(dto));

        assertEquals("No existe un pago válido asociado", exception.getMessage());
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarPrecio() {

        // GIVEN
        Precio precioExistente = new Precio();
        precioExistente.setIdPrecios(1L);
        precioExistente.setTemporada("Baja");
        precioExistente.setMultiplicador(1.0);
        precioExistente.setIdPropiedad(5L);

        PrecioRequestDTO dto = new PrecioRequestDTO();
        dto.setTemporada("Alta");
        dto.setMultiplicador(1.8);
        dto.setIdPropiedad(5L);

        Precio precioActualizado = new Precio();
        precioActualizado.setIdPrecios(1L);
        precioActualizado.setTemporada("Alta");
        precioActualizado.setMultiplicador(1.8);
        precioActualizado.setIdPropiedad(5L);

        when(repository.findById(1L)).thenReturn(Optional.of(precioExistente));
        when(repository.save(any(Precio.class))).thenReturn(precioActualizado);

        // WHEN
        PrecioResponseDTO resultado = service.actualizar(1L, dto);

        // THEN
        assertEquals(1L, resultado.getIdPrecios());
        assertEquals("Alta", resultado.getTemporada());
        assertEquals(1.8, resultado.getMultiplicador());
        assertEquals(5L, resultado.getIdPropiedad());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Precio.class));
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarPrecio() {

        // GIVEN
        Precio precio = new Precio();
        precio.setIdPrecios(1L);
        precio.setTemporada("Alta");
        precio.setMultiplicador(1.5);
        precio.setIdPropiedad(10L);

        when(repository.findById(1L)).thenReturn(Optional.of(precio));

        // WHEN
        service.eliminar(1L);

        // THEN
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(precio);
    }

    @Test
    void buscarPorTemporada_cuandoExiste_debeRetornarLista() {

        // GIVEN
        Precio precio = new Precio();
        precio.setIdPrecios(1L);
        precio.setTemporada("Alta");
        precio.setMultiplicador(1.5);
        precio.setIdPropiedad(10L);

        when(repository.findByTemporada("Alta")).thenReturn(List.of(precio));

        // WHEN
        List<PrecioResponseDTO> resultado =
                service.buscarPorTemporada("Alta");

        // THEN
        assertEquals(1, resultado.size());
        assertEquals("Alta", resultado.get(0).getTemporada());

        verify(repository, times(1)).findByTemporada("Alta");
    }

    @Test
    void buscarPorPropiedad_cuandoExiste_debeRetornarLista() {

        // GIVEN
        Precio precio = new Precio();
        precio.setIdPrecios(1L);
        precio.setTemporada("Alta");
        precio.setMultiplicador(1.5);
        precio.setIdPropiedad(10L);

        when(repository.findByIdPropiedad(10L)).thenReturn(List.of(precio));

        // WHEN
        List<PrecioResponseDTO> resultado =
                service.buscarPorPropiedad(10L);

        // THEN
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getIdPropiedad());

        verify(repository, times(1)).findByIdPropiedad(10L);
    }
}
