package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    @Test
    void getAllUsuarios_returnsRepositoryResult() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@uade.com");

        Mockito.when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> usuarios = service.getAllUsuarios();

        assertEquals(1, usuarios.size());
        assertEquals("usuario@uade.com", usuarios.get(0).getEmail());
    }

    @Test
    void getUsuarioById_whenUsuarioExists_returnsUsuario() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertEquals(usuario, service.getUsuarioById(1L));
    }

    @Test
    void getUsuarioById_whenUsuarioDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getUsuarioById(99L));
    }

    @Test
    void createUsuario_whenRoleIsNull_setsDefaultRoleAndEncodesPassword() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Usuario usuario = new Usuario();
        usuario.setPassword("password");

        Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded");
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario creado = service.createUsuario(usuario);

        assertEquals(Role.ROLE_USER, creado.getRole());
        assertEquals("encoded", creado.getPassword());
    }

    @Test
    void createUsuario_whenRoleExists_keepsRole() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Usuario usuario = new Usuario();
        usuario.setRole(Role.ROLE_ADMIN);
        usuario.setPassword("password");

        Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded");
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario creado = service.createUsuario(usuario);

        assertEquals(Role.ROLE_ADMIN, creado.getRole());
    }

    @Test
    void updateUsuario_whenPasswordAndRoleArePresent_updatesAllFields() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Usuario existente = new Usuario();
        existente.setPassword("old");
        existente.setRole(Role.ROLE_USER);

        Usuario cambios = new Usuario();
        cambios.setUsername("admin");
        cambios.setEmail("admin@uade.com");
        cambios.setPassword("new-password");
        cambios.setNombre("Admin");
        cambios.setApellido("Sistema");
        cambios.setRole(Role.ROLE_ADMIN);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(passwordEncoder.encode("new-password")).thenReturn("encoded-new");
        Mockito.when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario actualizado = service.updateUsuario(1L, cambios);

        assertEquals("admin", actualizado.getUsername());
        assertEquals("admin@uade.com", actualizado.getEmail());
        assertEquals("encoded-new", actualizado.getPassword());
        assertEquals("Admin", actualizado.getNombre());
        assertEquals("Sistema", actualizado.getApellido());
        assertEquals(Role.ROLE_ADMIN, actualizado.getRole());
    }

    @Test
    void updateUsuario_whenPasswordIsBlankAndRoleIsNull_keepsPreviousValues() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Usuario existente = new Usuario();
        existente.setPassword("old");
        existente.setRole(Role.ROLE_USER);

        Usuario cambios = new Usuario();
        cambios.setPassword(" ");

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario actualizado = service.updateUsuario(1L, cambios);

        assertEquals("old", actualizado.getPassword());
        assertEquals(Role.ROLE_USER, actualizado.getRole());
        Mockito.verify(passwordEncoder, Mockito.never()).encode(Mockito.anyString());
    }

    @Test
    void updateUsuario_whenUsuarioDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateUsuario(99L, new Usuario()));
    }

    @Test
    void deleteUsuario_whenUsuarioExists_deletesById() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Mockito.when(usuarioRepository.existsById(1L)).thenReturn(true);

        service.deleteUsuario(1L);

        Mockito.verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void deleteUsuario_whenUsuarioDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);

        Mockito.when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteUsuario(99L));
    }
}
