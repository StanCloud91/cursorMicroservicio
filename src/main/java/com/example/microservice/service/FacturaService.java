package com.example.microservice.service;

import com.example.microservice.model.Factura;
import com.example.microservice.model.DetalleFactura;
import com.example.microservice.model.Producto;
import com.example.microservice.repository.FacturaRepository;
import com.example.microservice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {
    
    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProductoService productoService;

    @Transactional(readOnly = true)
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }

    @Transactional
    public Factura save(Factura factura) {
        factura.setFechaEmision(LocalDateTime.now());
        
        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleFactura detalle : factura.getDetalles()) {
            Producto producto = productoService.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            // Verificar stock
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            
            // Actualizar stock
            productoService.actualizarStock(producto.getId(), -detalle.getCantidad());
            
            // Calcular subtotal del detalle
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            detalle.setFactura(factura);
            
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        
        factura.setSubtotal(subtotal);
        // Calcular impuestos (ejemplo: 19% IVA)
        factura.setImpuestos(subtotal.multiply(new BigDecimal("0.19")));
        factura.setTotal(factura.getSubtotal().add(factura.getImpuestos()));
        
        return facturaRepository.save(factura);
    }

    @Transactional(readOnly = true)
    public List<Factura> findByClienteId(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public Optional<Factura> findByNumeroFactura(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura);
    }

    @Transactional(readOnly = true)
    public List<Factura> findByFechaEmisionBetween(LocalDateTime inicio, LocalDateTime fin) {
        return facturaRepository.findByFechaEmisionBetween(inicio, fin);
    }
} 