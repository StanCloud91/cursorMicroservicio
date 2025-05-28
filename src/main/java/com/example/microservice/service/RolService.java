package com.example.microservice.service;

import com.example.microservice.dto.RolDTO;
import com.example.microservice.model.Rol;
import com.example.microservice.model.Permiso;
import com.example.microservice.repository.RolRepository;
import com.example.microservice.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PermisoRepository permisoRepository;

    @Transactional(readOnly = true)
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }

    @Transactional
    public Rol createFromDTO(RolDTO dto) {
        if (rolRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un rol con este nombre");
        }
        
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        
        return rolRepository.save(rol);
    }

    @Transactional
    public Rol update(Long id, Rol rol) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        
        Rol rolExistente = rolRepository.findById(id).get();
        if (!rolExistente.getNombre().equals(rol.getNombre()) && 
            rolRepository.existsByNombre(rol.getNombre())) {
            throw new RuntimeException("Ya existe un rol con este nombre");
        }
        
        rol.setId(id);
        return rolRepository.save(rol);
    }

    @Transactional
    public void delete(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Transactional
    public Rol asignarPermisos(Long rolId, Set<Long> permisoIds) {
        Rol rol = rolRepository.findById(rolId)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        // Limpiar permisos existentes
        rol.getPermisos().clear();
        
        // Obtener y validar todos los permisos antes de asignarlos
        Set<Permiso> nuevosPermisos = permisoIds.stream()
            .map(permisoId -> permisoRepository.findById(permisoId)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + permisoId)))
            .collect(java.util.stream.Collectors.toSet());
        
        // Asignar los nuevos permisos
        rol.getPermisos().addAll(nuevosPermisos);
        
        // Guardar y retornar el rol actualizado
        return rolRepository.save(rol);
    }
} 