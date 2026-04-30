package com.apple.tpo.e_commerce.dto.fotoproducto;

import lombok.Data;

@Data
public class FotoProductoResponse {
    private Long id;
    private String urlImagen;
    private Integer orden;
    private Long productoId;
}
