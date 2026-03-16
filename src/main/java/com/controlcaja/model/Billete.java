package com.controlcaja.model;

import java.math.BigDecimal;
import java.math.RoundingMode;


public record Billete(BigDecimal denominacion, int cantidad) {

    public Billete {
        if (cantidad < 0) throw new IllegalArgumentException(
                "La cantidad no puede ser negativa");
    }

    public BigDecimal subtotal() {
        return denominacion.multiply(new BigDecimal(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
