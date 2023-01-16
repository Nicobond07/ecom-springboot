package com.ecom.ecom.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.sql.Blob;
import java.util.Objects;

@Getter
@Setter
public class Product extends CommonEntity {

    @Serial
    private static final long serialVersionUID = -403146637613855841L;

    @NotBlank(message = "Nom nécessaire")
    private String name;


    @NotBlank(message = "Description nécessaire")
    private String description;

    @Lob
    private Blob image;


    @NotNull(message = "Prix initial nécessaire")
    private Float initialPrice;

    private int promo;

    private float promoPrice;

    private int idCategory;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Product product)) return false;
        return getId().equals(product.getId()) && getPromo() == product.getPromo() && Float.compare(product.getPromoPrice(), getPromoPrice()) == 0 && getIdCategory() == product.getIdCategory() && getName().equals(product.getName()) && getDescription().equals(product.getDescription()) && Objects.equals(getImage(), product.getImage()) && getInitialPrice().equals(product.getInitialPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getImage(), getInitialPrice(), getPromo(), getPromoPrice(), getIdCategory());
    }
}
