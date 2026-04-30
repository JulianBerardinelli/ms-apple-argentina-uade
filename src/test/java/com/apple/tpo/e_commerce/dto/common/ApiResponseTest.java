package com.apple.tpo.e_commerce.dto.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void ok_buildsSuccessfulResponse() {
        ApiResponse<String> response = ApiResponse.ok(200, "OK", "data");

        assertNotNull(response.getTimestamp());
        assertEquals(200, response.getStatus());
        assertTrue(response.getSuccess());
        assertEquals("OK", response.getMessage());
        assertEquals("data", response.getData());
    }

    @Test
    void error_buildsErrorResponse() {
        ApiResponse<String> response = ApiResponse.error(400, "Error", "detalle");

        assertNotNull(response.getTimestamp());
        assertEquals(400, response.getStatus());
        assertFalse(response.getSuccess());
        assertEquals("Error", response.getMessage());
        assertEquals("detalle", response.getData());
    }
}
