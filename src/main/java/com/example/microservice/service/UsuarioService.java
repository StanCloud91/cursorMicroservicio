package com.example.microservice.service;

import com.example.microservice.dto.UsuarioDTO;
import com.example.microservice.model.Usuario;
import com.example.microservice.model.Rol;
import com.example.microservice.repository.UsuarioRepository;
import com.example.microservice.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario createFromDTO(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con este email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        Usuario usuarioExistente = usuarioRepository.findById(id).get();
        if (!usuarioExistente.getEmail().equals(usuario.getEmail()) && 
            usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con este email");
        }
        
        usuario.setId(id);
        usuario.setFechaCreacion(usuarioExistente.getFechaCreacion());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public Usuario asignarRoles(Long usuarioId, Set<Long> rolIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.getRoles().clear();
        rolIds.forEach(rolId -> {
            Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolId));
            usuario.getRoles().add(rol);
        });
        
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void registrarAcceso(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
} 