package com.apple.tpo.e_commerce.service;

import com.apple.tpo.e_commerce.dto.auth.AuthResponse;
import com.apple.tpo.e_commerce.dto.auth.LoginRequest;
import com.apple.tpo.e_commerce.dto.auth.RegisterRequest;
import com.apple.tpo.e_commerce.exception.ConflictException;
import com.apple.tpo.e_commerce.exception.InvalidCredentialsException;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import com.apple.tpo.e_commerce.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("El email ya esta registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setRole(Role.ROLE_USER);
        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(usuario.getEmail())
                        .password(usuario.getPassword())
                        .authorities(usuario.getRole().name())
                        .build()
        );
        return new AuthResponse(token, usuario.getEmail(), usuario.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales invalidas"));

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(usuario.getEmail())
                        .password(usuario.getPassword())
                        .authorities(usuario.getRole().name())
                        .build()
        );
        return new AuthResponse(token, usuario.getEmail(), usuario.getRole().name());
    }
}
