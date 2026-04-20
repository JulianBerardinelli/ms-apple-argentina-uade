package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.respository.ItemCarritoRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<ItemCarrito> getItemsByCarritoId(Long carritoId) {
        return itemCarritoRepository.findByCarritoId(carritoId);
    }

    public ItemCarrito getItemById(Long id) {
        return itemCarritoRepository.findById(id).orElse(null);
    }

    public ItemCarrito createItem(ItemCarrito item) {
        // Calcular precio unitario y subtotal a partir del producto
        Producto producto = productoRepository.findById(item.getProducto().getId()).orElse(null);
        if (producto != null) {
            item.setPrecioUnitario(producto.getPrecio());
            item.setSubtotal(producto.getPrecio() * item.getCantidad());
        }
        return itemCarritoRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemCarritoRepository.deleteById(id);
    }

}
