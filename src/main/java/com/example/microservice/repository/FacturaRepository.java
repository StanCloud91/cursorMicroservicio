package com.example.microservice.repository;

import com.example.microservice.model.Factura;
import com.example.microservice.model.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByNumeroFactura(String numeroFactura);
    List<Factura> findByClienteId(Long clienteId);
    List<Factura> findByEstado(EstadoFactura estado);
    List<Factura> findByFechaEmisionBetween(LocalDateTime inicio, LocalDateTime fin);
} 