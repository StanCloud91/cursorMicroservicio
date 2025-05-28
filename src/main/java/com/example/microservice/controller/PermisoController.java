package com.example.microservice.controller;

import com.example.microservice.dto.PermisoDTO;
import com.example.microservice.model.Permiso;
import com.example.microservice.service.PermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Permiso> findAll() {
        return permisoService.findAll();
    }

    @Operation(summary = "Obtener un permiso por ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Permiso> findById(
            @Parameter(description = "ID del permiso") @PathVariable Long id) {
        return permisoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo permiso")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody PermisoDTO permisoDTO) {
        try {
            Permiso nuevoPermiso = permisoService.createFromDTO(permisoDTO);
            return ResponseEntity.ok(nuevoPermiso);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al crear permiso", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un permiso existente")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Parameter(description = "ID del permiso") @PathVariable Long id,
            @RequestBody Permiso permiso) {
        try {
            Permiso permisoActualizado = permisoService.update(id, permiso);
            return ResponseEntity.ok(permisoActualizado);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al actualizar permiso", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un permiso")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del permiso") @PathVariable Long id) {
        try {
            permisoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al eliminar permiso", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar permisos por módulo")
    @GetMapping(value = "/modulo/{modulo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Permiso> findByModulo(
            @Parameter(description = "Nombre del módulo") @PathVariable String modulo) {
        return permisoService.findByModulo(modulo);
    }

    @Operation(summary = "Buscar permiso por nombre")
    @GetMapping(value = "/nombre/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNombre(
            @Parameter(description = "Nombre del permiso") @PathVariable String nombre) {
        return permisoService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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