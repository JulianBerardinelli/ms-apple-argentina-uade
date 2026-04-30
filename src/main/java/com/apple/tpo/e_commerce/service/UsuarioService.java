package com.apple.tpo.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apple.tpo.e_commerce.dto.usuario.UsuarioRequest;
import com.apple.tpo.e_commerce.dto.usuario.UsuarioResponse;
import com.apple.tpo.e_commerce.exception.ResourceNotFoundException;
import com.apple.tpo.e_commerce.mapper.DtoMapper;
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

    public List<UsuarioResponse> getAllUsuarios() {
        return DtoMapper.toUsuarioResponseList(usuarioRepository.findAll());
    }

    public UsuarioResponse getUsuarioById(Long id) {
        return DtoMapper.toUsuarioResponse(findUsuarioById(id));
    }

    public UsuarioResponse createUsuario(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setRole(request.getRole() == null ? Role.ROLE_USER : request.getRole());
        return DtoMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse updateUsuario(Long id, UsuarioRequest request) {
        Usuario usuarioExistente = findUsuarioById(id);
        usuarioExistente.setUsername(request.getUsername());
        usuarioExistente.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        usuarioExistente.setNombre(request.getNombre());
        usuarioExistente.setApellido(request.getApellido());
        if (request.getRole() != null) {
            usuarioExistente.setRole(request.getRole());
        }
        return DtoMapper.toUsuarioResponse(usuarioRepository.save(usuarioExistente));
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }
}
