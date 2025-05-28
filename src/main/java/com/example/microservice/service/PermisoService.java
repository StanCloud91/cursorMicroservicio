package com.example.microservice.service;

import com.example.microservice.model.Permiso;
import com.example.microservice.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PermisoService {
    
    @Autowired
    private PermisoRepository permisoRepository;

    @Transactional(readOnly = true)
    public List<Permiso> findAll() {
        return permisoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Permiso> findById(Long id) {
        return permisoRepository.findById(id);
    }

    @Transactional
    public Permiso save(Permiso permiso) {
        if (permisoRepository.existsByNombre(permiso.getNombre())) {
            throw new RuntimeException("Ya existe un permiso con este nombre");
        }
        return permisoRepository.save(permiso);
    }

    @Transactional
    public Permiso update(Long id, Permiso permiso) {
        if (!permisoRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado");
        }
        
        Permiso permisoExistente = permisoRepository.findById(id).get();
        if (!permisoExistente.getNombre().equals(permiso.getNombre()) && 
            permisoRepository.existsByNombre(permiso.getNombre())) {
            throw new RuntimeException("Ya existe un permiso con este nombre");
        }
        
        permiso.setId(id);
        return permisoRepository.save(permiso);
    }

    @Transactional
    public void delete(Long id) {
        if (!permisoRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado");
        }
        permisoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Permiso> findByModulo(String modulo) {
        return permisoRepository.findByModulo(modulo);
    }

    @Transactional(readOnly = true)
    public Optional<Permiso> findByNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }
} 