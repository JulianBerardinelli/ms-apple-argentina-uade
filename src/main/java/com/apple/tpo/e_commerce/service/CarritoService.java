package com.apple.tpo.e_commerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.carrito.CarritoRequest;
import com.apple.tpo.e_commerce.dto.carrito.CarritoResponse;
import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.exception.BusinessException;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.CarritoRepository;
import com.apple.tpo.e_commerce.respository.DetalleOrdenRepository;
import com.apple.tpo.e_commerce.respository.OrdenCompraRepository;
import com.apple.tpo.e_commerce.respository.ProductoRepository;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;

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

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<CarritoResponse> getAllCarritos() {
        return DtoMapper.toCarritoResponseList(carritoRepository.findAll());
    }

    public CarritoResponse getCarritoById(Long id) {
        return DtoMapper.toCarritoResponse(findCarritoById(id));
    }

    public List<CarritoResponse> getCarritosByUsuarioId(Long usuarioId) {
        return DtoMapper.toCarritoResponseList(carritoRepository.findByUsuarioId(usuarioId));
    }

    public CarritoResponse createCarrito(CarritoRequest request) {
        Carrito carrito = new Carrito();
        carrito.setUsuario(findUsuarioById(request.getUsuarioId()));
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setEstado("ACTIVO");
        return DtoMapper.toCarritoResponse(carritoRepository.save(carrito));
    }

    public void deleteCarrito(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carrito no encontrado con id: " + id);
        }
        carritoRepository.deleteById(id);
    }

    public OrdenCompraResponse checkout(Long carritoId) {
        Carrito carrito = findCarritoById(carritoId);

        String estadoCarrito = carrito.getEstado() == null ? "" : carrito.getEstado().trim();
        if (!"ACTIVO".equalsIgnoreCase(estadoCarrito)) {
            throw new BusinessException("El carrito no esta activo. Estado actual: " + carrito.getEstado());
        }

        List<ItemCarrito> items = carrito.getItems();
        if (items == null || items.isEmpty()) {
            throw new BusinessException("El carrito esta vacio.");
        }

        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new BusinessException("Stock insuficiente para: " + producto.getNombre()
                        + ". Disponible: " + producto.getStock());
            }
        }

        OrdenCompra orden = new OrdenCompra();
        orden.setUsuario(carrito.getUsuario());
        orden.setCarrito(carrito);
        orden.setFechaOrden(LocalDateTime.now());
        orden.setEstado("COMPLETADA");
        orden.setTotal(0.0);
        OrdenCompra ordenGuardada = ordenCompraRepository.save(orden);

        double total = 0.0;
        List<DetalleOrden> detalles = new ArrayList<>();

        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(ordenGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(item.getSubtotal());
            detalles.add(detalleOrdenRepository.save(detalle));

            total += item.getSubtotal();
        }

        ordenGuardada.setTotal(total);
        ordenGuardada.setDetalles(detalles);
        ordenCompraRepository.save(ordenGuardada);

        carrito.setEstado("CHECKOUT");
        carritoRepository.save(carrito);

        return DtoMapper.toOrdenCompraResponse(ordenGuardada);
    }

    private Carrito findCarritoById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    }

    private Usuario findUsuarioById(Long id) {
        if (id == null) {
            return null;
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }
}
