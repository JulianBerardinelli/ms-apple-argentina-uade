package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.respository.CarritoRepository;
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

    @Autowired
    private CarritoRepository carritoRepository;

    public List<ItemCarrito> getItemsByCarritoId(Long carritoId) {
        return itemCarritoRepository.findByCarritoId(carritoId);
    }

    public ItemCarrito getItemById(Long id) {
        return itemCarritoRepository.findById(id).orElse(null);
    }

    public ItemCarrito createItem(ItemCarrito item) {
        if (item.getCarrito() == null || item.getCarrito().getId() == null) {
            throw new RuntimeException("Debe enviar carrito.id");
        }
        if (item.getProducto() == null || item.getProducto().getId() == null) {
            throw new RuntimeException("Debe enviar producto.id");
        }
        if (item.getCantidad() == null || item.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        Carrito carrito = carritoRepository.findById(item.getCarrito().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + item.getCarrito().getId()));

        Producto producto = productoRepository.findById(item.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + item.getProducto().getId()));

        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setPrecioUnitario(producto.getPrecio());
        item.setSubtotal(producto.getPrecio() * item.getCantidad());

        return itemCarritoRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemCarritoRepository.deleteById(id);
    }

}
