package com.example.microservice.controller;

import com.example.microservice.model.Permiso;
import com.example.microservice.service.PermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/permisos")
@Tag(name = "Permiso", description = "API de gestión de permisos")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @Operation(summary = "Obtener todos los permisos")
    @GetMapping
    public List<Permiso> findAll() {
        return permisoService.findAll();
    }

    @Operation(summary = "Obtener un permiso por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Permiso> findById(
            @Parameter(description = "ID del permiso") @PathVariable Long id) {
        return permisoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo permiso")
    @PostMapping
    public ResponseEntity<Permiso> save(@RequestBody Permiso permiso) {
        try {
            return ResponseEntity.ok(permisoService.save(permiso));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un permiso existente")
    @PutMapping("/{id}")
    public ResponseEntity<Permiso> update(
            @Parameter(description = "ID del permiso") @PathVariable Long id,
            @RequestBody Permiso permiso) {
        try {
            return ResponseEntity.ok(permisoService.update(id, permiso));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un permiso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del permiso") @PathVariable Long id) {
        try {
            permisoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar permisos por módulo")
    @GetMapping("/modulo/{modulo}")
    public List<Permiso> findByModulo(
            @Parameter(description = "Nombre del módulo") @PathVariable String modulo) {
        return permisoService.findByModulo(modulo);
    }

    @Operation(summary = "Buscar permiso por nombre")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Permiso> findByNombre(
            @Parameter(description = "Nombre del permiso") @PathVariable String nombre) {
        return permisoService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 