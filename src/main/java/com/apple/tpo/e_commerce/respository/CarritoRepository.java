package com.apple.tpo.e_commerce.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Buscar todos los carritos de un usuario
    List<Carrito> findByUsuarioId(Long usuarioId);

}
