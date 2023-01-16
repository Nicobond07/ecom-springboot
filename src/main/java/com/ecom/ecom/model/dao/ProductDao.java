package com.ecom.ecom.model.dao;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Blob;

@Data
@Entity
@Table(name = "product")
public class ProductDao implements CommonDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generate
    private Long id;

    @NotBlank(message = "Nom nécessaire")
    private String name;

    @NotBlank(message = "Description nécessaire")
    private String description;

    @Lob
    private Blob image;

    @Column(name = "initial_price")
    @NotNull(message = "Prix initial nécessaire")
    private Float initialPrice;

    private int promo;

    @Column(name = "promo_price")
    private float promoPrice;

    @Column(name = "id_category")
    private int idCategory;


}
