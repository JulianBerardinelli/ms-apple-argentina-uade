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

import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.service.FotoProductoService;

@RestController
@RequestMapping("/api/fotos")
public class FotoProductoController {

    @Autowired
    private FotoProductoService fotoProductoService;

    @GetMapping
    public List<FotoProducto> getAllFotos() {
        return fotoProductoService.getAllFotos();
    }

    @GetMapping("/{id}")
    public FotoProducto getFotoById(@PathVariable Long id) {
        return fotoProductoService.getFotoById(id);
    }

    @GetMapping("/producto/{productoId}")
    public List<FotoProducto> getFotosByProducto(@PathVariable Long productoId) {
        return fotoProductoService.getFotosByProductoId(productoId);
    }

    @PostMapping
    public FotoProducto createFoto(@RequestBody FotoProducto foto) {
        return fotoProductoService.createFoto(foto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFoto(@PathVariable Long id) {
        fotoProductoService.deleteFoto(id);
        return ResponseEntity.ok(ApiResponse.ok(HttpStatus.OK.value(), "Foto eliminada correctamente", null));
    }

}
