package com.apple.tpo.e_commerce.controller;

import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.dto.auth.AuthResponse;
import com.apple.tpo.e_commerce.dto.auth.LoginRequest;
import com.apple.tpo.e_commerce.dto.auth.RegisterRequest;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.service.AuthService;
import com.apple.tpo.e_commerce.service.CarritoService;
import com.apple.tpo.e_commerce.service.CategoriaService;
import com.apple.tpo.e_commerce.service.DetalleOrdenService;
import com.apple.tpo.e_commerce.service.FotoProductoService;
import com.apple.tpo.e_commerce.service.ItemCarritoService;
import com.apple.tpo.e_commerce.service.OrdenCompraService;
import com.apple.tpo.e_commerce.service.ProductoService;
import com.apple.tpo.e_commerce.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllersTest {

    @Test
    void authController_delegatesToService() {
        AuthService authService = Mockito.mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        RegisterRequest registerRequest = new RegisterRequest();
        LoginRequest loginRequest = new LoginRequest();
        AuthResponse authResponse = new AuthResponse("token", "usuario@uade.com", "ROLE_USER");

        Mockito.when(authService.register(registerRequest)).thenReturn(authResponse);
        Mockito.when(authService.login(loginRequest)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> registerResponse = controller.register(registerRequest);
        ResponseEntity<AuthResponse> loginResponse = controller.login(loginRequest);

        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
        assertEquals(authResponse, registerResponse.getBody());
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertEquals(authResponse, loginResponse.getBody());
    }

    @Test
    void categoriaController_delegatesToService() {
        CategoriaService categoriaService = Mockito.mock(CategoriaService.class);
        CategoriaController controller = new CategoriaController();
        ReflectionTestUtils.setField(controller, "categoriaService", categoriaService);

        Categoria categoria = new Categoria();

        Mockito.when(categoriaService.getAllCategorias()).thenReturn(List.of(categoria));
        Mockito.when(categoriaService.getCategoriaById(1L)).thenReturn(categoria);
        Mockito.when(categoriaService.createCategoria(categoria)).thenReturn(categoria);
        Mockito.when(categoriaService.updateCategoria(1L, categoria)).thenReturn(categoria);

        assertEquals(1, controller.getAllCategorias().size());
        assertEquals(categoria, controller.getCategoriaById(1L));
        assertEquals(categoria, controller.createCategoria(categoria));
        assertEquals(categoria, controller.updateCategoria(1L, categoria));

        ResponseEntity<ApiResponse<Void>> response = controller.deleteCategoria(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        Mockito.verify(categoriaService).deleteCategoria(1L);
    }

    @Test
    void productoController_delegatesToService() {
        ProductoService productoService = Mockito.mock(ProductoService.class);
        ProductoController controller = new ProductoController();
        ReflectionTestUtils.setField(controller, "productoService", productoService);

        Producto producto = new Producto();

        Mockito.when(productoService.getAllProductos()).thenReturn(List.of(producto));
        Mockito.when(productoService.getProductoById(1L)).thenReturn(producto);
        Mockito.when(productoService.createProducto(producto)).thenReturn(producto);
        Mockito.when(productoService.updateProducto(1L, producto)).thenReturn(producto);

        assertEquals(1, controller.getAllProductos().size());
        assertEquals(producto, controller.getProductoById(1L));
        assertEquals(producto, controller.createProducto(producto));
        assertEquals(producto, controller.updateProducto(1L, producto));

        ResponseEntity<ApiResponse<Void>> response = controller.deleteProducto(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        Mockito.verify(productoService).deleteProducto(1L);
    }

    @Test
    void usuarioController_delegatesToService() {
        UsuarioService usuarioService = Mockito.mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController();
        ReflectionTestUtils.setField(controller, "usuarioService", usuarioService);

        Usuario usuario = new Usuario();

        Mockito.when(usuarioService.getAllUsuarios()).thenReturn(List.of(usuario));
        Mockito.when(usuarioService.getUsuarioById(1L)).thenReturn(usuario);
        Mockito.when(usuarioService.createUsuario(usuario)).thenReturn(usuario);
        Mockito.when(usuarioService.updateUsuario(1L, usuario)).thenReturn(usuario);

        assertEquals(1, controller.getAllUsuarios().size());
        assertEquals(usuario, controller.getUsuarioById(1L));
        assertEquals(usuario, controller.createUsuario(usuario));
        assertEquals(usuario, controller.updateUsuario(1L, usuario));

        ResponseEntity<ApiResponse<Void>> response = controller.deleteUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        Mockito.verify(usuarioService).deleteUsuario(1L);
    }

    @Test
    void carritoController_delegatesToService() {
        CarritoService carritoService = Mockito.mock(CarritoService.class);
        CarritoController controller = new CarritoController();
        ReflectionTestUtils.setField(controller, "carritoService", carritoService);

        Carrito carrito = new Carrito();
        OrdenCompra orden = new OrdenCompra();

        Mockito.when(carritoService.getAllCarritos()).thenReturn(List.of(carrito));
        Mockito.when(carritoService.getCarritoById(1L)).thenReturn(carrito);
        Mockito.when(carritoService.getCarritosByUsuarioId(2L)).thenReturn(List.of(carrito));
        Mockito.when(carritoService.createCarrito(carrito)).thenReturn(carrito);
        Mockito.when(carritoService.checkout(1L)).thenReturn(orden);

        assertEquals(1, controller.getAllCarritos().size());
        assertEquals(carrito, controller.getCarritoById(1L));
        assertEquals(1, controller.getCarritosByUsuario(2L).size());
        assertEquals(carrito, controller.createCarrito(carrito));

        ResponseEntity<ApiResponse<Void>> deleteResponse = controller.deleteCarrito(1L);
        ResponseEntity<ApiResponse<OrdenCompra>> checkoutResponse = controller.checkout(1L);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertNotNull(deleteResponse.getBody());
        assertTrue(deleteResponse.getBody().getSuccess());
        assertEquals(HttpStatus.OK, checkoutResponse.getStatusCode());
        assertNotNull(checkoutResponse.getBody());
        assertEquals(orden, checkoutResponse.getBody().getData());
        Mockito.verify(carritoService).deleteCarrito(1L);
    }

    @Test
    void itemCarritoController_delegatesToService() {
        ItemCarritoService itemCarritoService = Mockito.mock(ItemCarritoService.class);
        ItemCarritoController controller = new ItemCarritoController();
        ReflectionTestUtils.setField(controller, "itemCarritoService", itemCarritoService);

        ItemCarrito item = new ItemCarrito();

        Mockito.when(itemCarritoService.getItemById(1L)).thenReturn(item);
        Mockito.when(itemCarritoService.getItemsByCarritoId(2L)).thenReturn(List.of(item));
        Mockito.when(itemCarritoService.createItem(item)).thenReturn(item);

        assertEquals(item, controller.getItemById(1L));
        assertEquals(1, controller.getItemsByCarrito(2L).size());
        assertEquals(item, controller.createItem(item));

        ResponseEntity<ApiResponse<Void>> response = controller.deleteItem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        Mockito.verify(itemCarritoService).deleteItem(1L);
    }

    @Test
    void fotoProductoController_delegatesToService() {
        FotoProductoService fotoProductoService = Mockito.mock(FotoProductoService.class);
        FotoProductoController controller = new FotoProductoController();
        ReflectionTestUtils.setField(controller, "fotoProductoService", fotoProductoService);

        FotoProducto foto = new FotoProducto();

        Mockito.when(fotoProductoService.getAllFotos()).thenReturn(List.of(foto));
        Mockito.when(fotoProductoService.getFotoById(1L)).thenReturn(foto);
        Mockito.when(fotoProductoService.getFotosByProductoId(2L)).thenReturn(List.of(foto));
        Mockito.when(fotoProductoService.createFoto(foto)).thenReturn(foto);

        assertEquals(1, controller.getAllFotos().size());
        assertEquals(foto, controller.getFotoById(1L));
        assertEquals(1, controller.getFotosByProducto(2L).size());
        assertEquals(foto, controller.createFoto(foto));

        ResponseEntity<ApiResponse<Void>> response = controller.deleteFoto(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        Mockito.verify(fotoProductoService).deleteFoto(1L);
    }

    @Test
    void detalleOrdenController_delegatesToService() {
        DetalleOrdenService detalleOrdenService = Mockito.mock(DetalleOrdenService.class);
        DetalleOrdenController controller = new DetalleOrdenController();
        ReflectionTestUtils.setField(controller, "detalleOrdenService", detalleOrdenService);

        DetalleOrden detalle = new DetalleOrden();

        Mockito.when(detalleOrdenService.getDetalleById(1L)).thenReturn(detalle);
        Mockito.when(detalleOrdenService.getDetallesByOrdenId(2L)).thenReturn(List.of(detalle));

        assertEquals(detalle, controller.getDetalleById(1L));
        assertEquals(1, controller.getDetallesByOrden(2L).size());
    }

    @Test
    void ordenCompraController_delegatesToService() {
        OrdenCompraService ordenCompraService = Mockito.mock(OrdenCompraService.class);
        OrdenCompraController controller = new OrdenCompraController();
        ReflectionTestUtils.setField(controller, "ordenCompraService", ordenCompraService);

        OrdenCompra orden = new OrdenCompra();

        Mockito.when(ordenCompraService.getAllOrdenes()).thenReturn(List.of(orden));
        Mockito.when(ordenCompraService.getOrdenById(1L)).thenReturn(orden);
        Mockito.when(ordenCompraService.getOrdenesByUsuarioId(2L)).thenReturn(List.of(orden));

        assertEquals(1, controller.getAllOrdenes().size());
        assertEquals(orden, controller.getOrdenById(1L));
        assertEquals(1, controller.getOrdenesByUsuario(2L).size());
    }
}
