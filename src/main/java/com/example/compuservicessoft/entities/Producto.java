package com.example.compuservicessoft.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(indexes = @Index(columnList = "nombre"))
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  Integer cantidad;
    private String nombre;
    private float precioVendido;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;
    private float total;
    private String imagen;
    @Column(length = 6000)
    private String descripcion;
    private String estado;
}
