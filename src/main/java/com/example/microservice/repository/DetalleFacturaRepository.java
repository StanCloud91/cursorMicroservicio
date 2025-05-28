package com.example.microservice.repository;

import com.example.microservice.model.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
    List<DetalleFactura> findByFacturaId(Long facturaId);
    List<DetalleFactura> findByProductoId(Long productoId);
} 