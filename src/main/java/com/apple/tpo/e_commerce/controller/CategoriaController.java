package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.dto.categoria.CategoriaRequest;
import com.apple.tpo.e_commerce.dto.categoria.CategoriaResponse;
import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponse> getAllCategorias() {
        return categoriaService.getAllCategorias();
    }

    @GetMapping("/{id}")
    public CategoriaResponse getCategoriaById(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id);
    }

    @PostMapping
    public CategoriaResponse createCategoria(@RequestBody CategoriaRequest request) {
        return categoriaService.createCategoria(request);
    }

    @PutMapping("/{id}")
    public CategoriaResponse updateCategoria(@PathVariable Long id, @RequestBody CategoriaRequest request) {
        return categoriaService.updateCategoria(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.ok(ApiResponse.ok(HttpStatus.OK.value(), "Categoria eliminada correctamente", null));
    }
}
