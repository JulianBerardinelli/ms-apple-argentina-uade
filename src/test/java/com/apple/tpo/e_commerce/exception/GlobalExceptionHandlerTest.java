package com.apple.tpo.e_commerce.exception;

import com.apple.tpo.e_commerce.dto.auth.RegisterRequest;
import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleResourceNotFound(new ResourceNotFoundException("No existe"));

        assertError(response, HttpStatus.NOT_FOUND, "No existe");
    }

    @Test
    void handleBusinessException_returnsBadRequestResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleBusinessException(new BusinessException("Stock insuficiente"));

        assertError(response, HttpStatus.BAD_REQUEST, "Stock insuficiente");
    }

    @Test
    void handleConflict_returnsConflictResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleConflict(new ConflictException("El email ya esta registrado"));

        assertError(response, HttpStatus.CONFLICT, "El email ya esta registrado");
    }

    @Test
    void handleInvalidCredentials_returnsUnauthorizedResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleInvalidCredentials(new InvalidCredentialsException("Credenciales invalidas"));

        assertError(response, HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
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

        assertError(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado");
    }

    private void assertError(ResponseEntity<ApiResponse<Void>> response, HttpStatus status, String message) {
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals(message, response.getBody().getMessage());
    }

    private void validar(RegisterRequest request) {
    }
}
