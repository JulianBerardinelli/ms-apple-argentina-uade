package com.apple.tpo.e_commerce.dto.ordencompra;

import java.time.LocalDateTime;
import java.util.List;

import com.apple.tpo.e_commerce.dto.detalleorden.DetalleOrdenResponse;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;

import lombok.Data;

@Data
public class OrdenCompraResponse {
    private Long id;
    private LocalDateTime fechaOrden;
    private Double total;
    private String estado;
    private UsuarioResponse usuario;
    private Long carritoId;
    private List<DetalleOrdenResponse> detalles;
}
