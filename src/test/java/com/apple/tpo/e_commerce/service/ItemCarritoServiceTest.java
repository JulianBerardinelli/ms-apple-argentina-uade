package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.respository.CarritoRepository;
import com.apple.tpo.e_commerce.respository.ItemCarritoRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemCarritoServiceTest {

    @Test
    void getItemsByCarritoId_returnsRepositoryResult() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        ItemCarrito item = new ItemCarrito();
        item.setCantidad(2);

        Mockito.when(itemCarritoRepository.findByCarritoId(1L)).thenReturn(List.of(item));

        List<ItemCarrito> items = service.getItemsByCarritoId(1L);

        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getCantidad());
    }

    @Test
    void getItemById_whenItemExists_returnsItem() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        ItemCarrito item = new ItemCarrito();
        item.setId(1L);

        Mockito.when(itemCarritoRepository.findById(1L)).thenReturn(Optional.of(item));

        assertEquals(item, service.getItemById(1L));
    }

    @Test
    void getItemById_whenItemDoesNotExist_returnsNull() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        Mockito.when(itemCarritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.getItemById(99L));
    }

    @Test
    void createItem_whenDataIsValid_setsReferencesAndTotals() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        Carrito carrito = new Carrito();
        carrito.setId(1L);

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setPrecio(1500.0);

        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(3);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        Mockito.when(itemCarritoRepository.save(item)).thenReturn(item);

        ItemCarrito creado = service.createItem(item);

        assertEquals(carrito, creado.getCarrito());
        assertEquals(producto, creado.getProducto());
        assertEquals(1500.0, creado.getPrecioUnitario());
        assertEquals(4500.0, creado.getSubtotal());
    }

    @Test
    void createItem_whenCarritoIsMissing_throwsRuntimeException() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        ItemCarrito item = new ItemCarrito();

        assertThrows(RuntimeException.class, () -> service.createItem(item));
    }

    @Test
    void createItem_whenProductoIsMissing_throwsRuntimeException() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        Carrito carrito = new Carrito();
        carrito.setId(1L);

        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);

        assertThrows(RuntimeException.class, () -> service.createItem(item));
    }

    @Test
    void createItem_whenCantidadIsInvalid_throwsRuntimeException() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        Carrito carrito = new Carrito();
        carrito.setId(1L);
        Producto producto = new Producto();
        producto.setId(2L);

        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(0);

        assertThrows(RuntimeException.class, () -> service.createItem(item));
    }

    @Test
    void createItem_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        Carrito carrito = new Carrito();
        carrito.setId(1L);
        Producto producto = new Producto();
        producto.setId(2L);

        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(1);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createItem(item));
    }

    @Test
    void createItem_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        Carrito carrito = new Carrito();
        carrito.setId(1L);
        Producto producto = new Producto();
        producto.setId(2L);

        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(1);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createItem(item));
    }

    @Test
    void deleteItem_deletesById() {
        ItemCarritoRepository itemCarritoRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemCarritoRepository, productoRepository, carritoRepository);

        service.deleteItem(1L);

        Mockito.verify(itemCarritoRepository).deleteById(1L);
    }

    private ItemCarritoService createService(
            ItemCarritoRepository itemCarritoRepository,
            ProductoRepository productoRepository,
            CarritoRepository carritoRepository
    ) {
        ItemCarritoService service = new ItemCarritoService();
        ReflectionTestUtils.setField(service, "itemCarritoRepository", itemCarritoRepository);
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);
        ReflectionTestUtils.setField(service, "carritoRepository", carritoRepository);
        return service;
    }
}
