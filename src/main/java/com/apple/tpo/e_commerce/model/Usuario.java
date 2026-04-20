package com.apple.tpo.e_commerce.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioCreador")
    private List<Producto> productos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Carrito> carritos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<OrdenCompra> ordenes;

}
