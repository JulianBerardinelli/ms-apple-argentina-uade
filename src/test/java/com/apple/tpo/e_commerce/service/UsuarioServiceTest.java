package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.usuario.UsuarioRequest;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    @Test
    void getAllUsuarios_returnsResponseWithoutPassword() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        UsuarioService service = createService(repository, Mockito.mock(PasswordEncoder.class));
        Usuario usuario = usuario("usuario@uade.com");
        usuario.setPassword("secret");

        Mockito.when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> usuarios = service.getAllUsuarios();

        assertEquals(1, usuarios.size());
        assertEquals("usuario@uade.com", usuarios.get(0).getEmail());
    }

    @Test
    void getUsuarioById_whenUsuarioExists_returnsDto() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        UsuarioService service = createService(repository, Mockito.mock(PasswordEncoder.class));
        Usuario usuario = usuario("usuario@uade.com");
        usuario.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponse response = service.getUsuarioById(1L);

        assertEquals(1L, response.getId());
        assertEquals("usuario@uade.com", response.getEmail());
    }

    @Test
    void getUsuarioById_whenUsuarioDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        UsuarioService service = createService(repository, Mockito.mock(PasswordEncoder.class));

        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getUsuarioById(99L));
    }

    @Test
    void createUsuario_whenRoleIsNull_setsDefaultRoleAndEncodesPassword() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = createService(repository, passwordEncoder);
        UsuarioRequest request = request();
        request.setRole(null);

        Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded");
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponse creado = service.createUsuario(request);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        Mockito.verify(repository).save(captor.capture());
        assertEquals(Role.ROLE_USER, captor.getValue().getRole());
        assertEquals("encoded", captor.getValue().getPassword());
        assertEquals(Role.ROLE_USER, creado.getRole());
    }

    @Test
    void createUsuario_whenRoleExists_keepsRole() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = createService(repository, passwordEncoder);
        UsuarioRequest request = request();
        request.setRole(Role.ROLE_ADMIN);

        Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded");
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponse creado = service.createUsuario(request);

        assertEquals(Role.ROLE_ADMIN, creado.getRole());
    }

    @Test
    void updateUsuario_whenPasswordAndRoleArePresent_updatesAllFields() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = createService(repository, passwordEncoder);
        Usuario existente = usuario("old@uade.com");
        existente.setPassword("old");
        existente.setRole(Role.ROLE_USER);
        UsuarioRequest cambios = request();
        cambios.setEmail("admin@uade.com");
        cambios.setPassword("new-password");
        cambios.setRole(Role.ROLE_ADMIN);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(passwordEncoder.encode("new-password")).thenReturn("encoded-new");
        Mockito.when(repository.save(existente)).thenReturn(existente);

        UsuarioResponse actualizado = service.updateUsuario(1L, cambios);

        assertEquals("admin@uade.com", actualizado.getEmail());
        assertEquals("encoded-new", existente.getPassword());
        assertEquals(Role.ROLE_ADMIN, actualizado.getRole());
    }

    @Test
    void updateUsuario_whenPasswordIsBlankAndRoleIsNull_keepsPreviousValues() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioService service = createService(repository, passwordEncoder);
        Usuario existente = usuario("old@uade.com");
        existente.setPassword("old");
        existente.setRole(Role.ROLE_USER);
        UsuarioRequest cambios = new UsuarioRequest();
        cambios.setPassword(" ");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(repository.save(existente)).thenReturn(existente);

        UsuarioResponse actualizado = service.updateUsuario(1L, cambios);

        assertEquals("old", existente.getPassword());
        assertEquals(Role.ROLE_USER, actualizado.getRole());
        Mockito.verify(passwordEncoder, Mockito.never()).encode(Mockito.anyString());
    }

    @Test
    void updateUsuario_whenUsuarioDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        UsuarioService service = createService(repository, Mockito.mock(PasswordEncoder.class));

        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateUsuario(99L, new UsuarioRequest()));
    }

    @Test
    void deleteUsuario_whenUsuarioExists_deletesById() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        UsuarioService service = createService(repository, Mockito.mock(PasswordEncoder.class));

        Mockito.when(repository.existsById(1L)).thenReturn(true);

        service.deleteUsuario(1L);

        Mockito.verify(repository).deleteById(1L);
    }

    @Test
    void deleteUsuario_whenUsuarioDoesNotExist_throwsResourceNotFoundException() {
        UsuarioRepository repository = Mockito.mock(UsuarioRepository.class);
        UsuarioService service = createService(repository, Mockito.mock(PasswordEncoder.class));

        Mockito.when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteUsuario(99L));
    }

    private UsuarioService createService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        UsuarioService service = new UsuarioService();
        ReflectionTestUtils.setField(service, "usuarioRepository", repository);
        ReflectionTestUtils.setField(service, "passwordEncoder", passwordEncoder);
        return service;
    }

    private Usuario usuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario");
        usuario.setEmail(email);
        usuario.setNombre("Usuario");
        usuario.setApellido("UADE");
        usuario.setRole(Role.ROLE_USER);
        return usuario;
    }

    private UsuarioRequest request() {
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername("usuario");
        request.setEmail("usuario@uade.com");
        request.setPassword("password");
        request.setNombre("Usuario");
        request.setApellido("UADE");
        return request;
    }
}
