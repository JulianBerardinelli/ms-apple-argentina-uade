package com.apple.tpo.e_commerce.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // AL EXTENDER JpaRepository, HEREDAMOS TODOS LOS MÉTODOS CRUD

}
