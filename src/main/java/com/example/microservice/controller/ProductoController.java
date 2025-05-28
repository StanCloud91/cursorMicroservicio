package com.example.microservice.controller;

import com.example.microservice.dto.ProductoDTO;
import com.example.microservice.model.Producto;
import com.example.microservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Producto", description = "API de gestión de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Producto> findAll() {
        return productoService.findAll();
    }

    @GetMapping("/test")
    public String test() {
        return "Hola Mundo";
    }

    @Operation(summary = "Obtener un producto por ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Producto> findById(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo producto")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ProductoDTO productoDTO) {
        try {
            Producto nuevoProducto = productoService.createFromDTO(productoDTO);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al crear producto", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un producto existente")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.update(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al actualizar producto", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        try {
            productoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al eliminar producto", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar producto por SKU")
    @GetMapping(value = "/sku/{codigoSku}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByCodigoSku(
            @Parameter(description = "Código SKU del producto") @PathVariable String codigoSku) {
        return productoService.findByCodigoSku(codigoSku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar productos por categoría")
    @GetMapping(value = "/categoria/{categoria}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Producto> findByCategoria(
            @Parameter(description = "Categoría del producto") @PathVariable String categoria) {
        return productoService.findByCategoria(categoria);
    }

    @Operation(summary = "Actualizar stock de un producto")
    @PutMapping(value = "/{id}/stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> actualizarStock(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Cantidad a modificar (positivo para agregar, negativo para restar)")
            @RequestParam Integer cantidad) {
        try {
            Producto producto = productoService.actualizarStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al actualizar stock", e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private final String error;
        private final String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
} 