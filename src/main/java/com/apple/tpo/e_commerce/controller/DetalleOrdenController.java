package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.service.DetalleOrdenService;

@RestController
@RequestMapping("/api/detalles-orden")
public class DetalleOrdenController {

    @Autowired
    private DetalleOrdenService detalleOrdenService;

    @GetMapping("/{id}")
    public DetalleOrden getDetalleById(@PathVariable Long id) {
        return detalleOrdenService.getDetalleById(id);
    }

    // Obtener todos los detalles de una orden
    @GetMapping("/orden/{ordenId}")
    public List<DetalleOrden> getDetallesByOrden(@PathVariable Long ordenId) {
        return detalleOrdenService.getDetallesByOrdenId(ordenId);
    }

}
