package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    @Test
    void getAllProductos_returnsRepositoryResult() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Producto producto = new Producto();
        producto.setNombre("iPhone 15");

        Mockito.when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> productos = service.getAllProductos();

        assertEquals(1, productos.size());
        assertEquals("iPhone 15", productos.get(0).getNombre());
    }

    @Test
    void getProductoById_whenProductoExists_returnsProducto() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Producto producto = new Producto();
        producto.setId(1L);

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertEquals(producto, service.getProductoById(1L));
    }

    @Test
    void getProductoById_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Mockito.when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getProductoById(99L));
    }

    @Test
    void createProducto_whenActivoIsNull_setsActivoTrue() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Producto producto = new Producto();
        producto.setNombre("MacBook Air");

        Mockito.when(productoRepository.save(producto)).thenReturn(producto);

        Producto creado = service.createProducto(producto);

        assertTrue(creado.getActivo());
        Mockito.verify(productoRepository).save(producto);
    }

    @Test
    void createProducto_whenActivoHasValue_keepsValue() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Producto producto = new Producto();
        producto.setActivo(false);

        Mockito.when(productoRepository.save(producto)).thenReturn(producto);

        Producto creado = service.createProducto(producto);

        assertFalse(creado.getActivo());
    }

    @Test
    void updateProducto_whenProductoExists_updatesFields() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Categoria categoria = new Categoria();
        Usuario usuario = new Usuario();
        Producto existente = new Producto();

        Producto cambios = new Producto();
        cambios.setNombre("iPad Pro");
        cambios.setDescripcion("Tablet");
        cambios.setPrecio(1200.0);
        cambios.setStock(5);
        cambios.setActivo(true);
        cambios.setCategoria(categoria);
        cambios.setUsuarioCreador(usuario);

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(productoRepository.save(existente)).thenReturn(existente);

        Producto actualizado = service.updateProducto(1L, cambios);

        assertEquals("iPad Pro", actualizado.getNombre());
        assertEquals("Tablet", actualizado.getDescripcion());
        assertEquals(1200.0, actualizado.getPrecio());
        assertEquals(5, actualizado.getStock());
        assertTrue(actualizado.getActivo());
        assertEquals(categoria, actualizado.getCategoria());
        assertEquals(usuario, actualizado.getUsuarioCreador());
    }

    @Test
    void updateProducto_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Mockito.when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateProducto(99L, new Producto()));
    }

    @Test
    void deleteProducto_whenProductoExists_deletesById() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Mockito.when(productoRepository.existsById(1L)).thenReturn(true);

        service.deleteProducto(1L);

        Mockito.verify(productoRepository).deleteById(1L);
    }

    @Test
    void deleteProducto_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);

        Mockito.when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteProducto(99L));
    }
}
