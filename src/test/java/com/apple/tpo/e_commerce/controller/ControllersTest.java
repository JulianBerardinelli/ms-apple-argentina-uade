package com.apple.tpo.e_commerce.controller;

import com.apple.tpo.e_commerce.dto.auth.AuthResponse;
import com.apple.tpo.e_commerce.dto.auth.LoginRequest;
import com.apple.tpo.e_commerce.dto.auth.RegisterRequest;
import com.apple.tpo.e_commerce.dto.carrito.CarritoRequest;
import com.apple.tpo.e_commerce.dto.carrito.CarritoResponse;
import com.apple.tpo.e_commerce.dto.categoria.CategoriaRequest;
import com.apple.tpo.e_commerce.dto.categoria.CategoriaResponse;
import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.dto.detalleorden.DetalleOrdenResponse;
import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoRequest;
import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoResponse;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoRequest;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoResponse;
import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.dto.producto.ProductoRequest;
import com.apple.tpo.e_commerce.dto.producto.ProductoResponse;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioRequest;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;
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
    void categoriaController_delegatesDtoToService() {
        CategoriaService service = Mockito.mock(CategoriaService.class);
        CategoriaController controller = new CategoriaController();
        ReflectionTestUtils.setField(controller, "categoriaService", service);
        CategoriaRequest request = new CategoriaRequest();
        CategoriaResponse responseDto = new CategoriaResponse();

        Mockito.when(service.getAllCategorias()).thenReturn(List.of(responseDto));
        Mockito.when(service.getCategoriaById(1L)).thenReturn(responseDto);
        Mockito.when(service.createCategoria(request)).thenReturn(responseDto);
        Mockito.when(service.updateCategoria(1L, request)).thenReturn(responseDto);

        assertEquals(1, controller.getAllCategorias().size());
        assertEquals(responseDto, controller.getCategoriaById(1L));
        assertEquals(responseDto, controller.createCategoria(request));
        assertEquals(responseDto, controller.updateCategoria(1L, request));
        assertDeleteOk(controller.deleteCategoria(1L));
        Mockito.verify(service).deleteCategoria(1L);
    }

    @Test
    void productoController_delegatesDtoToService() {
        ProductoService service = Mockito.mock(ProductoService.class);
        ProductoController controller = new ProductoController();
        ReflectionTestUtils.setField(controller, "productoService", service);
        ProductoRequest request = new ProductoRequest();
        ProductoResponse responseDto = new ProductoResponse();

        Mockito.when(service.getAllProductos()).thenReturn(List.of(responseDto));
        Mockito.when(service.getProductoById(1L)).thenReturn(responseDto);
        Mockito.when(service.createProducto(request)).thenReturn(responseDto);
        Mockito.when(service.updateProducto(1L, request)).thenReturn(responseDto);

        assertEquals(1, controller.getAllProductos().size());
        assertEquals(responseDto, controller.getProductoById(1L));
        assertEquals(responseDto, controller.createProducto(request));
        assertEquals(responseDto, controller.updateProducto(1L, request));
        assertDeleteOk(controller.deleteProducto(1L));
        Mockito.verify(service).deleteProducto(1L);
    }

    @Test
    void usuarioController_delegatesDtoToService() {
        UsuarioService service = Mockito.mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController();
        ReflectionTestUtils.setField(controller, "usuarioService", service);
        UsuarioRequest request = new UsuarioRequest();
        UsuarioResponse responseDto = new UsuarioResponse();

        Mockito.when(service.getAllUsuarios()).thenReturn(List.of(responseDto));
        Mockito.when(service.getUsuarioById(1L)).thenReturn(responseDto);
        Mockito.when(service.createUsuario(request)).thenReturn(responseDto);
        Mockito.when(service.updateUsuario(1L, request)).thenReturn(responseDto);

        assertEquals(1, controller.getAllUsuarios().size());
        assertEquals(responseDto, controller.getUsuarioById(1L));
        assertEquals(responseDto, controller.createUsuario(request));
        assertEquals(responseDto, controller.updateUsuario(1L, request));
        assertDeleteOk(controller.deleteUsuario(1L));
        Mockito.verify(service).deleteUsuario(1L);
    }

    @Test
    void carritoController_delegatesDtoToService() {
        CarritoService service = Mockito.mock(CarritoService.class);
        CarritoController controller = new CarritoController();
        ReflectionTestUtils.setField(controller, "carritoService", service);
        CarritoRequest request = new CarritoRequest();
        CarritoResponse responseDto = new CarritoResponse();
        OrdenCompraResponse orden = new OrdenCompraResponse();

        Mockito.when(service.getAllCarritos()).thenReturn(List.of(responseDto));
        Mockito.when(service.getCarritoById(1L)).thenReturn(responseDto);
        Mockito.when(service.getCarritosByUsuarioId(2L)).thenReturn(List.of(responseDto));
        Mockito.when(service.createCarrito(request)).thenReturn(responseDto);
        Mockito.when(service.checkout(1L)).thenReturn(orden);

        assertEquals(1, controller.getAllCarritos().size());
        assertEquals(responseDto, controller.getCarritoById(1L));
        assertEquals(1, controller.getCarritosByUsuario(2L).size());
        assertEquals(responseDto, controller.createCarrito(request));

        assertDeleteOk(controller.deleteCarrito(1L));
        ResponseEntity<ApiResponse<OrdenCompraResponse>> checkoutResponse = controller.checkout(1L);
        assertEquals(HttpStatus.OK, checkoutResponse.getStatusCode());
        assertNotNull(checkoutResponse.getBody());
        assertEquals(orden, checkoutResponse.getBody().getData());
        Mockito.verify(service).deleteCarrito(1L);
    }

    @Test
    void itemCarritoController_delegatesDtoToService() {
        ItemCarritoService service = Mockito.mock(ItemCarritoService.class);
        ItemCarritoController controller = new ItemCarritoController();
        ReflectionTestUtils.setField(controller, "itemCarritoService", service);
        ItemCarritoRequest request = new ItemCarritoRequest();
        ItemCarritoResponse responseDto = new ItemCarritoResponse();

        Mockito.when(service.getItemById(1L)).thenReturn(responseDto);
        Mockito.when(service.getItemsByCarritoId(2L)).thenReturn(List.of(responseDto));
        Mockito.when(service.createItem(request)).thenReturn(responseDto);

        assertEquals(responseDto, controller.getItemById(1L));
        assertEquals(1, controller.getItemsByCarrito(2L).size());
        assertEquals(responseDto, controller.createItem(request));
        assertDeleteOk(controller.deleteItem(1L));
        Mockito.verify(service).deleteItem(1L);
    }

    @Test
    void fotoProductoController_delegatesDtoToService() {
        FotoProductoService service = Mockito.mock(FotoProductoService.class);
        FotoProductoController controller = new FotoProductoController();
        ReflectionTestUtils.setField(controller, "fotoProductoService", service);
        FotoProductoRequest request = new FotoProductoRequest();
        FotoProductoResponse responseDto = new FotoProductoResponse();

        Mockito.when(service.getAllFotos()).thenReturn(List.of(responseDto));
        Mockito.when(service.getFotoById(1L)).thenReturn(responseDto);
        Mockito.when(service.getFotosByProductoId(2L)).thenReturn(List.of(responseDto));
        Mockito.when(service.createFoto(request)).thenReturn(responseDto);

        assertEquals(1, controller.getAllFotos().size());
        assertEquals(responseDto, controller.getFotoById(1L));
        assertEquals(1, controller.getFotosByProducto(2L).size());
        assertEquals(responseDto, controller.createFoto(request));
        assertDeleteOk(controller.deleteFoto(1L));
        Mockito.verify(service).deleteFoto(1L);
    }

    @Test
    void detalleOrdenController_delegatesToService() {
        DetalleOrdenService service = Mockito.mock(DetalleOrdenService.class);
        DetalleOrdenController controller = new DetalleOrdenController();
        ReflectionTestUtils.setField(controller, "detalleOrdenService", service);
        DetalleOrdenResponse responseDto = new DetalleOrdenResponse();

        Mockito.when(service.getDetalleById(1L)).thenReturn(responseDto);
        Mockito.when(service.getDetallesByOrdenId(2L)).thenReturn(List.of(responseDto));

        assertEquals(responseDto, controller.getDetalleById(1L));
        assertEquals(1, controller.getDetallesByOrden(2L).size());
    }

    @Test
    void ordenCompraController_delegatesToService() {
        OrdenCompraService service = Mockito.mock(OrdenCompraService.class);
        OrdenCompraController controller = new OrdenCompraController();
        ReflectionTestUtils.setField(controller, "ordenCompraService", service);
        OrdenCompraResponse responseDto = new OrdenCompraResponse();

        Mockito.when(service.getAllOrdenes()).thenReturn(List.of(responseDto));
        Mockito.when(service.getOrdenById(1L)).thenReturn(responseDto);
        Mockito.when(service.getOrdenesByUsuarioId(2L)).thenReturn(List.of(responseDto));

        assertEquals(1, controller.getAllOrdenes().size());
        assertEquals(responseDto, controller.getOrdenById(1L));
        assertEquals(1, controller.getOrdenesByUsuario(2L).size());
    }

    private void assertDeleteOk(ResponseEntity<ApiResponse<Void>> response) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
    }
}
