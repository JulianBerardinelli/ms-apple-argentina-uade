package com.apple.tpo.e_commerce.service;

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
    void getAllOrdenes_returnsRepositoryResult() {
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = new OrdenCompraService();
        ReflectionTestUtils.setField(service, "ordenCompraRepository", ordenCompraRepository);

        OrdenCompra orden = new OrdenCompra();
        orden.setEstado("COMPLETADA");

        Mockito.when(ordenCompraRepository.findAll()).thenReturn(List.of(orden));

        List<OrdenCompra> ordenes = service.getAllOrdenes();

        assertEquals(1, ordenes.size());
        assertEquals("COMPLETADA", ordenes.get(0).getEstado());
    }

    @Test
    void getOrdenById_whenOrdenExists_returnsOrden() {
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = new OrdenCompraService();
        ReflectionTestUtils.setField(service, "ordenCompraRepository", ordenCompraRepository);

        OrdenCompra orden = new OrdenCompra();
        orden.setId(1L);

        Mockito.when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertEquals(orden, service.getOrdenById(1L));
    }

    @Test
    void getOrdenById_whenOrdenDoesNotExist_returnsNull() {
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = new OrdenCompraService();
        ReflectionTestUtils.setField(service, "ordenCompraRepository", ordenCompraRepository);

        Mockito.when(ordenCompraRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.getOrdenById(99L));
    }

    @Test
    void getOrdenesByUsuarioId_returnsRepositoryResult() {
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        OrdenCompraService service = new OrdenCompraService();
        ReflectionTestUtils.setField(service, "ordenCompraRepository", ordenCompraRepository);

        OrdenCompra orden = new OrdenCompra();

        Mockito.when(ordenCompraRepository.findByUsuarioId(1L)).thenReturn(List.of(orden));

        assertEquals(1, service.getOrdenesByUsuarioId(1L).size());
    }
}
