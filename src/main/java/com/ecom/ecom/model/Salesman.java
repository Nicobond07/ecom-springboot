package com.ecom.ecom.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
public class Salesman extends CommonEntity {

    @Serial
    private static final long serialVersionUID = 5158492118516652575L;

    @NotBlank(message = "Pseudo nécessaire")
    private String pseudo;

    @Email
    @NotBlank(message = "Email nécessaire")
    private String email;

    @NotBlank(message = "Password nécessaire")
    private String password;

    @NotBlank(message = "PromoCode nécessaire")
    private String promoCode;

    private int numberOrders = 0;

    private float earnedCash = 0;

    private float moneyToSend = 0;

    // set default content for role
    private String role = "SALESMAN";

    // set default creation date
    private Date created = new Date(System.currentTimeMillis());

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Salesman salesman)) return false;
        return getId().equals(salesman.getId()) && getNumberOrders() == salesman.getNumberOrders() && Float.compare(salesman.getEarnedCash(), getEarnedCash()) == 0 && Float.compare(salesman.getMoneyToSend(), getMoneyToSend()) == 0 && getPseudo().equals(salesman.getPseudo()) && getEmail().equals(salesman.getEmail()) && getPassword().equals(salesman.getPassword()) && getPromoCode().equals(salesman.getPromoCode()) && getRole().equals(salesman.getRole()) && getCreated().equals(salesman.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPseudo(), getEmail(), getPassword(), getPromoCode(), getNumberOrders(), getEarnedCash(), getMoneyToSend(), getRole(), getCreated());
    }
}
