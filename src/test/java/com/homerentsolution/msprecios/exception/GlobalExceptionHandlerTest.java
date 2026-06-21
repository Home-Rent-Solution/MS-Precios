package com.homerentsolution.msprecios.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void errorResponseDebeGuardarYRetornarDatos() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 21, 15, 30);

        ErrorResponse error = new ErrorResponse("Error de prueba", 400, fecha);

        assertEquals("Error de prueba", error.getMensaje());
        assertEquals(400, error.getStatus());
        assertEquals(fecha, error.getFecha());

        ErrorResponse errorVacio = new ErrorResponse();
        errorVacio.setMensaje("Nuevo mensaje");
        errorVacio.setStatus(500);
        errorVacio.setFecha(fecha);

        assertEquals("Nuevo mensaje", errorVacio.getMensaje());
        assertEquals(500, errorVacio.getStatus());
        assertEquals(fecha, errorVacio.getFecha());
    }

    @Test
    void manejarRuntimeExceptionDebeRetornarBadRequest() {
        ResponseEntity<ErrorResponse> response =
                handler.manejarRuntimeException(new RuntimeException("Error de negocio"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de negocio", response.getBody().getMensaje());
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getFecha());
    }

    @Test
    void manejarGeneralDebeRetornarInternalServerError() {
        ResponseEntity<ErrorResponse> response =
                handler.manejarGeneral(new Exception("Error interno"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMensaje());
        assertEquals(500, response.getBody().getStatus());
        assertNotNull(response.getBody().getFecha());
    }

    @Test
    void manejarValidacionesDebeRetornarBadRequestConMensajeDeCampo() throws Exception {
        Method method = DummyController.class.getDeclaredMethod("crear", Object.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "precioRequest");

        bindingResult.addError(
                new FieldError("precioRequest", "temporada", "no debe estar vacío")
        );

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponse> response =
                handler.manejarValidaciones(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("temporada: no debe estar vacío", response.getBody().getMensaje());
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getFecha());
    }

    static class DummyController {
        void crear(Object request) {
        }
    }
}