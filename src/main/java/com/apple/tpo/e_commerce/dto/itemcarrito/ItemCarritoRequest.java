package com.apple.tpo.e_commerce.dto.itemcarrito;

import lombok.Data;

@Data
public class ItemCarritoRequest {
    private Long carritoId;
    private Long productoId;
    private Integer cantidad;
}
