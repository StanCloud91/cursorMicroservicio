package com.example.microservice.controller;

import com.example.microservice.dto.RolDTO;
import com.example.microservice.model.Rol;
import com.example.microservice.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Rol", description = "API de gestión de roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @Operation(summary = "Obtener todos los roles")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Rol> findAll() {
        return rolService.findAll();
    }

    @Operation(summary = "Obtener un rol por ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rol> findById(
            @Parameter(description = "ID del rol") @PathVariable Long id) {
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo rol")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody RolDTO rolDTO) {
        try {
            Rol nuevoRol = rolService.createFromDTO(rolDTO);
            return ResponseEntity.ok(nuevoRol);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al crear rol", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un rol existente")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Parameter(description = "ID del rol") @PathVariable Long id,
            @RequestBody Rol rol) {
        try {
            return ResponseEntity.ok(rolService.update(id, rol));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al actualizar rol", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un rol")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del rol") @PathVariable Long id) {
        try {
            rolService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al eliminar rol", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar rol por nombre")
    @GetMapping(value = "/nombre/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNombre(
            @Parameter(description = "Nombre del rol") @PathVariable String nombre) {
        return rolService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Asignar permisos a un rol")
    @PutMapping(value = "/{id}/permisos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> asignarPermisos(
            @Parameter(description = "ID del rol") @PathVariable Long id,
            @RequestBody Set<Long> permisoIds) {
        try {
            return ResponseEntity.ok(rolService.asignarPermisos(id, permisoIds));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al asignar permisos", e.getMessage()));
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