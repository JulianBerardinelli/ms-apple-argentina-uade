package com.apple.tpo.e_commerce.dto.detalleorden;

import lombok.Data;

@Data
public class DetalleOrdenRequest {
    private Long ordenId;
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
