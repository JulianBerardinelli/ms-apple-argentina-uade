package com.apple.tpo.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.tpo.e_commerce.dto.common.ApiResponse;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoRequest;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoResponse;
import com.apple.tpo.e_commerce.service.ItemCarritoService;

@RestController
@RequestMapping("/api/items-carrito")
public class ItemCarritoController {

    @Autowired
    private ItemCarritoService itemCarritoService;

    @GetMapping("/{id}")
    public ItemCarritoResponse getItemById(@PathVariable Long id) {
        return itemCarritoService.getItemById(id);
    }

    @GetMapping("/carrito/{carritoId}")
    public List<ItemCarritoResponse> getItemsByCarrito(@PathVariable Long carritoId) {
        return itemCarritoService.getItemsByCarritoId(carritoId);
    }

    @PostMapping
    public ItemCarritoResponse createItem(@RequestBody ItemCarritoRequest request) {
        return itemCarritoService.createItem(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemCarritoService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.ok(HttpStatus.OK.value(), "Item de carrito eliminado correctamente", null));
    }
}
