package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.detalleorden.DetalleOrdenResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.respository.DetalleOrdenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DetalleOrdenService {

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    public List<DetalleOrdenResponse> getDetallesByOrdenId(Long ordenId) {
        return DtoMapper.toDetalleOrdenResponseList(detalleOrdenRepository.findByOrdenId(ordenId));
    }

    public DetalleOrdenResponse getDetalleById(Long id) {
        return DtoMapper.toDetalleOrdenResponse(findDetalleById(id));
    }

    private DetalleOrden findDetalleById(Long id) {
        return detalleOrdenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de orden no encontrado con id: " + id));
    }
}
