package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public Usuario createUsuario(Usuario usuario) {
        if (usuario.getRole() == null) {
            usuario.setRole(Role.ROLE_USER);
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setEmail(usuario.getEmail());
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        if (usuario.getRole() != null) {
            usuarioExistente.setRole(usuario.getRole());
        }
        return usuarioRepository.save(usuarioExistente);
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

}
