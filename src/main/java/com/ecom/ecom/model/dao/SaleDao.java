package com.ecom.ecom.model.dao;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Entity
@Table(name = "sale")
public class SaleDao implements CommonDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generate
    private Long id;

    @NotBlank(message = "Promo code nécessaire")
    private String promo_code;


    @Column(name = "total_price")
    @NotNull(message = "Prix total nécessaire")
    private Float totalPrice;

    private Date created = new Date(System.currentTimeMillis());

    @Column(name = "id_client")
    private float idClient;

    @Column(name = "id_product")
    private int idProduct;
}
