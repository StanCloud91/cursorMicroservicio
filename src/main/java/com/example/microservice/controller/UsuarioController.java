package com.example.microservice.controller;

import com.example.microservice.dto.UsuarioDTO;
import com.example.microservice.model.Usuario;
import com.example.microservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuario", description = "API de gestión de usuarios")
public class UsuarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }

    @Operation(summary = "Obtener un usuario por ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> obtenerUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.createFromDTO(usuarioDTO);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al crear usuario", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un usuario existente")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @RequestBody Usuario usuario) {
        try {
            logger.info("Intentando actualizar usuario con ID: {}", id);
            Usuario usuarioActualizado = usuarioService.update(id, usuario);
            logger.info("Usuario actualizado exitosamente: {}", usuarioActualizado.getEmail());
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al actualizar usuario", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        try {
            logger.info("Intentando eliminar usuario con ID: {}", id);
            usuarioService.delete(id);
            logger.info("Usuario eliminado exitosamente");
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al eliminar usuario", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar usuario por email")
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByEmail(
            @Parameter(description = "Email del usuario") @PathVariable String email) {
        try {
            logger.info("Buscando usuario por email: {}", email);
            return usuarioService.findByEmail(email)
                    .map(usuario -> {
                        logger.info("Usuario encontrado: {}", usuario.getEmail());
                        return ResponseEntity.ok(usuario);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            logger.error("Error al buscar usuario por email: {}", e.getMessage());
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al buscar usuario", e.getMessage()));
        }
    }

    @Operation(summary = "Asignar roles a un usuario")
    @PutMapping(value = "/{id}/roles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> asignarRoles(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @RequestBody Set<Long> rolIds) {
        try {
            return ResponseEntity.ok(usuarioService.asignarRoles(id, rolIds));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar estado de un usuario")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> actualizarEstado(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Parameter(description = "Estado del usuario (activo/inactivo)") 
            @RequestParam boolean activo) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarEstado(id, activo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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