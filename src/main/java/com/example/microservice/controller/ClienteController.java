package com.example.microservice.controller;

import com.example.microservice.dto.ClienteDTO;
import com.example.microservice.model.Cliente;
import com.example.microservice.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Cliente", description = "API de gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cliente> findAll() {
        return clienteService.findAll();
    }

    @Operation(summary = "Obtener un cliente por ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> findById(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo cliente")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ClienteDTO clienteDTO) {
        try {
            Cliente nuevoCliente = clienteService.createFromDTO(clienteDTO);
            return ResponseEntity.ok(nuevoCliente);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al crear cliente", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un cliente existente")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.update(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al actualizar cliente", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un cliente")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        try {
            clienteService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error al eliminar cliente", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar cliente por email")
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByEmail(
            @Parameter(description = "Email del cliente") @PathVariable String email) {
        return clienteService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar cliente por número de documento")
    @GetMapping(value = "/documento/{numeroDocumento}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNumeroDocumento(
            @Parameter(description = "Número de documento del cliente") @PathVariable String numeroDocumento) {
        return clienteService.findByNumeroDocumento(numeroDocumento)
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