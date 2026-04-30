package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.service.OrdenCompraService;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;

    @GetMapping
    public List<OrdenCompraResponse> getAllOrdenes() {
        return ordenCompraService.getAllOrdenes();
    }

    @GetMapping("/{id}")
    public OrdenCompraResponse getOrdenById(@PathVariable Long id) {
        return ordenCompraService.getOrdenById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<OrdenCompraResponse> getOrdenesByUsuario(@PathVariable Long usuarioId) {
        return ordenCompraService.getOrdenesByUsuarioId(usuarioId);
    }
}
