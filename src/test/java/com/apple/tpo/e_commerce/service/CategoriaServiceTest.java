package com.apple.tpo.e_commerce.service;

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
    void getAllCategorias_returnsRepositoryResult() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Categoria categoria = new Categoria();
        categoria.setNombre("Mac");

        Mockito.when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> categorias = service.getAllCategorias();

        assertEquals(1, categorias.size());
        assertEquals("Mac", categorias.get(0).getNombre());
    }

    @Test
    void getCategoriaById_whenCategoriaExists_returnsCategoria() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        assertEquals(categoria, service.getCategoriaById(1L));
    }

    @Test
    void getCategoriaById_whenCategoriaDoesNotExist_throwsResourceNotFoundException() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Mockito.when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCategoriaById(99L));
    }

    @Test
    void createCategoria_savesCategoria() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Categoria categoria = new Categoria();
        categoria.setNombre("iPhone");

        Mockito.when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria creada = service.createCategoria(categoria);

        assertEquals("iPhone", creada.getNombre());
        Mockito.verify(categoriaRepository).save(categoria);
    }

    @Test
    void updateCategoria_whenCategoriaExists_updatesFields() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Categoria existente = new Categoria();
        existente.setNombre("Viejo");
        existente.setDescripcion("Descripcion vieja");

        Categoria cambios = new Categoria();
        cambios.setNombre("Nuevo");
        cambios.setDescripcion("Descripcion nueva");

        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(categoriaRepository.save(existente)).thenReturn(existente);

        Categoria actualizada = service.updateCategoria(1L, cambios);

        assertEquals("Nuevo", actualizada.getNombre());
        assertEquals("Descripcion nueva", actualizada.getDescripcion());
    }

    @Test
    void updateCategoria_whenCategoriaDoesNotExist_throwsResourceNotFoundException() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Mockito.when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateCategoria(99L, new Categoria()));
    }

    @Test
    void deleteCategoria_whenCategoriaExists_deletesById() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Mockito.when(categoriaRepository.existsById(1L)).thenReturn(true);

        service.deleteCategoria(1L);

        Mockito.verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void deleteCategoria_whenCategoriaDoesNotExist_throwsResourceNotFoundException() {
        CategoriaRepository categoriaRepository = Mockito.mock(CategoriaRepository.class);
        CategoriaService service = new CategoriaService();
        ReflectionTestUtils.setField(service, "categoriaRepository", categoriaRepository);

        Mockito.when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategoria(99L));
    }
}
