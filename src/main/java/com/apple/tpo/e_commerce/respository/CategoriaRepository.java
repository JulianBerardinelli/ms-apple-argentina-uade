package com.apple.tpo.e_commerce.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // AL EXTENDER JpaRepository, HEREDAMOS TODOS LOS MÉTODOS CRUD

}
