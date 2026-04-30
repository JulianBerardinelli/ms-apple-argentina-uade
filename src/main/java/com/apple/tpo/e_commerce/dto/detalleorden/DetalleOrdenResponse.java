package com.apple.tpo.e_commerce.dto.detalleorden;

import com.apple.tpo.e_commerce.dto.producto.ProductoResponse;

import lombok.Data;

@Data
public class DetalleOrdenResponse {
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private Long ordenId;
    private ProductoResponse producto;
}
