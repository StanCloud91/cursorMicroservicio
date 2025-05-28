package com.example.microservice.service;

import com.example.microservice.dto.ProductoDTO;
import com.example.microservice.model.Producto;
import com.example.microservice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto createFromDTO(ProductoDTO dto) {
        if (dto.getCodigoSku() != null && productoRepository.existsByCodigoSku(dto.getCodigoSku())) {
            throw new RuntimeException("Ya existe un producto con este código SKU");
        }
        
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCodigoSku(dto.getCodigoSku());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());
        producto.setUnidadMedida(dto.getUnidadMedida());
        
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto update(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        
        Producto productoExistente = productoRepository.findById(id).get();
        if (producto.getCodigoSku() != null && 
            !producto.getCodigoSku().equals(productoExistente.getCodigoSku()) && 
            productoRepository.existsByCodigoSku(producto.getCodigoSku())) {
            throw new RuntimeException("Ya existe un producto con este código SKU");
        }
        
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @Transactional
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> findByCodigoSku(String codigoSku) {
        return productoRepository.findByCodigoSku(codigoSku);
    }

    @Transactional(readOnly = true)
    public List<Producto> findByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Transactional
    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }
} 