package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.respository.OrdenCompraRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrdenCompraServiceTest {

    @Test
    void getAllOrdenes_returnsDtoResult() {
        OrdenCompraRepository repository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = createService(repository);
        OrdenCompra orden = new OrdenCompra();
        orden.setEstado("COMPLETADA");

        Mockito.when(repository.findAll()).thenReturn(List.of(orden));

        List<OrdenCompraResponse> ordenes = service.getAllOrdenes();

        assertEquals(1, ordenes.size());
        assertEquals("COMPLETADA", ordenes.get(0).getEstado());
    }

    @Test
    void getOrdenById_whenOrdenExists_returnsDto() {
        OrdenCompraRepository repository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = createService(repository);
        OrdenCompra orden = new OrdenCompra();
        orden.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(orden));

        assertEquals(1L, service.getOrdenById(1L).getId());
    }

    @Test
    void getOrdenById_whenOrdenDoesNotExist_throwsResourceNotFoundException() {
        OrdenCompraRepository repository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = createService(repository);

        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getOrdenById(99L));
    }

    @Test
    void getOrdenesByUsuarioId_returnsDtoResult() {
        OrdenCompraRepository repository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = createService(repository);
        OrdenCompra orden = new OrdenCompra();

        Mockito.when(repository.findByUsuarioId(1L)).thenReturn(List.of(orden));

        assertEquals(1, service.getOrdenesByUsuarioId(1L).size());
    }

    private OrdenCompraService createService(OrdenCompraRepository repository) {
        OrdenCompraService service = new OrdenCompraService();
        ReflectionTestUtils.setField(service, "ordenCompraRepository", repository);
        return service;
    }
}
