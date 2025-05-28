package com.example.microservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "permisos")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"roles"})
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private String modulo;

    @Column(nullable = false)
    private boolean puedeCrear = false;

    @Column(nullable = false)
    private boolean puedeLeer = false;

    @Column(nullable = false)
    private boolean puedeActualizar = false;

    @Column(nullable = false)
    private boolean puedeEliminar = false;

    @ManyToMany(mappedBy = "permisos")
    @JsonBackReference
    private Set<Rol> roles = new HashSet<>();
} 