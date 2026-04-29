package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.auth.AuthResponse;
import com.apple.tpo.e_commerce.dto.auth.LoginRequest;
import com.apple.tpo.e_commerce.dto.auth.RegisterRequest;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import com.apple.tpo.e_commerce.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void register_whenEmailIsNew_savesUserAndReturnsToken() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthService service = new AuthService(usuarioRepository, passwordEncoder, authenticationManager, jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("usuario");
        request.setEmail("usuario@uade.com");
        request.setPassword("password");
        request.setNombre("Usuario");
        request.setApellido("UADE");

        Mockito.when(usuarioRepository.existsByEmail("usuario@uade.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded");
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("token");

        AuthResponse response = service.register(request);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        Mockito.verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario guardado = usuarioCaptor.getValue();

        assertEquals("usuario", guardado.getUsername());
        assertEquals("usuario@uade.com", guardado.getEmail());
        assertEquals("encoded", guardado.getPassword());
        assertEquals("Usuario", guardado.getNombre());
        assertEquals("UADE", guardado.getApellido());
        assertEquals(Role.ROLE_USER, guardado.getRole());
        assertEquals("token", response.getToken());
        assertEquals("usuario@uade.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void register_whenEmailAlreadyExists_throwsRuntimeException() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthService service = new AuthService(usuarioRepository, passwordEncoder, authenticationManager, jwtService);

        RegisterRequest request = new RegisterRequest();
        request.setEmail("usuario@uade.com");

        Mockito.when(usuarioRepository.existsByEmail("usuario@uade.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.register(request));
        Mockito.verify(usuarioRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void login_whenCredentialsAreValid_returnsToken() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthService service = new AuthService(usuarioRepository, passwordEncoder, authenticationManager, jwtService);

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@uade.com");
        request.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setEmail("admin@uade.com");
        usuario.setPassword("encoded");
        usuario.setRole(Role.ROLE_ADMIN);

        Mockito.when(usuarioRepository.findByEmail("admin@uade.com")).thenReturn(Optional.of(usuario));
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("token-admin");

        AuthResponse response = service.login(request);

        assertEquals("token-admin", response.getToken());
        assertEquals("admin@uade.com", response.getEmail());
        assertEquals("ROLE_ADMIN", response.getRole());
        Mockito.verify(authenticationManager).authenticate(Mockito.any());
    }

    @Test
    void login_whenUsuarioDoesNotExist_throwsRuntimeException() {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthService service = new AuthService(usuarioRepository, passwordEncoder, authenticationManager, jwtService);

        LoginRequest request = new LoginRequest();
        request.setEmail("missing@uade.com");
        request.setPassword("password");

        Mockito.when(usuarioRepository.findByEmail("missing@uade.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.login(request));
    }
}
