package com.homerentsolution.msprecios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homerentsolution.msprecios.dto.PrecioRequestDTO;
import com.homerentsolution.msprecios.dto.PrecioResponseDTO;
import com.homerentsolution.msprecios.service.PrecioService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrecioController.class)
class PrecioControllerTest {

    // Permite ejecutar peticiones HTTP simuladas sin levantar todo el servidor
    @Autowired
    private MockMvc mockMvc;

    // Convierte objetos Java a JSON para probar POST y PUT
    @Autowired
    private ObjectMapper objectMapper;

    // Simula el service para no depender de base de datos ni Feign
    @MockitoBean
    private PrecioService service;

    @Test
    void listar_debeRetornarStatus200() throws Exception {

        // GIVEN
        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPrecios(1L);
        response.setTemporada("Alta");
        response.setMultiplicador(1.5);
        response.setIdPropiedad(10L);

        when(service.listar()).thenReturn(List.of(response));

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/precios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPrecios").value(1))
                .andExpect(jsonPath("$[0].temporada").value("Alta"))
                .andExpect(jsonPath("$[0].multiplicador").value(1.5))
                .andExpect(jsonPath("$[0].idPropiedad").value(10));

        verify(service, times(1)).listar();
    }

    @Test
    void buscar_debeRetornarStatus200() throws Exception {

        // GIVEN
        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPrecios(1L);
        response.setTemporada("Media");
        response.setMultiplicador(1.2);
        response.setIdPropiedad(20L);

        when(service.buscarPorId(1L)).thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/precios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPrecios").value(1))
                .andExpect(jsonPath("$.temporada").value("Media"))
                .andExpect(jsonPath("$.multiplicador").value(1.2))
                .andExpect(jsonPath("$.idPropiedad").value(20));

        verify(service, times(1)).buscarPorId(1L);
    }

    @Test
    void guardar_conDatosValidos_debeRetornarStatus201() throws Exception {

        // GIVEN
        PrecioRequestDTO request = new PrecioRequestDTO();
        request.setTemporada("Alta");
        request.setMultiplicador(1.5);
        request.setIdPropiedad(10L);

        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPrecios(1L);
        response.setTemporada("Alta");
        response.setMultiplicador(1.5);
        response.setIdPropiedad(10L);

        when(service.guardar(any(PrecioRequestDTO.class))).thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/precios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPrecios").value(1))
                .andExpect(jsonPath("$.temporada").value("Alta"))
                .andExpect(jsonPath("$.multiplicador").value(1.5))
                .andExpect(jsonPath("$.idPropiedad").value(10));

        verify(service, times(1)).guardar(any(PrecioRequestDTO.class));
    }

    @Test
    void guardar_conDatosInvalidos_debeRetornarStatus400() throws Exception {

        // GIVEN: request inválido porque temporada viene vacía
        PrecioRequestDTO request = new PrecioRequestDTO();
        request.setTemporada("");
        request.setMultiplicador(1.5);
        request.setIdPropiedad(10L);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/precios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, never()).guardar(any(PrecioRequestDTO.class));
    }

    @Test
    void actualizar_conDatosValidos_debeRetornarStatus200() throws Exception {

        // GIVEN
        PrecioRequestDTO request = new PrecioRequestDTO();
        request.setTemporada("Baja");
        request.setMultiplicador(1.1);
        request.setIdPropiedad(10L);

        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPrecios(1L);
        response.setTemporada("Baja");
        response.setMultiplicador(1.1);
        response.setIdPropiedad(10L);

        when(service.actualizar(eq(1L), any(PrecioRequestDTO.class)))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(put("/api/v1/precios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPrecios").value(1))
                .andExpect(jsonPath("$.temporada").value("Baja"))
                .andExpect(jsonPath("$.multiplicador").value(1.1))
                .andExpect(jsonPath("$.idPropiedad").value(10));

        verify(service, times(1))
                .actualizar(eq(1L), any(PrecioRequestDTO.class));
    }

    @Test
    void eliminar_debeRetornarStatus204() throws Exception {

        // GIVEN
        doNothing().when(service).eliminar(1L);

        // WHEN + THEN
        mockMvc.perform(delete("/api/v1/precios/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).eliminar(1L);
    }

    @Test
    void buscarPorTemporada_debeRetornarStatus200() throws Exception {

        // GIVEN
        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPrecios(1L);
        response.setTemporada("Alta");
        response.setMultiplicador(1.5);
        response.setIdPropiedad(10L);

        when(service.buscarPorTemporada("Alta")).thenReturn(List.of(response));

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/precios/temporada/Alta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temporada").value("Alta"));

        verify(service, times(1)).buscarPorTemporada("Alta");
    }

    @Test
    void buscarPorPropiedad_debeRetornarStatus200() throws Exception {

        // GIVEN
        PrecioResponseDTO response = new PrecioResponseDTO();
        response.setIdPrecios(1L);
        response.setTemporada("Alta");
        response.setMultiplicador(1.5);
        response.setIdPropiedad(10L);

        when(service.buscarPorPropiedad(10L)).thenReturn(List.of(response));

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/precios/propiedad/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPropiedad").value(10));

        verify(service, times(1)).buscarPorPropiedad(10L);
    }
}