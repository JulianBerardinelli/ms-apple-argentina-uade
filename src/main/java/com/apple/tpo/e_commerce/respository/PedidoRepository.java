package com.apple.tpo.e_commerce.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // AL EXTENDER JpaRepository, HEREDAMOS TODOS LOS MÉTODOS CRUD

}
