package com.apple.tpo.e_commerce.dto.fotoproducto;

import lombok.Data;

@Data
public class FotoProductoRequest {
    private String urlImagen;
    private Integer orden;
    private Long productoId;
}
