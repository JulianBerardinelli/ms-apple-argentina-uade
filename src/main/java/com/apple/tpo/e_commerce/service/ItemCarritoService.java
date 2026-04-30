package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoRequest;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoResponse;
import com.apple.tpo.e_commerce.exception.BusinessException;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
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

    public List<ItemCarritoResponse> getItemsByCarritoId(Long carritoId) {
        return DtoMapper.toItemCarritoResponseList(itemCarritoRepository.findByCarritoId(carritoId));
    }

    public ItemCarritoResponse getItemById(Long id) {
        return DtoMapper.toItemCarritoResponse(findItemById(id));
    }

    public ItemCarritoResponse createItem(ItemCarritoRequest request) {
        if (request.getCarritoId() == null) {
            throw new BusinessException("Debe enviar carritoId");
        }
        if (request.getProductoId() == null) {
            throw new BusinessException("Debe enviar productoId");
        }
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0");
        }

        Carrito carrito = carritoRepository.findById(request.getCarritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + request.getCarritoId()));

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.getProductoId()));

        ItemCarrito item = new ItemCarrito();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(request.getCantidad());
        item.setPrecioUnitario(producto.getPrecio());
        item.setSubtotal(producto.getPrecio() * request.getCantidad());

        return DtoMapper.toItemCarritoResponse(itemCarritoRepository.save(item));
    }

    public void deleteItem(Long id) {
        if (!itemCarritoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item de carrito no encontrado con id: " + id);
        }
        itemCarritoRepository.deleteById(id);
    }

    private ItemCarrito findItemById(Long id) {
        return itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado con id: " + id));
    }
}
