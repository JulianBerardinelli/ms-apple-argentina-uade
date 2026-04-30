package com.apple.tpo.e_commerce.dto.ordencompra;

import lombok.Data;

@Data
public class OrdenCompraRequest {
    private Long usuarioId;
    private Long carritoId;
    private Double total;
    private String estado;
}
