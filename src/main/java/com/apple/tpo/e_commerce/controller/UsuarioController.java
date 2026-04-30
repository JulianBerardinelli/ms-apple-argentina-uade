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

import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioRequest;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;
import com.apple.tpo.e_commerce.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponse getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping
    public UsuarioResponse createUsuario(@RequestBody UsuarioRequest request) {
        return usuarioService.createUsuario(request);
    }

    @PutMapping("/{id}")
    public UsuarioResponse updateUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        return usuarioService.updateUsuario(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok(HttpStatus.OK.value(), "Usuario eliminado correctamente", null));
    }
}
