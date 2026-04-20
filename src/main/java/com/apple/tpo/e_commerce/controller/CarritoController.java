package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.service.CarritoService;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<Carrito> getAllCarritos() {
        return carritoService.getAllCarritos();
    }

    @GetMapping("/{id}")
    public Carrito getCarritoById(@PathVariable Long id) {
        return carritoService.getCarritoById(id);
    }

    // Obtener todos los carritos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Carrito> getCarritosByUsuario(@PathVariable Long usuarioId) {
        return carritoService.getCarritosByUsuarioId(usuarioId);
    }

    @PostMapping
    public Carrito createCarrito(@RequestBody Carrito carrito) {
        return carritoService.createCarrito(carrito);
    }

    @DeleteMapping("/{id}")
    public void deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
    }

    // CHECKOUT: genera la OrdenCompra y descuenta el stock
    @PostMapping("/{id}/checkout")
    public ResponseEntity<?> checkout(@PathVariable Long id) {
        try {
            OrdenCompra orden = carritoService.checkout(id);
            return ResponseEntity.ok(orden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
