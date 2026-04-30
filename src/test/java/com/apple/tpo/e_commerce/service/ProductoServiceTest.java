package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.producto.ProductoRequest;
import com.apple.tpo.e_commerce.dto.producto.ProductoResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.CategoriaRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    @Test
    void getAllProductos_returnsDtoResult() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);
        Producto producto = new Producto();
        producto.setNombre("iPhone 15");

        Mockito.when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponse> productos = service.getAllProductos();

        assertEquals(1, productos.size());
        assertEquals("iPhone 15", productos.get(0).getNombre());
    }

    @Test
    void getProductoById_whenProductoExists_returnsDto() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);
        Producto producto = new Producto();
        producto.setId(1L);

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertEquals(1L, service.getProductoById(1L).getId());
    }

    @Test
    void getProductoById_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);

        Mockito.when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getProductoById(99L));
    }

    @Test
    void createProducto_whenActivoIsNull_setsActivoTrue() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);
        ProductoRequest request = request();
        request.setActivo(null);

        Mockito.when(productoRepository.save(Mockito.any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponse creado = service.createProducto(request);

        assertTrue(creado.getActivo());
        Mockito.verify(productoRepository).save(Mockito.any(Producto.class));
    }

    @Test
    void createProducto_whenActivoHasValue_keepsValue() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);
        ProductoRequest request = request();
        request.setActivo(false);

        Mockito.when(productoRepository.save(Mockito.any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponse creado = service.createProducto(request);

        assertFalse(creado.getActivo());
    }

    @Test
    void createProducto_whenReferencesArePresent_mapsThem() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        ProductoService service = createService(productoRepository, categoriaRepository, usuarioRepository);
        ProductoRequest request = request();
        request.setCategoriaId(10L);
        request.setUsuarioCreadorId(20L);
        Categoria categoria = new Categoria();
        categoria.setId(10L);
        Usuario usuario = new Usuario();
        usuario.setId(20L);

        Mockito.when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoria));
        Mockito.when(usuarioRepository.findById(20L)).thenReturn(Optional.of(usuario));
        Mockito.when(productoRepository.save(Mockito.any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponse creado = service.createProducto(request);

        assertEquals(10L, creado.getCategoria().getId());
        assertEquals(20L, creado.getUsuarioCreador().getId());
    }

    @Test
    void updateProducto_whenProductoExists_updatesFields() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);
        Producto existente = new Producto();
        ProductoRequest cambios = request();
        cambios.setNombre("iPad Pro");
        cambios.setPrecio(1200.0);

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(productoRepository.save(existente)).thenReturn(existente);

        ProductoResponse actualizado = service.updateProducto(1L, cambios);

        assertEquals("iPad Pro", actualizado.getNombre());
        assertEquals(1200.0, actualizado.getPrecio());
    }

    @Test
    void updateProducto_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);

        Mockito.when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateProducto(99L, new ProductoRequest()));
    }

    @Test
    void deleteProducto_whenProductoExists_deletesById() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);

        Mockito.when(productoRepository.existsById(1L)).thenReturn(true);

        service.deleteProducto(1L);

        Mockito.verify(productoRepository).deleteById(1L);
    }

    @Test
    void deleteProducto_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        ProductoService service = createService(productoRepository);

        Mockito.when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteProducto(99L));
    }

    private ProductoService createService(ProductoRepository productoRepository) {
        return createService(productoRepository, Mockito.mock(CategoriaRepository.class), Mockito.mock(UsuarioRepository.class));
    }

    private ProductoService createService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository
    ) {
        ProductoService service = new ProductoService();
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        return service;
    }

    private ProductoRequest request() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("MacBook Air");
        request.setDescripcion("Notebook");
        request.setPrecio(999.0);
        request.setStock(5);
        request.setActivo(true);
        return request;
    }
}
