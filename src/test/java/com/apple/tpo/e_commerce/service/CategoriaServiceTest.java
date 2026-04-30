package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.categoria.CategoriaRequest;
import com.apple.tpo.e_commerce.dto.categoria.CategoriaResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Categoria;
import com.apple.tpo.e_commerce.respository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaServiceTest {

    @Test
    void getAllCategorias_returnsDtoResult() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);
        Categoria categoria = new Categoria();
        categoria.setNombre("Mac");

        Mockito.when(repository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponse> categorias = service.getAllCategorias();

        assertEquals(1, categorias.size());
        assertEquals("Mac", categorias.get(0).getNombre());
    }

    @Test
    void getCategoriaById_whenCategoriaExists_returnsDto() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        assertEquals(1L, service.getCategoriaById(1L).getId());
    }

    @Test
    void getCategoriaById_whenCategoriaDoesNotExist_throwsResourceNotFoundException() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);

        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCategoriaById(99L));
    }

    @Test
    void createCategoria_savesCategoriaFromRequest() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);
        CategoriaRequest request = request("iPhone", "Telefonos");

        Mockito.when(repository.save(Mockito.any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponse creada = service.createCategoria(request);

        assertEquals("iPhone", creada.getNombre());
        Mockito.verify(repository).save(Mockito.any(Categoria.class));
    }

    @Test
    void updateCategoria_whenCategoriaExists_updatesFields() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);
        Categoria existente = new Categoria();
        CategoriaRequest cambios = request("Nuevo", "Descripcion nueva");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(repository.save(existente)).thenReturn(existente);

        CategoriaResponse actualizada = service.updateCategoria(1L, cambios);

        assertEquals("Nuevo", actualizada.getNombre());
        assertEquals("Descripcion nueva", actualizada.getDescripcion());
    }

    @Test
    void updateCategoria_whenCategoriaDoesNotExist_throwsResourceNotFoundException() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);

        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateCategoria(99L, new CategoriaRequest()));
    }

    @Test
    void deleteCategoria_whenCategoriaExists_deletesById() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);

        Mockito.when(repository.existsById(1L)).thenReturn(true);

        service.deleteCategoria(1L);

        Mockito.verify(repository).deleteById(1L);
    }

    @Test
    void deleteCategoria_whenCategoriaDoesNotExist_throwsResourceNotFoundException() {
        CategoriaRepository repository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = createService(repository);

        Mockito.when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategoria(99L));
    }

    private CategoriaService createService(CategoriaRepository repository) {
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", repository);
        return service;
    }

    private CategoriaRequest request(String nombre, String descripcion) {
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre(nombre);
        request.setDescripcion(descripcion);
        return request;
    }
}
