package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.respository.OrdenCompraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public List<OrdenCompraResponse> getAllOrdenes() {
        return DtoMapper.toOrdenCompraResponseList(ordenCompraRepository.findAll());
    }

    public OrdenCompraResponse getOrdenById(Long id) {
        return DtoMapper.toOrdenCompraResponse(findOrdenById(id));
    }

    public List<OrdenCompraResponse> getOrdenesByUsuarioId(Long usuarioId) {
        return DtoMapper.toOrdenCompraResponseList(ordenCompraRepository.findByUsuarioId(usuarioId));
    }

    private OrdenCompra findOrdenById(Long id) {
        return ordenCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));
    }
}
