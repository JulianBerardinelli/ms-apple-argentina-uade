package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.CarritoRepository;
import com.apple.tpo.e_commerce.respository.DetalleOrdenRepository;
import com.apple.tpo.e_commerce.respository.OrdenCompraRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarritoServiceTest {

    @Test
    void getAllCarritos_returnsRepositoryResult() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Carrito carrito = new Carrito();
        carrito.setEstado("ACTIVO");

        Mockito.when(carritoRepository.findAll()).thenReturn(List.of(carrito));

        List<Carrito> carritos = service.getAllCarritos();

        assertEquals(1, carritos.size());
        assertEquals("ACTIVO", carritos.get(0).getEstado());
    }

    @Test
    void getCarritoById_whenCarritoExists_returnsCarrito() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Carrito carrito = new Carrito();
        carrito.setId(1L);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertEquals(carrito, service.getCarritoById(1L));
    }

    @Test
    void getCarritoById_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Mockito.when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCarritoById(99L));
    }

    @Test
    void getCarritosByUsuarioId_returnsRepositoryResult() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Carrito carrito = new Carrito();

        Mockito.when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(carrito));

        assertEquals(1, service.getCarritosByUsuarioId(1L).size());
    }

    @Test
    void createCarrito_setsFechaAndEstadoActivo() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Carrito carrito = new Carrito();

        Mockito.when(carritoRepository.save(carrito)).thenReturn(carrito);

        Carrito creado = service.createCarrito(carrito);

        assertNotNull(creado.getFechaCreacion());
        assertEquals("ACTIVO", creado.getEstado());
    }

    @Test
    void deleteCarrito_whenCarritoExists_deletesById() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Mockito.when(carritoRepository.existsById(1L)).thenReturn(true);

        service.deleteCarrito(1L);

        Mockito.verify(carritoRepository).deleteById(1L);
    }

    @Test
    void deleteCarrito_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Mockito.when(carritoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCarrito(99L));
    }

    @Test
    void checkout_whenCarritoIsValid_createsOrdenAndUpdatesStock() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Usuario usuario = new Usuario();
        Producto producto = new Producto();
        producto.setNombre("iPhone");
        producto.setStock(10);

        ItemCarrito item = new ItemCarrito();
        item.setProducto(producto);
        item.setCantidad(2);
        item.setPrecioUnitario(500.0);
        item.setSubtotal(1000.0);

        Carrito carrito = new Carrito();
        carrito.setId(1L);
        carrito.setUsuario(usuario);
        carrito.setEstado(" ACTIVO ");
        carrito.setItems(List.of(item));

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        Mockito.when(ordenCompraRepository.save(Mockito.any(OrdenCompra.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdenCompra orden = service.checkout(1L);

        assertEquals(usuario, orden.getUsuario());
        assertEquals(carrito, orden.getCarrito());
        assertEquals("COMPLETADA", orden.getEstado());
        assertEquals(1000.0, orden.getTotal());
        assertEquals(8, producto.getStock());
        assertEquals("CHECKOUT", carrito.getEstado());

        ArgumentCaptor<DetalleOrden> detalleCaptor = ArgumentCaptor.forClass(DetalleOrden.class);
        Mockito.verify(detalleOrdenRepository).save(detalleCaptor.capture());
        assertEquals(producto, detalleCaptor.getValue().getProducto());
        assertEquals(2, detalleCaptor.getValue().getCantidad());
        assertEquals(1000.0, detalleCaptor.getValue().getSubtotal());
    }

    @Test
    void checkout_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Mockito.when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.checkout(99L));
    }

    @Test
    void checkout_whenCarritoIsNotActive_throwsRuntimeException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Carrito carrito = new Carrito();
        carrito.setEstado("CHECKOUT");

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertThrows(RuntimeException.class, () -> service.checkout(1L));
    }

    @Test
    void checkout_whenCarritoIsEmpty_throwsRuntimeException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Carrito carrito = new Carrito();
        carrito.setEstado("ACTIVO");
        carrito.setItems(List.of());

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertThrows(RuntimeException.class, () -> service.checkout(1L));
    }

    @Test
    void checkout_whenStockIsInsufficient_throwsRuntimeException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository);

        Producto producto = new Producto();
        producto.setNombre("iPhone");
        producto.setStock(1);

        ItemCarrito item = new ItemCarrito();
        item.setProducto(producto);
        item.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setEstado("ACTIVO");
        carrito.setItems(List.of(item));

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertThrows(RuntimeException.class, () -> service.checkout(1L));
    }

    private CarritoService createService(
            CarritoRepository carritoRepository,
            OrdenCompraRepository ordenCompraRepository,
            DetalleOrdenRepository detalleOrdenRepository,
            ProductoRepository productoRepository
    ) {
        CarritoService service = new CarritoService();
        ReflectionTestUtils.setField(service, "carritoRepository", carritoRepository);
        ReflectionTestUtils.setField(service, "ordenCompraRepository", ordenCompraRepository);
        ReflectionTestUtils.setField(service, "detalleOrdenRepository", detalleOrdenRepository);
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);
        return service;
    }
}
