package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.model.Pedido;
import com.apple.tpo.e_commerce.respository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public Pedido createPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido updatePedido(Long id, Pedido pedido) {
        Pedido pedidoExistente = pedidoRepository.findById(id).orElse(null);
        if (pedidoExistente == null) return null;
        pedidoExistente.setUsuarioId(pedido.getUsuarioId());
        pedidoExistente.setProductoId(pedido.getProductoId());
        pedidoExistente.setCantidad(pedido.getCantidad());
        pedidoExistente.setTotal(pedido.getTotal());
        pedidoExistente.setEstado(pedido.getEstado());
        return pedidoRepository.save(pedidoExistente);
    }

    public void deletePedido(Long id) {
        pedidoRepository.deleteById(id);
    }

}
