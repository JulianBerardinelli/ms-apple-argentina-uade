package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.respository.OrdenCompraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public List<OrdenCompra> getAllOrdenes() {
        return ordenCompraRepository.findAll();
    }

    public OrdenCompra getOrdenById(Long id) {
        return ordenCompraRepository.findById(id).orElse(null);
    }

    public List<OrdenCompra> getOrdenesByUsuarioId(Long usuarioId) {
        return ordenCompraRepository.findByUsuarioId(usuarioId);
    }

}
