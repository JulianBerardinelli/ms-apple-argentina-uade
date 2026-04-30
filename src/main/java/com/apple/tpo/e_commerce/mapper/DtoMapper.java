package com.apple.tpo.e_commerce.mapper;

import java.util.List;
import java.util.function.Function;

import com.apple.tpo.e_commerce.dto.carrito.CarritoResponse;
import com.apple.tpo.e_commerce.dto.categoria.CategoriaResponse;
import com.apple.tpo.e_commerce.dto.detalleorden.DetalleOrdenResponse;
import com.apple.tpo.e_commerce.dto.fotoproducto.FotoProductoResponse;
import com.apple.tpo.e_commerce.dto.itemcarrito.ItemCarritoResponse;
import com.apple.tpo.e_commerce.dto.ordencompra.OrdenCompraResponse;
import com.apple.tpo.e_commerce.dto.producto.ProductoResponse;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;
import com.apple.tpo.e_commerce.model.Carrito;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.model.DetalleOrden;
import com.apple.tpo.e_commerce.model.FotoProducto;
import com.apple.tpo.e_commerce.model.ItemCarrito;
import com.apple.tpo.e_commerce.model.OrdenCompra;
import com.apple.tpo.e_commerce.model.Producto;
import com.apple.tpo.e_commerce.model.Usuario;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setEmail(usuario.getEmail());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setRole(usuario.getRole());
        return response;
    }

    public static List<UsuarioResponse> toUsuarioResponseList(List<Usuario> usuarios) {
        return toList(usuarios, DtoMapper::toUsuarioResponse);
    }

    public static CategoriaResponse toCategoriaResponse(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        CategoriaResponse response = new CategoriaResponse();
        response.setId(categoria.getId());
        response.setNombre(categoria.getNombre());
        response.setDescripcion(categoria.getDescripcion());
        return response;
    }

    public static List<CategoriaResponse> toCategoriaResponseList(List<Categoria> categorias) {
        return toList(categorias, DtoMapper::toCategoriaResponse);
    }

    public static ProductoResponse toProductoResponse(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setStock(producto.getStock());
        response.setActivo(producto.getActivo());
        response.setUsuarioCreador(toUsuarioResponse(producto.getUsuarioCreador()));
        response.setCategoria(toCategoriaResponse(producto.getCategoria()));
        response.setFotos(toFotoProductoResponseList(producto.getFotos()));
        return response;
    }

    public static List<ProductoResponse> toProductoResponseList(List<Producto> productos) {
        return toList(productos, DtoMapper::toProductoResponse);
    }

    public static FotoProductoResponse toFotoProductoResponse(FotoProducto foto) {
        if (foto == null) {
            return null;
        }
        FotoProductoResponse response = new FotoProductoResponse();
        response.setId(foto.getId());
        response.setUrlImagen(foto.getUrlImagen());
        response.setOrden(foto.getOrden());
        response.setProductoId(foto.getProducto() == null ? null : foto.getProducto().getId());
        return response;
    }

    public static List<FotoProductoResponse> toFotoProductoResponseList(List<FotoProducto> fotos) {
        return toList(fotos, DtoMapper::toFotoProductoResponse);
    }

    public static CarritoResponse toCarritoResponse(Carrito carrito) {
        if (carrito == null) {
            return null;
        }
        CarritoResponse response = new CarritoResponse();
        response.setId(carrito.getId());
        response.setFechaCreacion(carrito.getFechaCreacion());
        response.setEstado(carrito.getEstado());
        response.setUsuario(toUsuarioResponse(carrito.getUsuario()));
        response.setItems(toItemCarritoResponseList(carrito.getItems()));
        return response;
    }

    public static List<CarritoResponse> toCarritoResponseList(List<Carrito> carritos) {
        return toList(carritos, DtoMapper::toCarritoResponse);
    }

    public static ItemCarritoResponse toItemCarritoResponse(ItemCarrito item) {
        if (item == null) {
            return null;
        }
        ItemCarritoResponse response = new ItemCarritoResponse();
        response.setId(item.getId());
        response.setCantidad(item.getCantidad());
        response.setPrecioUnitario(item.getPrecioUnitario());
        response.setSubtotal(item.getSubtotal());
        response.setCarritoId(item.getCarrito() == null ? null : item.getCarrito().getId());
        response.setProducto(toProductoResponse(item.getProducto()));
        return response;
    }

    public static List<ItemCarritoResponse> toItemCarritoResponseList(List<ItemCarrito> items) {
        return toList(items, DtoMapper::toItemCarritoResponse);
    }

    public static OrdenCompraResponse toOrdenCompraResponse(OrdenCompra orden) {
        if (orden == null) {
            return null;
        }
        OrdenCompraResponse response = new OrdenCompraResponse();
        response.setId(orden.getId());
        response.setFechaOrden(orden.getFechaOrden());
        response.setTotal(orden.getTotal());
        response.setEstado(orden.getEstado());
        response.setUsuario(toUsuarioResponse(orden.getUsuario()));
        response.setCarritoId(orden.getCarrito() == null ? null : orden.getCarrito().getId());
        response.setDetalles(toDetalleOrdenResponseList(orden.getDetalles()));
        return response;
    }

    public static List<OrdenCompraResponse> toOrdenCompraResponseList(List<OrdenCompra> ordenes) {
        return toList(ordenes, DtoMapper::toOrdenCompraResponse);
    }

    public static DetalleOrdenResponse toDetalleOrdenResponse(DetalleOrden detalle) {
        if (detalle == null) {
            return null;
        }
        DetalleOrdenResponse response = new DetalleOrdenResponse();
        response.setId(detalle.getId());
        response.setCantidad(detalle.getCantidad());
        response.setPrecioUnitario(detalle.getPrecioUnitario());
        response.setSubtotal(detalle.getSubtotal());
        response.setOrdenId(detalle.getOrden() == null ? null : detalle.getOrden().getId());
        response.setProducto(toProductoResponse(detalle.getProducto()));
        return response;
    }

    public static List<DetalleOrdenResponse> toDetalleOrdenResponseList(List<DetalleOrden> detalles) {
        return toList(detalles, DtoMapper::toDetalleOrdenResponse);
    }

    private static <T, R> List<R> toList(List<T> source, Function<T, R> mapper) {
        if (source == null) {
            return List.of();
        }
        return source.stream().map(mapper).toList();
    }
}
