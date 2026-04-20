package com.apple.tpo.e_commerce.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.DetalleOrden;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {

    // Buscar todos los detalles de una orden
    List<DetalleOrden> findByOrdenId(Long ordenId);

}
