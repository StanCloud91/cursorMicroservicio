package com.example.microservice.repository;

import com.example.microservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigoSku(String codigoSku);
    List<Producto> findByCategoria(String categoria);
    boolean existsByCodigoSku(String codigoSku);
} 