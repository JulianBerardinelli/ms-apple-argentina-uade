package com.apple.tpo.e_commerce.dto.producto;

import java.util.List;

import com.apple.tpo.e_commerce.dto.categoria.CategoriaResponse;
import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoResponse;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;

import lombok.Data;

@Data
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Boolean activo;
    private UsuarioResponse usuarioCreador;
    private CategoriaResponse categoria;
    private List<FotoProductoResponse> fotos;
}
