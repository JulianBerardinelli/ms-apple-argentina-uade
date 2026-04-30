package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoRequest;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoResponse;
import com.apple.tpo.e_commerce.exception.BusinessException;
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
    void getItemsByCarritoId_returnsDtoResult() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ItemCarritoService service = createService(itemRepository);
        ItemCarrito item = new ItemCarrito();
        item.setCantidad(2);

        Mockito.when(itemRepository.findByCarritoId(1L)).thenReturn(List.of(item));

        List<ItemCarritoResponse> items = service.getItemsByCarritoId(1L);

        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getCantidad());
    }

    @Test
    void getItemById_whenItemExists_returnsDto() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ItemCarritoService service = createService(itemRepository);
        ItemCarrito item = new ItemCarrito();
        item.setId(1L);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertEquals(1L, service.getItemById(1L).getId());
    }

    @Test
    void getItemById_whenItemDoesNotExist_throwsResourceNotFoundException() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ItemCarritoService service = createService(itemRepository);

        Mockito.when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getItemById(99L));
    }

    @Test
    void createItem_whenDataIsValid_setsReferencesAndTotals() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemRepository, productoRepository, carritoRepository);
        Carrito carrito = new Carrito();
        carrito.setId(1L);
        Producto producto = new Producto();
        producto.setId(2L);
        producto.setPrecio(1500.0);
        ItemCarritoRequest request = request(1L, 2L, 3);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        Mockito.when(itemRepository.save(Mockito.any(ItemCarrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCarritoResponse creado = service.createItem(request);

        assertEquals(1L, creado.getCarritoId());
        assertEquals(2L, creado.getProducto().getId());
        assertEquals(1500.0, creado.getPrecioUnitario());
        assertEquals(4500.0, creado.getSubtotal());
    }

    @Test
    void createItem_whenCarritoIsMissing_throwsBusinessException() {
        ItemCarritoService service = createService(Mockito.mock(ItemCarritoRepository.class));

        assertThrows(BusinessException.class, () -> service.createItem(request(null, 2L, 1)));
    }

    @Test
    void createItem_whenProductoIsMissing_throwsBusinessException() {
        ItemCarritoService service = createService(Mockito.mock(ItemCarritoRepository.class));

        assertThrows(BusinessException.class, () -> service.createItem(request(1L, null, 1)));
    }

    @Test
    void createItem_whenCantidadIsInvalid_throwsBusinessException() {
        ItemCarritoService service = createService(Mockito.mock(ItemCarritoRepository.class));

        assertThrows(BusinessException.class, () -> service.createItem(request(1L, 2L, 0)));
    }

    @Test
    void createItem_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemRepository, productoRepository, carritoRepository);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createItem(request(1L, 2L, 1)));
    }

    @Test
    void createItem_whenProductoDoesNotExist_throwsResourceNotFoundException() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        ItemCarritoService service = createService(itemRepository, productoRepository, carritoRepository);
        Carrito carrito = new Carrito();

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createItem(request(1L, 2L, 1)));
    }

    @Test
    void deleteItem_whenItemExists_deletesById() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ItemCarritoService service = createService(itemRepository);

        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);

        service.deleteItem(1L);

        Mockito.verify(itemRepository).deleteById(1L);
    }

    @Test
    void deleteItem_whenItemDoesNotExist_throwsResourceNotFoundException() {
        ItemCarritoRepository itemRepository = Mockito.mock(ItemCarritoRepository.class);
        ItemCarritoService service = createService(itemRepository);

        Mockito.when(itemRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteItem(99L));
    }

    private ItemCarritoService createService(ItemCarritoRepository itemRepository) {
        return createService(itemRepository, Mockito.mock(ProductoRepository.class), Mockito.mock(CarritoRepository.class));
    }

    private ItemCarritoService createService(
            ItemCarritoRepository itemRepository,
            ProductoRepository productoRepository,
            CarritoRepository carritoRepository
    ) {
        ItemCarritoService service = new ItemCarritoService();
        ReflectionTestUtils.setField(service, "itemCarritoRepository", itemRepository);
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);
        ReflectionTestUtils.setField(service, "carritoRepository", carritoRepository);
        return service;
    }

    private ItemCarritoRequest request(Long carritoId, Long productoId, Integer cantidad) {
        ItemCarritoRequest request = new ItemCarritoRequest();
        request.setCarritoId(carritoId);
        request.setProductoId(productoId);
        request.setCantidad(cantidad);
        return request;
    }
}
