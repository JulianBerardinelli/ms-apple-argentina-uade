package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.categoria.CategoriaRequest;
import com.apple.tpo.e_commerce.dto.categoria.CategoriaResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.respository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> getAllCategorias() {
        return DtoMapper.toCategoriaResponseList(categoriaRepository.findAll());
    }

    public CategoriaResponse getCategoriaById(Long id) {
        return DtoMapper.toCategoriaResponse(findCategoriaById(id));
    }

    public CategoriaResponse createCategoria(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return DtoMapper.toCategoriaResponse(categoriaRepository.save(categoria));
    }

    public CategoriaResponse updateCategoria(Long id, CategoriaRequest request) {
        Categoria categoriaExistente = findCategoriaById(id);
        categoriaExistente.setNombre(request.getNombre());
        categoriaExistente.setDescripcion(request.getDescripcion());
        return DtoMapper.toCategoriaResponse(categoriaRepository.save(categoriaExistente));
    }

    public void deleteCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private Categoria findCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }
}
