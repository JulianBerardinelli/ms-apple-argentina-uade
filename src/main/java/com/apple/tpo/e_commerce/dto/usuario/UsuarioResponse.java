package com.apple.tpo.e_commerce.dto.usuario;

import com.apple.tpo.e_commerce.model.Role;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Role role;
}
