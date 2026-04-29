package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.respository.FotoProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FotoProductoServiceTest {

    @Test
    void getAllFotos_returnsRepositoryResult() {
        FotoProductoRepository fotoProductoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoProductoRepository);

        FotoProducto foto = new FotoProducto();
        foto.setUrlImagen("https://img.test/iphone.png");

        Mockito.when(fotoProductoRepository.findAll()).thenReturn(List.of(foto));

        List<FotoProducto> fotos = service.getAllFotos();

        assertEquals(1, fotos.size());
        assertEquals("https://img.test/iphone.png", fotos.get(0).getUrlImagen());
    }

    @Test
    void getFotosByProductoId_returnsRepositoryResult() {
        FotoProductoRepository fotoProductoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoProductoRepository);

        FotoProducto foto = new FotoProducto();

        Mockito.when(fotoProductoRepository.findByProductoId(1L)).thenReturn(List.of(foto));

        assertEquals(1, service.getFotosByProductoId(1L).size());
    }

    @Test
    void getFotoById_whenFotoExists_returnsFoto() {
        FotoProductoRepository fotoProductoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoProductoRepository);

        FotoProducto foto = new FotoProducto();
        foto.setId(1L);

        Mockito.when(fotoProductoRepository.findById(1L)).thenReturn(Optional.of(foto));

        assertEquals(foto, service.getFotoById(1L));
    }

    @Test
    void getFotoById_whenFotoDoesNotExist_returnsNull() {
        FotoProductoRepository fotoProductoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoProductoRepository);

        Mockito.when(fotoProductoRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.getFotoById(99L));
    }

    @Test
    void createFoto_savesFoto() {
        FotoProductoRepository fotoProductoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoProductoRepository);

        FotoProducto foto = new FotoProducto();

        Mockito.when(fotoProductoRepository.save(foto)).thenReturn(foto);

        assertEquals(foto, service.createFoto(foto));
        Mockito.verify(fotoProductoRepository).save(foto);
    }

    @Test
    void deleteFoto_deletesById() {
        FotoProductoRepository fotoProductoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoProductoRepository);

        service.deleteFoto(1L);

        Mockito.verify(fotoProductoRepository).deleteById(1L);
    }
}
