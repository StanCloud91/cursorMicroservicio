package com.example.microservice.repository;

import com.example.microservice.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByNombre(String nombre);
    List<Permiso> findByModulo(String modulo);
    boolean existsByNombre(String nombre);
} 