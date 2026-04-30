package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoRequest;
import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.respository.FotoProductoRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FotoProductoService {

    @Autowired
    private FotoProductoRepository fotoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<FotoProductoResponse> getAllFotos() {
        return DtoMapper.toFotoProductoResponseList(fotoProductoRepository.findAll());
    }

    public List<FotoProductoResponse> getFotosByProductoId(Long productoId) {
        return DtoMapper.toFotoProductoResponseList(fotoProductoRepository.findByProductoId(productoId));
    }

    public FotoProductoResponse getFotoById(Long id) {
        return DtoMapper.toFotoProductoResponse(findFotoById(id));
    }

    public FotoProductoResponse createFoto(FotoProductoRequest request) {
        FotoProducto foto = new FotoProducto();
        foto.setUrlImagen(request.getUrlImagen());
        foto.setOrden(request.getOrden());
        foto.setProducto(findProductoById(request.getProductoId()));
        return DtoMapper.toFotoProductoResponse(fotoProductoRepository.save(foto));
    }

    public void deleteFoto(Long id) {
        if (!fotoProductoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Foto no encontrada con id: " + id);
        }
        fotoProductoRepository.deleteById(id);
    }

    private FotoProducto findFotoById(Long id) {
        return fotoProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto no encontrada con id: " + id));
    }

    private Producto findProductoById(Long id) {
        if (id == null) {
            return null;
        }
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }
}
