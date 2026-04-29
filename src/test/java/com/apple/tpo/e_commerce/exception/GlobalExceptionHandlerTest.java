package com.apple.tpo.e_commerce.exception;

import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.dto.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void handleResourceNotFound_returnsNotFoundResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response = handler.handleResourceNotFound(new ResourceNotFoundException("No existe"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals("No existe", response.getBody().getMessage());
    }

    @Test
    void handleRuntimeException_returnsBadRequestResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response = handler.handleRuntimeException(new RuntimeException("Stock insuficiente"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals("Stock insuficiente", response.getBody().getMessage());
    }

    @Test
    void handleValidation_returnsBadRequestWithFieldMessages() throws Exception {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RegisterRequest request = new RegisterRequest();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        bindingResult.addError(new FieldError("request", "email", "debe ser un email valido"));
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("validar", RegisterRequest.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);

        ResponseEntity<ApiResponse<List<String>>> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals("Validacion fallida", response.getBody().getMessage());
        assertEquals(List.of("email: debe ser un email valido"), response.getBody().getData());
    }

    @Test
    void handleException_returnsInternalServerErrorResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response = handler.handleException(new Exception("Error inesperado"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals("Error inesperado", response.getBody().getMessage());
    }

    private void validar(RegisterRequest request) {
    }
}
