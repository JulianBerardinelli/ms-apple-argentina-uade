package com.apple.tpo.e_commerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.respository.CarritoRepository;
import com.apple.tpo.e_commerce.respository.DetalleOrdenRepository;
import com.apple.tpo.e_commerce.respository.OrdenCompraRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }

    public Carrito getCarritoById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    }

    public List<Carrito> getCarritosByUsuarioId(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public Carrito createCarrito(Carrito carrito) {
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setEstado("ACTIVO");
        return carritoRepository.save(carrito);
    }

    public void deleteCarrito(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carrito no encontrado con id: " + id);
        }
        carritoRepository.deleteById(id);
    }

    // =====================================================
    // CHECKOUT: descuenta stock y genera la OrdenCompra
    // =====================================================
    public OrdenCompra checkout(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + carritoId));

        if (!carrito.getEstado().equals("ACTIVO")) {
            throw new RuntimeException("El carrito no está activo.");
        }

        List<ItemCarrito> items = carrito.getItems();
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        // 1. Verificar stock de todos los productos antes de procesar
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                        + ". Disponible: " + producto.getStock());
            }
        }

        // 2. Crear la OrdenCompra
        OrdenCompra orden = new OrdenCompra();
        orden.setUsuario(carrito.getUsuario());
        orden.setCarrito(carrito);
        orden.setFechaOrden(LocalDateTime.now());
        orden.setEstado("COMPLETADA");
        orden.setTotal(0.0);
        OrdenCompra ordenGuardada = ordenCompraRepository.save(orden);

        // 3. Procesar cada item: descontar stock y crear DetalleOrden
        double total = 0.0;

        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();

            // Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            // Crear detalle de la orden
            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(ordenGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(item.getSubtotal());
            detalleOrdenRepository.save(detalle);

            total += item.getSubtotal();
        }

        // 4. Actualizar el total de la orden
        ordenGuardada.setTotal(total);
        ordenCompraRepository.save(ordenGuardada);

        // 5. Marcar el carrito como CHECKOUT
        carrito.setEstado("CHECKOUT");
        carritoRepository.save(carrito);

        return ordenGuardada;
    }

}
