package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.respository.DetalleOrdenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DetalleOrdenService {

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    public List<DetalleOrden> getDetallesByOrdenId(Long ordenId) {
        return detalleOrdenRepository.findByOrdenId(ordenId);
    }

    public DetalleOrden getDetalleById(Long id) {
        return detalleOrdenRepository.findById(id).orElse(null);
    }

}
