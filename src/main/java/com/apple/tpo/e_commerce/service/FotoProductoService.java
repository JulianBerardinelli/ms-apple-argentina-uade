package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.respository.FotoProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FotoProductoService {

    @Autowired
    private FotoProductoRepository fotoProductoRepository;

    public List<FotoProducto> getAllFotos() {
        return fotoProductoRepository.findAll();
    }

    public List<FotoProducto> getFotosByProductoId(Long productoId) {
        return fotoProductoRepository.findByProductoId(productoId);
    }

    public FotoProducto getFotoById(Long id) {
        return fotoProductoRepository.findById(id).orElse(null);
    }

    public FotoProducto createFoto(FotoProducto foto) {
        return fotoProductoRepository.save(foto);
    }

    public void deleteFoto(Long id) {
        fotoProductoRepository.deleteById(id);
    }

}
