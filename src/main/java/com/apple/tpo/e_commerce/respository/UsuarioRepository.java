package com.apple.tpo.e_commerce.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.tpo.e_commerce.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
