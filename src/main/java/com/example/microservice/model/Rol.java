package com.example.microservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"usuarios", "permisos"})
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_permisos",
        joinColumns = @JoinColumn(name = "rol_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    @JsonManagedReference
    private Set<Permiso> permisos = new HashSet<>();
} 