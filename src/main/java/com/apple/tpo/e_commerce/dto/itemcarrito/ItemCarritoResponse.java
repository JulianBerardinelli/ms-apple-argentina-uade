package com.apple.tpo.e_commerce.dto.itemcarrito;

import com.apple.tpo.e_commerce.dto.producto.ProductoResponse;

import lombok.Data;

@Data
public class ItemCarritoResponse {
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private Long carritoId;
    private ProductoResponse producto;
}
