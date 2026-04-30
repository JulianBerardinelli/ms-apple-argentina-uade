package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.carrito.CarritoRequest;
import com.apple.tpo.e_commerce.dto.carrito.CarritoResponse;
import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.exception.BusinessException;
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
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarritoServiceTest {

    @Test
    void getAllCarritos_returnsDtoResult() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
        Carrito carrito = new Carrito();
        carrito.setEstado("ACTIVO");

        Mockito.when(carritoRepository.findAll()).thenReturn(List.of(carrito));

        List<CarritoResponse> carritos = service.getAllCarritos();

        assertEquals(1, carritos.size());
        assertEquals("ACTIVO", carritos.get(0).getEstado());
    }

    @Test
    void getCarritoById_whenCarritoExists_returnsDto() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
        Carrito carrito = new Carrito();
        carrito.setId(1L);

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertEquals(1L, service.getCarritoById(1L).getId());
    }

    @Test
    void getCarritoById_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);

        Mockito.when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCarritoById(99L));
    }

    @Test
    void getCarritosByUsuarioId_returnsDtoResult() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
        Carrito carrito = new Carrito();

        Mockito.when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(carrito));

        assertEquals(1, service.getCarritosByUsuarioId(1L).size());
    }

    @Test
    void createCarrito_setsFechaAndEstadoActivo() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
        CarritoRequest request = new CarritoRequest();

        Mockito.when(carritoRepository.save(Mockito.any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoResponse creado = service.createCarrito(request);

        assertNotNull(creado.getFechaCreacion());
        assertEquals("ACTIVO", creado.getEstado());
    }

    @Test
    void createCarrito_whenUsuarioIdIsPresent_setsUsuario() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        CarritoService service = createService(
                carritoRepository,
                Mockito.mock(OrdenCompraRepository.class),
                Mockito.mock(DetalleOrdenRepository.class),
                Mockito.mock(ProductoRepository.class),
                usuarioRepository
        );
        CarritoRequest request = new CarritoRequest();
        request.setUsuarioId(7L);
        Usuario usuario = new Usuario();
        usuario.setId(7L);

        Mockito.when(usuarioRepository.findById(7L)).thenReturn(Optional.of(usuario));
        Mockito.when(carritoRepository.save(Mockito.any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoResponse creado = service.createCarrito(request);

        assertEquals(7L, creado.getUsuario().getId());
    }

    @Test
    void deleteCarrito_whenCarritoExists_deletesById() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);

        Mockito.when(carritoRepository.existsById(1L)).thenReturn(true);

        service.deleteCarrito(1L);

        Mockito.verify(carritoRepository).deleteById(1L);
    }

    @Test
    void deleteCarrito_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);

        Mockito.when(carritoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCarrito(99L));
    }

    @Test
    void checkout_whenCarritoIsValid_createsOrdenAndUpdatesStock() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        OrdenCompraRepository ordenCompraRepository = Mockito.mock(OrdenCompraRepository.class);
        DetalleOrdenRepository detalleOrdenRepository = Mockito.mock(DetalleOrdenRepository.class);
        ProductoRepository productoRepository = Mockito.mock(ProductoRepository.class);
        CarritoService service = createService(carritoRepository, ordenCompraRepository, detalleOrdenRepository, productoRepository,
                Mockito.mock(UsuarioRepository.class));

        Usuario usuario = new Usuario();
        usuario.setId(3L);
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
        Mockito.when(ordenCompraRepository.save(Mockito.any(OrdenCompra.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(detalleOrdenRepository.save(Mockito.any(DetalleOrden.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrdenCompraResponse orden = service.checkout(1L);

        assertEquals(3L, orden.getUsuario().getId());
        assertEquals(1L, orden.getCarritoId());
        assertEquals("COMPLETADA", orden.getEstado());
        assertEquals(1000.0, orden.getTotal());
        assertEquals(8, producto.getStock());
        assertEquals("CHECKOUT", carrito.getEstado());
        assertEquals(1, orden.getDetalles().size());

        ArgumentCaptor<DetalleOrden> detalleCaptor = ArgumentCaptor.forClass(DetalleOrden.class);
        Mockito.verify(detalleOrdenRepository).save(detalleCaptor.capture());
        assertEquals(producto, detalleCaptor.getValue().getProducto());
        assertEquals(2, detalleCaptor.getValue().getCantidad());
        assertEquals(1000.0, detalleCaptor.getValue().getSubtotal());
    }

    @Test
    void checkout_whenCarritoDoesNotExist_throwsResourceNotFoundException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);

        Mockito.when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.checkout(99L));
    }

    @Test
    void checkout_whenCarritoIsNotActive_throwsBusinessException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
        Carrito carrito = new Carrito();
        carrito.setEstado("CHECKOUT");

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertThrows(BusinessException.class, () -> service.checkout(1L));
    }

    @Test
    void checkout_whenCarritoIsEmpty_throwsBusinessException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
        Carrito carrito = new Carrito();
        carrito.setEstado("ACTIVO");
        carrito.setItems(List.of());

        Mockito.when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        assertThrows(BusinessException.class, () -> service.checkout(1L));
    }

    @Test
    void checkout_whenStockIsInsufficient_throwsBusinessException() {
        CarritoRepository carritoRepository = Mockito.mock(CarritoRepository.class);
        CarritoService service = createService(carritoRepository);
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

        assertThrows(BusinessException.class, () -> service.checkout(1L));
    }

    private CarritoService createService(CarritoRepository carritoRepository) {
        return createService(
                carritoRepository,
                Mockito.mock(OrdenCompraRepository.class),
                Mockito.mock(DetalleOrdenRepository.class),
                Mockito.mock(ProductoRepository.class),
                Mockito.mock(UsuarioRepository.class)
        );
    }

    private CarritoService createService(
            CarritoRepository carritoRepository,
            OrdenCompraRepository ordenCompraRepository,
            DetalleOrdenRepository detalleOrdenRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository
    ) {
        CarritoService service = new CarritoService();
        ReflectionTestUtils.setField(service, "carritoRepository", carritoRepository);
        ReflectionTestUtils.setField(service, "ordenCompraRepository", ordenCompraRepository);
        ReflectionTestUtils.setField(service, "detalleOrdenRepository", detalleOrdenRepository);
        ReflectionTestUtils.setField(service, "productoRepository", productoRepository);
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        return service;
    }
}
