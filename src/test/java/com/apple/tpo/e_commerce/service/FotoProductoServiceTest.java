package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoRequest;
import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.respository.FotoProductoRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FotoProductoServiceTest {

    @Test
    void getAllFotos_returnsDtoResult() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = createService(fotoRepository);
        FotoProducto foto = new FotoProducto();
        foto.setUrlImagen("https://img.test/iphone.png");

        Mockito.when(fotoRepository.findAll()).thenReturn(List.of(foto));

        List<FotoProductoResponse> fotos = service.getAllFotos();

        assertEquals(1, fotos.size());
        assertEquals("https://img.test/iphone.png", fotos.get(0).getUrlImagen());
    }

    @Test
    void getFotosByProductoId_returnsDtoResult() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = createService(fotoRepository);
        FotoProducto foto = new FotoProducto();

        Mockito.when(fotoRepository.findByProductoId(1L)).thenReturn(List.of(foto));

        assertEquals(1, service.getFotosByProductoId(1L).size());
    }

    @Test
    void getFotoById_whenFotoExists_returnsDto() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = createService(fotoRepository);
        FotoProducto foto = new FotoProducto();
        foto.setId(1L);

        Mockito.when(fotoRepository.findById(1L)).thenReturn(Optional.of(foto));

        assertEquals(1L, service.getFotoById(1L).getId());
    }

    @Test
    void getFotoById_whenFotoDoesNotExist_throwsResourceNotFoundException() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = createService(fotoRepository);

        Mockito.when(fotoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getFotoById(99L));
    }

    @Test
    void createFoto_savesFotoFromRequest() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        FotoProductoService service = createService(fotoRepository, productoRepository);
        FotoProductoRequest request = request();
        Producto producto = new Producto();
        producto.setId(2L);

        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        Mockito.when(fotoRepository.save(Mockito.any(FotoProducto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FotoProductoResponse creada = service.createFoto(request);

        assertEquals("https://img.test/iphone.png", creada.getUrlImagen());
        assertEquals(2L, creada.getProductoId());
    }

    @Test
    void createFoto_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        FotoProductoService service = createService(fotoRepository, productoRepository);
        FotoProductoRequest request = request();

        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createFoto(request));
    }

    @Test
    void deleteFoto_whenFotoExists_deletesById() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = createService(fotoRepository);

        Mockito.when(fotoRepository.existsById(1L)).thenReturn(true);

        service.deleteFoto(1L);

        Mockito.verify(fotoRepository).deleteById(1L);
    }

    @Test
    void deleteFoto_whenFotoDoesNotExist_throwsResourceNotFoundException() {
        FotoProductoRepository fotoRepository = Mockito.mock(FotoProductoRepository.class);
        FotoProductoService service = createService(fotoRepository);

        Mockito.when(fotoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteFoto(99L));
    }

    private FotoProductoService createService(FotoProductoRepository fotoRepository) {
        return createService(fotoRepository, Mockito.mock(ProductoRepository.class));
    }

    private FotoProductoService createService(FotoProductoRepository fotoRepository, ProductoRepository productoRepository) {
        FotoProductoService service = new FotoProductoService();
        ReflectionTestUtils.setField(service, "fotoProductoRepository", fotoRepository);
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);
        return service;
    }

    private FotoProductoRequest request() {
        FotoProductoRequest request = new FotoProductoRequest();
        request.setUrlImagen("https://img.test/iphone.png");
        request.setOrden(1);
        request.setProductoId(2L);
        return request;
    }
}
