package com.apple.tpo.e_commerce.dto.producto;

import lombok.Data;

@Data
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Boolean activo;
    private Long usuarioCreadorId;
    private Long categoriaId;
}
