package com.apple.tpo.e_commerce.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // AL EXTENDER JpaRepository, HEREDAMOS TODOS LOS MÉTODOS CRUD

}
