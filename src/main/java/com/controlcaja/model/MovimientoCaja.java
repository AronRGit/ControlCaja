package com.controlcaja.model;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public record MovimientoCaja(
        String         id,
        LocalDate      fecha,
        String         descripcion,
        MetodoPago     metodoPago,
        BigDecimal     monto,
        TipoMovimiento tipo,
        String         cajero
) {
    public MovimientoCaja {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(
                    "El monto debe ser mayor a cero");
        if (descripcion == null || descripcion.isBlank())
            throw new IllegalArgumentException(
                    "La descripcion es obligatoria");
        if (cajero == null || cajero.isBlank())
            throw new IllegalArgumentException(
                    "El cajero es obligatorio");
    }

    // Lo que realmente ingresa a caja descontando la comisión
    public BigDecimal montoNeto() {
        return monto.subtract(metodoPago.calcularComision(monto))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
