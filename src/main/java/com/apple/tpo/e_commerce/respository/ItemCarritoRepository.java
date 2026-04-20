package com.apple.tpo.e_commerce.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    // Buscar todos los items de un carrito
    List<ItemCarrito> findByCarritoId(Long carritoId);

}
