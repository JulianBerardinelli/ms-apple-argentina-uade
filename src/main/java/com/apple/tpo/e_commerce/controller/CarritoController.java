package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.dto.carrito.CarritoRequest;
import com.apple.tpo.e_commerce.dto.carrito.CarritoResponse;
import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.service.CarritoService;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<CarritoResponse> getAllCarritos() {
        return carritoService.getAllCarritos();
    }

    @GetMapping("/{id}")
    public CarritoResponse getCarritoById(@PathVariable Long id) {
        return carritoService.getCarritoById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CarritoResponse> getCarritosByUsuario(@PathVariable Long usuarioId) {
        return carritoService.getCarritosByUsuarioId(usuarioId);
    }

    @PostMapping
    public CarritoResponse createCarrito(@RequestBody CarritoRequest request) {
        return carritoService.createCarrito(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
        return ResponseEntity.ok(ApiResponse.ok(HttpStatus.OK.value(), "Carrito eliminado correctamente", null));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<OrdenCompraResponse>> checkout(@PathVariable Long id) {
        OrdenCompraResponse orden = carritoService.checkout(id);
        return ResponseEntity.ok(ApiResponse.ok(HttpStatus.OK.value(), "Checkout realizado correctamente", orden));
    }
}
