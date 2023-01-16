package com.ecom.ecom.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
public class Sale extends CommonEntity {

    @Serial
    private static final long serialVersionUID = -1973628270535512696L;

    @NotBlank(message = "Promo code nécessaire")
    private String promo_code;

    @NotNull(message = "Prix total nécessaire")
    private Double totalPrice;

    private Date created = new Date(System.currentTimeMillis());

    private Integer idClient;

    private Integer idProduct;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Sale sale)) return false;
        return getId().equals(sale.getId()) && getPromo_code().equals(sale.getPromo_code()) && getTotalPrice().equals(sale.getTotalPrice()) && getCreated().equals(sale.getCreated()) && getIdClient().equals(sale.getIdClient()) && getIdProduct().equals(sale.getIdProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPromo_code(), getTotalPrice(), getCreated(), getIdClient(), getIdProduct());
    }
}
