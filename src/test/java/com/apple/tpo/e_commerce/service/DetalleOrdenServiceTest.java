package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.respository.DetalleOrdenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DetalleOrdenServiceTest {

    @Test
    void getDetallesByOrdenId_returnsRepositoryResult() {
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        DetalleOrdenService service = new DetalleOrdenService();
        ReflectionTestUtils.setField(service, "detalleOrdenRepository", detalleOrdenRepository);

        DetalleOrden detalle = new DetalleOrden();
        detalle.setCantidad(2);

        Mockito.when(detalleOrdenRepository.findByOrdenId(1L)).thenReturn(List.of(detalle));

        List<DetalleOrden> detalles = service.getDetallesByOrdenId(1L);

        assertEquals(1, detalles.size());
        assertEquals(2, detalles.get(0).getCantidad());
    }

    @Test
    void getDetalleById_whenDetalleExists_returnsDetalle() {
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        DetalleOrdenService service = new DetalleOrdenService();
        ReflectionTestUtils.setField(service, "detalleOrdenRepository", detalleOrdenRepository);

        DetalleOrden detalle = new DetalleOrden();
        detalle.setId(1L);

        Mockito.when(detalleOrdenRepository.findById(1L)).thenReturn(Optional.of(detalle));

        assertEquals(detalle, service.getDetalleById(1L));
    }

    @Test
    void getDetalleById_whenDetalleDoesNotExist_returnsNull() {
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        DetalleOrdenService service = new DetalleOrdenService();
        ReflectionTestUtils.setField(service, "detalleOrdenRepository", detalleOrdenRepository);

        Mockito.when(detalleOrdenRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.getDetalleById(99L));
    }
}
