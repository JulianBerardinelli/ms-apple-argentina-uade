package com.apple.tpo.e_commerce.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.FotoProducto;

public interface FotoProductoRepository extends JpaRepository<FotoProducto, Long> {

    // Buscar todas las fotos de un producto específico
    List<FotoProducto> findByProductoId(Long productoId);

}
