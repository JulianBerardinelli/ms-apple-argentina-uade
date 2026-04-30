package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.detalleorden.DetalleOrdenResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
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
    void getDetallesByOrdenId_returnsDtoResult() {
        DetalleOrdenRepository repository = Mockito.mock(DetalleOrdenRepository.class);
        DetalleOrdenService service = createService(repository);
        DetalleOrden detalle = new DetalleOrden();
        detalle.setCantidad(2);

        Mockito.when(repository.findByOrdenId(1L)).thenReturn(List.of(detalle));

        List<DetalleOrdenResponse> detalles = service.getDetallesByOrdenId(1L);

        assertEquals(1, detalles.size());
        assertEquals(2, detalles.get(0).getCantidad());
    }

    @Test
    void getDetalleById_whenDetalleExists_returnsDto() {
        DetalleOrdenRepository repository = Mockito.mock(DetalleOrdenRepository.class);
        DetalleOrdenService service = createService(repository);
        DetalleOrden detalle = new DetalleOrden();
        detalle.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(detalle));

        assertEquals(1L, service.getDetalleById(1L).getId());
    }

    @Test
    void getDetalleById_whenDetalleDoesNotExist_throwsResourceNotFoundException() {
        DetalleOrdenRepository repository = Mockito.mock(DetalleOrdenRepository.class);
        DetalleOrdenService service = createService(repository);

        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getDetalleById(99L));
    }

    private DetalleOrdenService createService(DetalleOrdenRepository repository) {
        DetalleOrdenService service = new DetalleOrdenService();
        ReflectionTestUtils.setField(service, "detalleOrdenRepository", repository);
        return service;
    }
}
