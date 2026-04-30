package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsername_whenUserExists_returnsUserDetails() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(usuarioRepository);

        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@uade.com");
        usuario.setPassword("password");
        usuario.setRole(Role.ROLE_USER);

        Mockito.when(usuarioRepository.findByEmail("usuario@uade.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = service.loadUserByUsername("usuario@uade.com");

        assertEquals("usuario@uade.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        List<String> authorities = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        assertTrue(authorities.contains("ROLE_USER"));
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(usuarioRepository);

        Mockito.when(usuarioRepository.findByEmail("missing@uade.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.loadUserByUsername("missing@uade.com"));
    }
}

