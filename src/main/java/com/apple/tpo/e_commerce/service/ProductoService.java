package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.producto.ProductoRequest;
import com.apple.tpo.e_commerce.dto.producto.ProductoResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.CategoriaRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ProductoResponse> getAllProductos() {
        return DtoMapper.toProductoResponseList(productoRepository.findAll());
    }

    public ProductoResponse getProductoById(Long id) {
        return DtoMapper.toProductoResponse(findProductoById(id));
    }

    public ProductoResponse createProducto(ProductoRequest request) {
        Producto producto = new Producto();
        applyRequest(producto, request);
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        return DtoMapper.toProductoResponse(productoRepository.save(producto));
    }

    public ProductoResponse updateProducto(Long id, ProductoRequest request) {
        Producto productoExistente = findProductoById(id);
        applyRequest(productoExistente, request);
        return DtoMapper.toProductoResponse(productoRepository.save(productoExistente));
    }

    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    private Producto findProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private void applyRequest(Producto producto, ProductoRequest request) {
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setActivo(request.getActivo());
        producto.setCategoria(findCategoriaById(request.getCategoriaId()));
        producto.setUsuarioCreador(findUsuarioById(request.getUsuarioCreadorId()));
    }

    private Categoria findCategoriaById(Long id) {
        if (id == null) {
            return null;
        }
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }

    private Usuario findUsuarioById(Long id) {
        if (id == null) {
            return null;
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }
}
