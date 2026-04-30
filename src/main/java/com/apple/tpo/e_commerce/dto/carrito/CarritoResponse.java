package com.apple.tpo.e_commerce.dto.carrito;

import java.time.LocalDateTime;
import java.util.List;

import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoResponse;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;

import lombok.Data;

@Data
public class CarritoResponse {
    private Long id;
    private LocalDateTime fechaCreacion;
    private String estado;
    private UsuarioResponse usuario;
    private List<ItemCarritoResponse> items;
}
