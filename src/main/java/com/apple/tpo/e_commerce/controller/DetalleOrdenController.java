package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.dto.detalleorden.DetalleOrdenResponse;
import com.apple.tpo.e_commerce.service.DetalleOrdenService;

@RestController
@RequestMapping("/api/detalles-orden")
public class DetalleOrdenController {

    @Autowired
    private DetalleOrdenService detalleOrdenService;

    @GetMapping("/{id}")
    public DetalleOrdenResponse getDetalleById(@PathVariable Long id) {
        return detalleOrdenService.getDetalleById(id);
    }

    @GetMapping("/orden/{ordenId}")
    public List<DetalleOrdenResponse> getDetallesByOrden(@PathVariable Long ordenId) {
        return detalleOrdenService.getDetallesByOrdenId(ordenId);
    }
}
