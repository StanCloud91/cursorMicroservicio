package com.example.microservice.controller;

import com.example.microservice.model.Factura;
import com.example.microservice.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Factura", description = "API de gestión de facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Operation(summary = "Obtener todas las facturas", description = "Retorna una lista de todas las facturas en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de facturas encontrada exitosamente")
    @GetMapping
    public List<Factura> findAll() {
        return facturaService.findAll();
    }

    @Operation(summary = "Obtener una factura por ID", description = "Retorna una factura según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Factura> findById(
            @Parameter(description = "ID de la factura a buscar") @PathVariable Long id) {
        return facturaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva factura", description = "Crea una nueva factura en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de factura inválidos")
    })
    @PostMapping
    public ResponseEntity<Factura> save(@RequestBody Factura factura) {
        try {
            Factura nuevaFactura = facturaService.save(factura);
            return ResponseEntity.ok(nuevaFactura);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Buscar facturas por cliente", description = "Retorna una lista de facturas asociadas a un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public List<Factura> findByClienteId(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        return facturaService.findByClienteId(clienteId);
    }

    @Operation(summary = "Buscar factura por número", description = "Retorna una factura según su número")
    @GetMapping("/numero/{numeroFactura}")
    public ResponseEntity<Factura> findByNumeroFactura(
            @Parameter(description = "Número de factura") @PathVariable String numeroFactura) {
        return facturaService.findByNumeroFactura(numeroFactura)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar facturas por rango de fechas", description = "Retorna una lista de facturas emitidas entre las fechas especificadas")
    @GetMapping("/fecha")
    public List<Factura> findByFechaEmisionBetween(
            @Parameter(description = "Fecha de inicio (formato ISO)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Fecha de fin (formato ISO)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return facturaService.findByFechaEmisionBetween(inicio, fin);
    }
} 