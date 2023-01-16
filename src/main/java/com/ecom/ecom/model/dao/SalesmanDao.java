package com.ecom.ecom.model.dao;

import lombok.Data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "salesman")
public class SalesmanDao implements CommonDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generate
    private Long id;

    @NotBlank(message = "Pseudo nécessaire")
    private String pseudo;


    @Email
    @NotBlank(message = "Email nécessaire")
    private String email;


    @NotBlank(message = "Password nécessaire")
    private String password;


    @NotBlank(message = "PromoCode nécessaire")
    @Column(name="promo_code")
    private String promoCode;

    @Column(name="number_orders")
    private int numberOrders = 0;

    @Column(name="earned_cash")
    private float earnedCash = 0;

    @Column(name="money_to_send")
    private float moneyToSend = 0;

    // set default content for role
    private String role = "SALESMAN";

    // set default creation date
    private Date created = new Date(System.currentTimeMillis());
}
